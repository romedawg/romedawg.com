package com.romedawg.utils;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.romedawg.domain.Metra.Alert;
import com.romedawg.domain.Metra.Product;

import javax.persistence.Column;
import java.io.IOException;

public class AlertDeserializer extends StdDeserializer<Alert> {


    protected AlertDeserializer() {
        this(null);
    }

    public AlertDeserializer(Class<?> vc) {
        super(vc);
    }

    @Column(name = "alert_url")
    private String alertUrl;
    @Column(name = "alert_header")
    private String alertHeader;
    @Column(name = "alert_description")
    private String alertDescription;

    @Override
    public Alert deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode alertNode = jp.getCodec().readTree(jp);
        Alert alert = new Alert();
        alert.setId(String.valueOf(alertNode.get("id").textValue()));
        alert.setIsDeleted(Boolean.valueOf(alertNode.get("is_deleted").textValue()));
        alert.setTripUpdate(String.valueOf(alertNode.get("trip_update").textValue()));
        alert.setVehicle(String.valueOf(alertNode.get("vehicle").textValue()));
        alert.setAlertStartDate(String.valueOf(alertNode.get("alert").get("active_period").get(0).get("start").get("low").textValue()));
        alert.setAlertEndDate(String.valueOf(alertNode.get("alert").get("active_period").get(0).get("end").get("low").textValue()));
        alert.setTripId(String.valueOf(alertNode.get("alert").get("informed_entity").get(0).get("trip").get("trip_id").textValue()));
        alert.setRouteId(String.valueOf(alertNode.get("alert").get("informed_entity").get(0).get("trip").get("route_id").textValue()));
        alert.setStartTime(String.valueOf(alertNode.get("alert").get("informed_entity").get(0).get("trip").get("start_time").textValue()));
        alert.setStartDate(String.valueOf(alertNode.get("alert").get("informed_entity").get(0).get("trip").get("start_date").textValue()));
        alert.setDirectionId(String.valueOf(alertNode.get("alert").get("informed_entity").get(0).get("trip").get("direction_id").textValue()));
        alert.setScheduleRelationship((Integer) alertNode.get("alert").get("informed_entity").get(0).get("trip").get("schedule_relationship").numberValue());
        alert.setStopId(String.valueOf(alertNode.get("alert").get("informed_entity").get(0).get("stop_id").textValue()));
        alert.setCause((Integer) alertNode.get("alert").get("cause").numberValue());
        alert.setEffect((Integer) alertNode.get("alert").get("effect").numberValue());
        alert.setAlertUrl(alertNode.get("alert").get("url").get("translation").get(0).get("text").textValue());
        alert.setAlertHeader(alertNode.get("alert").get("header_text").get("translation").get(0).get("text").textValue());
        alert.setAlertDescription(alertNode.get("alert").get("description_text").get("translation").get(0).get("text").textValue());

        return alert;
    }
}
