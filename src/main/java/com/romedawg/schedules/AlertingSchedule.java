package com.romedawg.schedules;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.romedawg.domain.Metra.Route;
import com.romedawg.repository.RouteRepository;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;


@Component
public class AlertingSchedule {

    @Value("${METRA_API_USERNAME}")
    private String metraUrlUsername;
    @Value("${METRA_API_PASSWORD}")
    private String metraUrlPassword;

//    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    ObjectMapper objectMapper = new ObjectMapper();

    private RouteRepository routeRepository;

    public AlertingSchedule(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Scheduled(fixedRate = 2000)
    void UpdateRoutes() throws JsonProcessingException {

        System.out.println("test");

        System.out.println("update outing test");
        String URL = "https://gtfsapi.metrarail.com/gtfs/schedule/routes";
        StringBuffer sb = makeHttpRequest(URL);

        System.out.println("about to hit the Route Builder/objec mapper");
        System.out.println(objectMapper.getPropertyNamingStrategy());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        Route.Builder[] newRoutes = objectMapper.readValue(sb.toString(), Route.Builder[].class);


//        System.out.println("loading metra routes");
//        List<Route> existingRoutes = routeRepository.findAll();

        System.out.println("Iterate over the routes");
        for (Route.Builder newroute:newRoutes){
            System.out.println(String.format("new url request, route id: %s", newroute.build().getRouteId()));
//            if (routeRepository.findRouteID(newroute.build().getRouteId()) == ""){
//                System.out.println(String.format("%s does not exist, adding it.", newroute.build().getRouteId()));
//            }
        }
        System.out.println("metra routes done loading");

        System.out.println(sb);

    }

    private StringBuffer makeHttpRequest(String URL){

        StringBuffer buffer = new StringBuffer();

        String auth = metraUrlUsername + ":" + metraUrlPassword;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authenticatedValue = "Basic " + new String(encodedAuth);

        try {
            java.net.URL url = new URL(URL);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestProperty("Authorization", authenticatedValue );
            httpConn.setRequestMethod("GET");

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            String inputLine;
            while ((inputLine = bufReader.readLine()) != null){
                buffer.append(inputLine);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return buffer;
    }


    // Get this working
    private void SlackAlerts() throws SlackApiException, IOException {
        Slack slack = Slack.getInstance();

        String slackToken = "";

        // Initialize an API Methods client with the given token
        MethodsClient methods = slack.methods(slackToken);

        // Build a request object
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel("#metra-alerts") // Use a channel ID `C1234567` is preferrable
                .text(":wave: Hi from a bot written in Java!")
                .build();

        // Get a response as a Java object
        ChatPostMessageResponse response = methods.chatPostMessage(request);

    }
}