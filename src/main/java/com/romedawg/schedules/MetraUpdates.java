package com.romedawg.schedules;

import com.romedawg.domain.Metra.Route;
import com.romedawg.domain.Metra.Stop;
import com.romedawg.domain.Metra.StopTime;
import com.romedawg.domain.Metra.Trip;
import com.romedawg.domain.customSchedules.HinsdaleSchedule;
import com.romedawg.repository.Metra.RouteRepository;
import com.romedawg.repository.Metra.StopRepository;
import com.romedawg.repository.Metra.StopTimeRepository;
import com.romedawg.repository.Metra.TripRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import com.romedawg.repository.Metra.customSchedules.HinsdaleRepository;
import com.romedawg.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;


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
    private HinsdaleRepository hinsdaleRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @Value("${METRA_API_USERNAME}")
    private String metraUrlUsername;
    @Value("${METRA_API_PASSWORD}")
    private String metraUrlPassword;

//     These are Routes that should rarely change, if ever
//    @Scheduled(fixedRate = twentyFourHoursMS)
//    private void updateRoutes(){
//
//        log.info("Load Metra Routes every 24 hours");
//        String URL = "https://gtfsapi.metrarail.com/gtfs/schedule/routes";
//        StringBuffer sb = Utils.makeHttpRequest(URL, metraUrlUsername, metraUrlPassword);
//
//        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
//        Route.Builder[] newRoutes = new Route.Builder[0];
//
//        try {
//            newRoutes = objectMapper.readValue(sb.toString(), Route.Builder[].class);
//        } catch (JsonProcessingException e) {
//            log.error("failed to map stopTime data to stopTime data object", e );
//        }
//
//        for (Route.Builder newroute : newRoutes) {
//            if (routeRepository.getRouteID(newroute.build().getRouteId()) != null) {
//            } else {
//                log.info(String.format("Route %s does not exist, adding into db", newroute.build().getRouteId()));
//                routeRepository.save(newroute.build());
//            }
//        }
//        log.info("Metra Route loading completed");
//    }
//
//    // These are stops that should rarely change
//    @Scheduled(fixedRate = twentyFourHoursMS)
//    private void updateStops() {
//
//        log.info("Load Metra Stops");
//        String URL = "https://gtfsapi.metrarail.com/gtfs/schedule/stops";
//        StringBuffer sb = Utils.makeHttpRequest(URL, metraUrlUsername, metraUrlPassword);
//
//        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
//        Stop.Builder[] newStops = new Stop.Builder[0];
//        try {
//            newStops = objectMapper.readValue(sb.toString(), Stop.Builder[].class);
//        } catch (JsonProcessingException e) {
//            log.error("failed to map stopTime data to stopTime data object");
//            e.printStackTrace();
//        }
//
//        for (Stop.Builder newstop : newStops) {
//            if (stopRepository.findStopByStopId(newstop.build().getStopId()) != null) {
//                continue;
//            } else {
//                stopRepository.save(newstop.build());
//            }
//        }
//
//        log.info("Metra Stops loading completed");
//
//    }
//
//    @Scheduled(fixedRate = fifteenMinutesMS)
//    private void updateTrips(){
//
//        log.info("Load Metra Trip Times");
//        String URL = "https://gtfsapi.metrarail.com/gtfs/schedule/trips";
//        StringBuffer sb = Utils.makeHttpRequest(URL, metraUrlUsername, metraUrlPassword);
//
//        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
//        Trip.Builder[] newTrips = new Trip.Builder[0];
//
//        try {
//            newTrips = objectMapper.readValue(sb.toString(), Trip.Builder[].class);
//        } catch (JsonProcessingException e) {
//            log.error("failed to map trip data to Trip data object", e);
//        }
//
//        for (Trip.Builder newTrip : newTrips) {
//            if (tripRepository.getTripById(newTrip.build().getTripId()) != null) {
//                continue;
//            } else {
//                tripRepository.save(newTrip.build());
//            }
//        }
//
//        log.info("Metra Trip loading completed");
//    }
//
//    @Scheduled(fixedRate = fifteenMinutesMS)
//    private void updateStopTimes() {
//
//        // Trip needs to be loaded first
//        if (tripRepository.getTripCount() == 0){
//            try{
//                TimeUnit.SECONDS.sleep(30);
//            }catch (InterruptedException ie){
//                ie.printStackTrace();
//            }
//        }
//
//        log.info("Load Metra Stops Times");
//        String URL = "https://gtfsapi.metrarail.com/gtfs/schedule/stop_times";
//        StringBuffer sb = Utils.makeHttpRequest(URL, metraUrlUsername, metraUrlPassword);
//
//        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
//        StopTime.Builder[] newStopTimes = new StopTime.Builder[0];
//
//        try {
//            newStopTimes = objectMapper.readValue(sb.toString(), StopTime.Builder[].class);
//        } catch (JsonProcessingException e) {
//            log.error("failed to map stopTime data to stopTime data object");
//            e.printStackTrace();
//        }
//
//        for (StopTime.Builder newStopTime : newStopTimes) {
//            stopTimeRepository.save(newStopTime.build());
//        }
//
//        log.info("Metra Stop Times loading completed");
//
//    }

    @Scheduled(fixedRate = fifteenMinutesMS)
    private void updateHinsdaleSchedule() {

        log.debug("Updating Hinsdale Schedule Domain object");

        List<String> BNSFIds = tripRepository.getTripIdByStopId("HINSDALE");

        for (String tripID: BNSFIds){


            // checkout if the arrival time already exist.
            // Each trip_id can have 4 of the same arrival/departure times.. believe that's because 4 train carts per trip_id
            String hinsdaleDepartureTime = stopTimeRepository.getArrivalTime(tripID, "HINSDALE");
            String cusArrivalTime = stopTimeRepository.getArrivalTime(tripID, "CUS");
            long travelMinutes = 0;
            LocalTime timeConvertedDepTime;
            LocalTime timeConvertedArrivalTime;
            if (cusArrivalTime.startsWith("24")){
                log.debug("Chicago Union Station starts with 24 hour, skipping");
                break;
            }else {
                timeConvertedDepTime = LocalTime.parse( hinsdaleDepartureTime ) ;
                timeConvertedArrivalTime = LocalTime.parse( cusArrivalTime ) ;
                travelMinutes = ChronoUnit.MINUTES.between(timeConvertedDepTime,timeConvertedArrivalTime );
            }

            log.debug("hinsdale deptature time: " + timeConvertedDepTime+ "arrival time: " + timeConvertedArrivalTime);
            log.debug("travel time: " + travelMinutes);

            String checkHinsdaleStopArrivalTime = hinsdaleRepository.getTripDepartureTime(hinsdaleDepartureTime);
            if (checkHinsdaleStopArrivalTime == null){
                HinsdaleSchedule hinsdaleSchedule = new HinsdaleSchedule("Union Station", cusArrivalTime, "Hinsdale", hinsdaleDepartureTime, travelMinutes, tripID);
                hinsdaleRepository.save(hinsdaleSchedule);
            }

        }
    }

    public MetraUpdates(RouteRepository routeRepository, StopRepository stopRepository, StopTimeRepository stopTimeRepository, TripRepository tripRepository, HinsdaleRepository hinsdaleRepository) {
        this.routeRepository = routeRepository;
        this.stopRepository = stopRepository;
        this.stopTimeRepository = stopTimeRepository;
        this.tripRepository = tripRepository;
        this.hinsdaleRepository = hinsdaleRepository;
    }
}