package com.romedawg.domain.Metra;


import javax.persistence.*;

@Entity
@Table(name="stop_time")
public class StopTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "trip_id")
    private String tripId;
    @Column(name="arrival_time")
    private String arrivalTime;
    @Column(name="departure_time")
    private String departureTime;
    @Column(name="stop_id")
    private String stopId;
    @Column(name="stop_sequence")
    private Integer stopSequence;
    @Column(name="pickup_type")
    private Integer pickupType;
    @Column(name="drop_off_type")
    private Integer dropOffType;
    @Column(name="center_boarding")
    private Integer centerBoarding;
    @Column(name="south_boarding")
    private Integer southBoarding;
    @Column(name="bikes_allowed")
    private Integer bikesAllowed;
    @Column(name="notice")
    private Integer notice;

    /***
     * Empty Constructor
     */
    public StopTime() {
    }

    private StopTime(Builder builder){
        this.tripId = builder.tripId;
        this.arrivalTime = builder.arrivalTime;
        this.departureTime = builder.departureTime;
        this.stopId = builder.stopId;
        this.stopSequence = builder.stopSequence;
        this.pickupType = builder.pickupType;
        this.dropOffType = builder.dropOffType;
        this.centerBoarding = builder.centerBoarding;
        this.southBoarding = builder.southBoarding;
        this.bikesAllowed = builder.bikesAllowed;
        this.notice = builder.notice;
    }

    /***
     * Builder Class
     */
    public static final class Builder{
        private String tripId;
        private String arrivalTime;
        private String departureTime;
        private String stopId;
        private Integer stopSequence;
        private Integer pickupType;
        private Integer dropOffType;
        private Integer centerBoarding;
        private Integer southBoarding;
        private Integer bikesAllowed;
        private Integer notice;

        public Builder setTripId(String tripId) {
            this.tripId = tripId;
            return this;
        }

        public Builder setArrivalTime(String arrivalTime) {
            this.arrivalTime = arrivalTime;
            return this;
        }

        public Builder setDepartureTime(String departureTime) {
            this.departureTime = departureTime;
            return this;
        }

        public Builder setStopId(String stopId) {
            this.stopId = stopId;
            return this;
        }

        public Builder setStopSequence(Integer stopSequence) {
            this.stopSequence = stopSequence;
            return this;
        }

        public Builder setPickupType(Integer pickupType) {
            this.pickupType = pickupType;
            return this;
        }

        public Builder setDropOffType(Integer dropOffType) {
            this.dropOffType = dropOffType;
            return this;
        }

        public Builder setCenterBoarding(Integer centerBoarding) {
            this.centerBoarding = centerBoarding;
            return this;
        }

        public Builder setSouthBoarding(Integer southBoarding) {
            this.southBoarding = southBoarding;
            return this;
        }

        public Builder setBikesAllowed(Integer bikesAllowed) {
            this.bikesAllowed = bikesAllowed;
            return this;
        }

        public Builder setNotice(Integer notice) {
            this.notice = notice;
            return this;
        }

        public StopTime build(){
            return new StopTime(this);
        }
    }

    @Override
    public String toString() {
        return "Stop{" +
                "tripId='" + tripId + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", stopId='" + stopId + '\'' +
                ", stopSequence=" + stopSequence +
                ", pickupType=" + pickupType +
                ", dropOffType=" + dropOffType +
                ", centerBoarding=" + centerBoarding +
                ", southBoarding=" + southBoarding +
                ", bikesAllowed=" + bikesAllowed +
                ", notice=" + notice +
                '}';
    }


    public String getTripId() {
        return tripId;
    }

    public StopTime setTripId(String tripId) {
        this.tripId = tripId;
        return this;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public StopTime setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
        return this;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public StopTime setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
        return this;
    }

    public String getStopId() {
        return stopId;
    }

    public StopTime setStopId(String stopId) {
        this.stopId = stopId;
        return this;
    }

    public Integer getStopSequence() {
        return stopSequence;
    }

    public StopTime setStopSequence(Integer stopSequence) {
        this.stopSequence = stopSequence;
        return this;
    }

    public Integer getPickupType() {
        return pickupType;
    }

    public StopTime setPickupType(Integer pickupType) {
        this.pickupType = pickupType;
        return this;
    }

    public Integer getDropOffType() {
        return dropOffType;
    }

    public StopTime setDropOffType(Integer dropOffType) {
        this.dropOffType = dropOffType;
        return this;
    }

    public Integer getCenterBoarding() {
        return centerBoarding;
    }

    public StopTime setCenterBoarding(Integer centerBoarding) {
        this.centerBoarding = centerBoarding;
        return this;
    }

    public Integer getSouthBoarding() {
        return southBoarding;
    }

    public StopTime setSouthBoarding(Integer southBoarding) {
        this.southBoarding = southBoarding;
        return this;
    }

    public Integer getBikesAllowed() {
        return bikesAllowed;
    }

    public StopTime setBikesAllowed(Integer bikesAllowed) {
        this.bikesAllowed = bikesAllowed;
        return this;
    }

    public Integer getNotice() {
        return notice;
    }

    public StopTime setNotice(Integer notice) {
        this.notice = notice;
        return this;
    }
}


/***
 {"trip_id":"BNSF_BN1264_V1_C",
 "arrival_time":"11:47:00",
 "departure_time":"11:47:00",
 "stop_id":"CUS",
 "stop_sequence":28,
 "pickup_type":0,
 "drop_off_type":0,
 "center_boarding":0,
 "south_boarding":0,
 "bikes_allowed":1,
 "notice":0}]
 ***/
