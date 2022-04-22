package com.romedawg.schedules;

import com.romedawg.domain.Metra.Stop;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

@Component
public class AlertingSchedule {

    @Scheduled(fixedRate = 2000)
    void UpdateAlertingSchedule(){
        // create a client
        var client = HttpClient.newHttpClient();

        // create a request
        var request = HttpRequest.newBuilder(
                        URI.create("https://gtfsapi.metrarail.com/gtfs/schedule/stops"))
                .header("accept", "application/json")
                .build();

        // use the client to send the request
//        var response = client.send(request, new JsonBodyHandler<>(Stop.class));

        // the response:
        System.out.println("alerting test");
    }
}
