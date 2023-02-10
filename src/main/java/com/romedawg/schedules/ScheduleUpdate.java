package com.romedawg.schedules;

import com.romedawg.domain.Metra.Alert;
import com.romedawg.repository.Metra.AlertRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataInput;
import java.io.IOException;

/***
 * Goals of this class
 * Scan for alerts.
 * Transform/parse them to Alerts Domain
 * Send a text message to subsribed users(ie. roman)
 * TODO setup a subscription page.
 * For each each
 */

@Component
public class ScheduleUpdate {

    private static Logger log = LoggerFactory.getLogger(ScheduleUpdate.class);
    ObjectMapper objectMapper = new ObjectMapper();
    private static final int fifteenMinutesMS = 900000;
    private AlertRepository alertRepository;

    @Value("${METRA_API_USERNAME}")
    private String metraUrlUsername;
    @Value("${METRA_API_PASSWORD}")
    private String metraUrlPassword;
    @Value("${SLACK_WEBHOOK}")
    private String webHookUrl;

    private final String AlertTest = "{\"id\":\"fa604e0d-90ed-4c74-8fc9-8d30b5cae2c3\",\n" +
            " \"is_deleted\":false,\n" +
            " \"trip_update\":null,\n" +
            " \"vehicle\":null,\n" +
            " \"alert\":{\n" +
            " \"active_period\":\n" +
            " [{\"start\":\n" +
            " {\"low\":\"2023-02-02T02:57:42.000Z\",\n" +
            " \"high\":0,\n" +
            " \"unsigned\":true\n" +
            " },\n" +
            " \"end\":\n" +
            " {\"low\":\"2023-02-02T04:14:00.000Z\",\n" +
            " \"high\":0,\n" +
            " \"unsigned\":true}\n" +
            " }],\n" +
            " \"informed_entity\":\n" +
            " [{\"agency_id\":null,\n" +
            " \"route_id\":null,\n" +
            " \"route_type\":null,\n" +
            " \"trip\":{\n" +
            " \"trip_id\":\"BNSF_BN1292_V2_C\",\n" +
            " \"route_id\":\"BNSF\",\n" +
            " \"direction_id\":null,\n" +
            " \"start_time\":\"18:04:00\",\n" +
            " \"start_date\":\"20230201\",\n" +
            " \"schedule_relationship\":0\n" +
            " },\n" +
            " \"stop_id\":null}],\n" +
            " \"cause\":1,\n" +
            " \"effect\":8,\n" +
            " \"url\":\n" +
            " {\"translation\":\n" +
            " [{\"text\":\"http://metrarail.com/metra/en/home.html?Twitter=0&Email=0&Website=1&OnBoard=0\",\"language\":\"en-US\"}]\n" +
            " },\n" +
            " \"header_text\":\n" +
            " {\"translation\":\n" +
            " [{\"text\":\"Metra Alert BNSF - Inbound Train #1292 sched. to arrive Chicago at 7:16 PM - may be 178 minutes delayed. \",\"language\":\"en-US\"}]\n" +
            " },\n" +
            " \"description_text\":\n" +
            " {\"translation\":\n" +
            " [{\"text\":\"Train #1292, scheduled to arrive Chicago at 7:16 PM, may be operating 178 minutes behind schedule. \",\"language\":\"en-US\"}]\n" +
            " }\n" +
            " }\n" +
            " }";

    public ScheduleUpdate(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Scheduled(fixedRate = fifteenMinutesMS)
    private void alertRequest() throws IOException {

        log.info("d2 json object");
        Alert alert = objectMapper.readValue(AlertTest, Alert.class);

        System.out.println(alert.getId());
        System.out.println(alert.getIsDeleted());
        System.out.println(alert.getTripUpdate());
        System.out.println(alert.getVehicle());
        System.out.println(alert.getAlertStartDate());
        System.out.println(alert.getAlertEndDate());
        System.out.println(alert.getTripId());
        System.out.println(alert.getRouteId());
        System.out.println(alert.getStartDate());
        System.out.println(alert.getStartTime());
        System.out.println(alert.getDirectionId());
        System.out.println(alert.getScheduleRelationship());
        System.out.println(alert.getStopId());
        System.out.println(alert.getCause());
        System.out.println(alert.getEffect());
        System.out.println(alert.getAlertUrl());
        System.out.println(alert.getAlertHeader());
        System.out.println(alert.getAlertDescription());

        Alert newAlert = new Alert();
        newAlert = objectMapper.readValue(AlertTest, Alert.class);


        log.info("trying to save the object to the database meow");
//        newAlert = objectMapper.readValue(newAlert, Alert.Builder[].class);
        alertRepository.save(newAlert);
    }
}
