package com.romedawg.schedules;

import com.romedawg.domain.Metra.Route;
import com.romedawg.domain.Metra.Stop;
import com.romedawg.domain.Metra.StopTime;
import com.romedawg.domain.Metra.Trip;
import com.romedawg.repository.Metra.RouteRepository;
import com.romedawg.repository.Metra.StopRepository;
import com.romedawg.repository.Metra.StopTimeRepository;
import com.romedawg.repository.Metra.TripRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Date;


@Component
public class MetraUpdates {

    private static final Logger log = LoggerFactory.getLogger(MetraUpdates.class);

    // Schedule intervals
    private static final int twentyFourHoursMS = 86400000;
    private static final int fifteenMinutesMS = 900000;
    private static final int thirtyMinutesMS = 1800000;

    private RouteRepository routeRepository;
    private StopRepository stopRepository;
    private StopTimeRepository stopTimeRepository;
    private TripRepository tripRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @Value("${METRA_API_USERNAME}")
    private String metraUrlUsername;
    @Value("${METRA_API_PASSWORD}")
    private String metraUrlPassword;

    @Value("${SLACK_WEBHOOK}")
    private String webHookUrl;

    // These are Routes that should rarely change, if ever
    @Scheduled(fixedRate = twentyFourHoursMS)
    private void updateRoutes() {

        log.info("Load Metra Routes every 24 hours");
        String URL = "https://gtfsapi.metrarail.com/gtfs/schedule/routes";
        StringBuffer sb = makeHttpRequest(URL);

        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        Route.Builder[] newRoutes = new Route.Builder[0];

        try {
            newRoutes = objectMapper.readValue(sb.toString(), Route.Builder[].class);
        } catch (JsonProcessingException e) {
            log.error("failed to map stopTime data to stopTime data object");
            e.printStackTrace();
        }

        for (Route.Builder newroute : newRoutes) {
            if (routeRepository.getRouteID(newroute.build().getRouteId()) != null) {
            } else {
                log.info(String.format("Route %s does not exist, adding into db", newroute.build().getRouteId()));
                routeRepository.save(newroute.build());
            }
        }
        log.info("Metra Route loading completed");
    }

    // These are stops that should rarely change
    @Scheduled(fixedRate = twentyFourHoursMS)
    private void updateStops() {

        log.info("Load Metra Stops");
        String URL = "https://gtfsapi.metrarail.com/gtfs/schedule/stops";
        StringBuffer sb = makeHttpRequest(URL);

        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        Stop.Builder[] newStops = new Stop.Builder[0];
        try {
            newStops = objectMapper.readValue(sb.toString(), Stop.Builder[].class);
        } catch (JsonProcessingException e) {
            log.error("failed to map stopTime data to stopTime data object");
            e.printStackTrace();
        }

        for (Stop.Builder newstop : newStops) {
            if (stopRepository.findStopByStopId(newstop.build().getStopId()) != null) {
                continue;
            } else {
                log.info(String.format("stop id: %s", newstop.build().getStopId()));
                stopRepository.save(newstop.build());
            }
        }

        log.info("Metra Stops loading completed");

    }

    @Scheduled(fixedRate = fifteenMinutesMS)
    private void updateStopTimes() {

        log.info("Load Metra Stops Times");
        String URL = "https://gtfsapi.metrarail.com/gtfs/schedule/stop_times";
        StringBuffer sb = makeHttpRequest(URL);

        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        StopTime.Builder[] newStopTimes = new StopTime.Builder[0];

        try {
            newStopTimes = objectMapper.readValue(sb.toString(), StopTime.Builder[].class);
        } catch (JsonProcessingException e) {
            log.error("failed to map stopTime data to stopTime data object");
            e.printStackTrace();
        }

        // TODO Fix - do not add duplicates
        for (StopTime.Builder newStopTime : newStopTimes) {
            stopTimeRepository.save(newStopTime.build());
        }

        log.info("Metra Stop Times loading completed");

    }

    @Scheduled(fixedRate = fifteenMinutesMS)
    private void updateTrips(){

        log.info("Load Metra Trip Times");
        String URL = "https://gtfsapi.metrarail.com/gtfs/schedule/trips";
        StringBuffer sb = makeHttpRequest(URL);

        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        Trip.Builder[] newTrips = new Trip.Builder[0];

        try {
            newTrips = objectMapper.readValue(sb.toString(), Trip.Builder[].class);
        } catch (JsonProcessingException e) {
            log.error("failed to map trip data to Trip data object");
            e.printStackTrace();
        }

        for (Trip.Builder newTrip : newTrips) {
            if (tripRepository.getTripById(newTrip.build().getTripId()) != null) {
                continue;
            } else {
                tripRepository.save(newTrip.build());
            }
        }


        log.info("Metra Trip loading completed");
    }

    private StringBuffer makeHttpRequest(String URL){

        log.info("Executing http request to " + URL);
        StringBuffer buffer = new StringBuffer();

        String auth = metraUrlUsername + ":" + metraUrlPassword;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authenticatedValue = "Basic " + new String(encodedAuth);

        // Used for alerting to Slack
        int httpStatusCode = 0;

        try {
            java.net.URL url = new URL(URL);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestProperty("Authorization", authenticatedValue );
            httpConn.setRequestMethod("GET");
            httpStatusCode = httpConn.getResponseCode();

            log.debug(String.format("Response code is %s for URL: %s", httpStatusCode, URL));

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            String inputLine;
            while ((inputLine = bufReader.readLine()) != null){
                buffer.append(inputLine);
            }
        }catch (Exception e){
            log.error(String.format("Response code is %s for URL: %s", httpStatusCode, URL));
            SlackAlerts("HTTP REQUEST failed for URL: " + URL + " STATUS CODE: " + httpStatusCode);
            e.printStackTrace();
        }
        return buffer;
    }


    private void SlackAlerts(String slackMessage) {
        Slack slack = Slack.getInstance();
        log.info(webHookUrl);

        Date date = new Date();

        Payload payload = Payload.builder().text(date + " - " + slackMessage).build();

        try {
            WebhookResponse response = slack.send(webHookUrl, payload);
            log.info("response from slack: " + response);
        } catch (IOException e){
            e.printStackTrace();
        }

        log.error(slackMessage);

    }

    public MetraUpdates(RouteRepository routeRepository, StopRepository stopRepository, StopTimeRepository stopTimeRepository, TripRepository tripRepository) {
        this.routeRepository = routeRepository;
        this.stopRepository = stopRepository;
        this.stopTimeRepository = stopTimeRepository;
        this.tripRepository = tripRepository;
    }
}