package com.romedawg.domain.customSchedules;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="hinsdale_schedule")
public class HinsdaleSchedule {

    private Long id;
    private String tripId;
    private String pickupTime;
    private String arrivalTime;

    public HinsdaleSchedule() {
    }

    public HinsdaleSchedule(String tripId, String pickupTime, String arrivalTime) {
        this.tripId = tripId;
        this.pickupTime = pickupTime;
        this.arrivalTime = arrivalTime;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
