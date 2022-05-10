package com.romedawg.domain.customSchedules;

import javax.persistence.*;

@Entity
@Table(name="hinsdale_schedule")
public class HinsdaleSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "trip_id")
    private String tripId;

    @Column(name = "departure_time")
    private String departureTime;

    @Column(name = "departure_location")
    private String departureLocation;

    @Column(name = "arrival_time")
    private String arrivalTime;

    @Column(name = "arrival_location")
    private String arrivalLocation;

    @Column(name = "travel_minutes")
    private long travelMinutes;

    public HinsdaleSchedule(String arrivalLocation, String arrivalTime, String departureLocation, String departureTime, long travelMinutes, String tripId) {
        this.arrivalLocation = arrivalLocation;
        this.arrivalTime = arrivalTime;
        this.departureLocation = departureLocation;
        this.departureTime = departureTime;
        this.travelMinutes = travelMinutes;
        this.tripId = tripId;
    }

    public void HinsdaleScheduleCustom(String arrivalLocation, String arrivalTime, String departureLocation, String departureTime, long travelMinutes, String tripId) {
        this.arrivalLocation = arrivalLocation;
        this.arrivalTime = arrivalTime;
        this.departureLocation = departureLocation;
        this.departureTime = departureTime;
        this.travelMinutes = travelMinutes;
        this.tripId = tripId;
    }

    public HinsdaleSchedule() {

    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    public String getArrivalLocation() {
        return arrivalLocation;
    }

    public void setArrivalLocation(String arrivalLocations) {
        this.arrivalLocation = arrivalLocations;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getPickupTime() {
        return departureTime;
    }

    public void setPickupTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public long getTravel_minutes() {
        return travelMinutes;
    }

    public void setTravel_minutes(long travelMinutes) {
        this.travelMinutes = travelMinutes;
    }
}
