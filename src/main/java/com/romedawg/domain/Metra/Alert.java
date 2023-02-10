package com.romedawg.domain.Metra;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.romedawg.utils.AlertDeserializer;
import com.romedawg.utils.ProductDeserializer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="alert")
@JsonDeserialize(using = AlertDeserializer.class)
public class Alert {

    @Id
    @Column(name="id")
    private String id;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @Column(name = "trip_update")
    private String tripUpdate;
    @Column(name = "vehicle")
    private String vehicle;
    @Column(name = "alertStartDate")
    private String alertStartDate;
    @Column(name = "alert_end_date")
    private String alertEndDate;
    @Column(name = "trip_id")
    private String tripId;
    @Column(name = "route_id")
    private String routeId;
    @Column(name = "direction_id")
    private String directionId;
    @Column(name = "start_date")
    private String startTime;
    @Column(name = "stop_date")
    private String startDate;
    @Column(name = "schedule_relationship")
    private Integer scheduleRelationship;
    @Column(name = "stop_id")
    private String stopId;
    @Column(name = "cause")
    private Integer cause;
    @Column(name = "effect")
    private Integer effect;
    @Column(name = "alert_url")
    private String alertUrl;
    @Column(name = "alert_header")
    private String alertHeader;
    @Column(name = "alert_description")
    private String alertDescription;

    public Alert() {}

//    public Alert(String id, String isDeleted, String tripUpdate, String vehicle, String alertStartDate, String alertEndDate, String tripId, String routeId, String directionId, String startTime, String startDate, String scheduleRelationship, String stopId, String cause, String effect, String alertUrl, String alertHeader, String alertDescription) {
//        this.id = id;
//        this.isDeleted = isDeleted;
//        this.tripUpdate = tripUpdate;
//        this.vehicle = vehicle;
//        this.alertStartDate = alertStartDate;
//        this.alertEndDate = alertEndDate;
//        this.tripId = tripId;
//        this.routeId = routeId;
//        this.directionId = directionId;
//        this.startTime = startTime;
//        this.startDate = startDate;
//        this.scheduleRelationship = scheduleRelationship;
//        this.stopId = stopId;
//        this.cause = cause;
//        this.effect = effect;
//        this.alertUrl = alertUrl;
//        this.alertHeader = alertHeader;
//        this.alertDescription = alertDescription;
//    }

    public Alert(Builder builder) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getTripUpdate() {
        return tripUpdate;
    }

    public void setTripUpdate(String tripUpdate) {
        this.tripUpdate = tripUpdate;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getAlertStartDate() {
        return alertStartDate;
    }

    public void setAlertStartDate(String alertStartDate) {
        this.alertStartDate = alertStartDate;
    }

    public String getAlertEndDate() {
        return alertEndDate;
    }

    public void setAlertEndDate(String alertEndDate) {
        this.alertEndDate = alertEndDate;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getDirectionId() {
        return directionId;
    }

    public void setDirectionId(String directionId) {
        this.directionId = directionId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Integer getScheduleRelationship() {
        return scheduleRelationship;
    }

    public void setScheduleRelationship(Integer scheduleRelationship) {
        this.scheduleRelationship = scheduleRelationship;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public Integer getCause() {
        return cause;
    }

    public void setCause(Integer cause) {
        this.cause = cause;
    }

    public Integer getEffect() {
        return effect;
    }

    public void setEffect(Integer effect) {
        this.effect = effect;
    }

    public String getAlertUrl() {
        return alertUrl;
    }

    public void setAlertUrl(String alertUrl) {
        this.alertUrl = alertUrl;
    }

    public String getAlertHeader() {
        return alertHeader;
    }

    public void setAlertHeader(String alertHeader) {
        this.alertHeader = alertHeader;
    }

    public String getAlertDescription() {
        return alertDescription;
    }

    public void setAlertDescription(String alertDescription) {
        this.alertDescription = alertDescription;
    }

    public static class Builder {
        private String id;
        private String isDeleted;
        private String tripUpdate;
        private String vehicle;
        private String alertStartDate;
        private String alertEndDate;
        private String tripId;
        private String routeId;
        private String directionId;
        private String startTime;
        private String startDate;
        private Integer scheduleRelationship;
        private String stopId;
        private Integer cause;
        private Integer effect;
        private String alertUrl;
        private String alertHeader;
        private String alertDescription;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIsDeleted() {
            return isDeleted;
        }

        public void setIsDeleted(String isDeleted) {
            this.isDeleted = isDeleted;
        }

        public String getAlertStartDate() {
            return alertStartDate;
        }

        public String getTripUpdate() {
            return tripUpdate;
        }

        public void setTripUpdate(String tripUpdate) {
            this.tripUpdate = tripUpdate;
        }

        public void setAlertStartDate(String alertStartDate) {
            this.alertStartDate = alertStartDate;
        }

        public String getAlertEndDate() {
            return alertEndDate;
        }

        public void setAlertEndDate(String alertEndDate) {
            this.alertEndDate = alertEndDate;
        }

        public String getTripId() {
            return tripId;
        }

        public void setTripId(String tripId) {
            this.tripId = tripId;
        }

        public String getRouteId() {
            return routeId;
        }

        public void setRouteId(String routeId) {
            this.routeId = routeId;
        }

        public String getDirectionId() {
            return directionId;
        }

        public void setDirectionId(String directionId) {
            this.directionId = directionId;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public Integer getScheduleRelationship() {
            return scheduleRelationship;
        }

        public void setScheduleRelationship(Integer scheduleRelationship) {
            this.scheduleRelationship = scheduleRelationship;
        }

        public String getStopId() {
            return stopId;
        }

        public void setStopId(String stopId) {
            this.stopId = stopId;
        }

        public Integer getCause() {
            return cause;
        }

        public void setCause(Integer cause) {
            this.cause = cause;
        }

        public Integer getEffect() {
            return effect;
        }

        public void setEffect(Integer effect) {
            this.effect = effect;
        }

        public String getAlertUrl() {
            return alertUrl;
        }

        public void setAlertUrl(String alertUrl) {
            this.alertUrl = alertUrl;
        }

        public String getAlertHeader() {
            return alertHeader;
        }

        public void setAlertHeader(String alertHeader) {
            this.alertHeader = alertHeader;
        }

        public String getAlertDescription() {
            return alertDescription;
        }

        public void setAlertDescription(String alertDescription) {
            this.alertDescription = alertDescription;
        }

        public Alert Builder(){
            return new Alert(this);
    }
}







/***
 * id
 * vehicle
 * alert_table ->
 *      start_date
 *      end_date
 * informed_entity
 *      agency_id
 *      route_id
 *      route_type
 *      trip_id
 *      direction_id
 *      start_time
 *      start_date
 *      schedule_relationship
 *      stop_id
 * cause
 * effect
 * url
 * header_text
 * description
 *
 [
     {
         "id":"fa604e0d-90ed-4c74-8fc9-8d30b5cae2c3",
         "is_deleted":false,
         "trip_update":null,
         "vehicle":null,
         "alert":{
             "active_period":
                 [{"start":
                     {"low":"2023-02-02T02:57:42.000Z",
                     "high":0,
                     "unsigned":true
                     },
                 "end":
                     {"low":"2023-02-02T04:14:00.000Z",
                     "high":0,
                     "unsigned":true}
                 }],
             "informed_entity":
                 [{"agency_id":null,
                     "route_id":null,
                     "route_type":null,
                     "trip":{
                         "trip_id":"BNSF_BN1292_V2_C",
                         "route_id":"BNSF",
                         "direction_id":null,
                         "start_time":"18:04:00",
                         "start_date":"2023-02-01",
                         "schedule_relationship":0
                     },
                    "stop_id":null
                }],
         "cause":1,
         "effect":8,
         "url":
             {"translation":
                [{"text":"http://metrarail.com/metra/en/home.html?Twitter=0&Email=0&Website=1&OnBoard=0","language":"en-US"}]
             },
         "header_text":
             {"translation":
                [{"text":"Metra Alert BNSF - Inbound Train #1292 sched. to arrive Chicago at 7:16 PM - may be 178 minutes delayed. ","language":"en-US"}]
             },
         "description_text":
             {"translation":
                [{"text":"Train #1292, scheduled to arrive Chicago at 7:16 PM, may be operating 178 minutes behind schedule. ","language":"en-US"}]
             }
         }
     }
 ]
 ***/

/***
 * https://gtfsapi.metrarail.com/gtfs/alerts
 {
 "id": "cms_alert1081",S
 "is_deleted": false,
 "alert": {
 "url": {
 "translation": [
 {
 "text": "metrarail.com/riding-metra/service-updates?Source=0&Website=1&OnBoard=0&Email=1&Twitter=1"
 }
 ]
 },
 "informed_entity": [
 {
 "route_id": "MD-W"
 }
 ],
 "active_period": [
 {
 "start": {
 "low": "2022-04-22T09:26:00"
 },
 "end": {
 "low": "2022-05-31T11:26:00"
 }
 }
 ],
 "header_text": {
 "translation": [
 {
 "text": "Metra Alert MD-W - Franklin Park Platform Rehabilitation Will Begin April 27, 2022",
 "language": "en-US"
 }
 ]
 },
 "description_text": {
 "translation": [
 {
 "text": "<p>Beginning Wednesday, April 27, through the end of May, the east half of the island platform (Platform #2) will be closed for rehabilitation work.&nbsp; During this phase, 25th Avenue access to platform #2 will be closed.&nbsp; Please allow additional time to reach the in-service platform and exercise caution nea rthe work zones.</p>",
 "language": "en-US"
 }
 ]
 }
 }
 }
 ***/

}
