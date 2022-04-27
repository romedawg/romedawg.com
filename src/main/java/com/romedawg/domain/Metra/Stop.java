package com.romedawg.domain.Metra;

import javax.persistence.*;

@Entity
@Table(name="stop")
public class Stop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
//    id, stop_id, stop_name, stop_desc, lat, lon, zone_id, stop_url, wheelchair_boarding

    @Column(name = "stop_id")
    private String stopId;

    @Column(name = "stop_name")
    private String stopName;

    @Column(name = "stop_desc")
    private String stopDesc;

    @Column(name = "stop_lat")
    private Long stopLat;

    @Column(name = "stop_lon")
    private Long stopLon;

    @Column(name = "zone_id")
    private String zoneId;

    @Column(name = "stop_url")
    private String stopUrl;

    @Column(name = "wheelchair_boarding")
    private Integer wheelchairBoarding;

    /***
     * Empty Constructor
     */
    public Stop() {
    }


    private Stop(Builder builder){
        this.stopId = builder.stopId;
        this.stopName = builder.stopName;
        this.stopDesc = builder.stopDesc;
        this.stopLat = builder.stopLat;
        this.stopLon = builder.stopLon;
        this.zoneId = builder.zoneId;
        this.stopUrl = builder.stopUrl;
        this.wheelchairBoarding = builder.wheelchairBoarding;
    }

    /***
     * Builder class.
     */
   public static final class Builder {
       private String stopId;
       private String stopName;
       private String stopDesc;
       private Long stopLat;
       private Long stopLon;
       private String zoneId;
       private String stopUrl;
       private Integer wheelchairBoarding;

       public Builder setStopId(String stopId) {
           this.stopId = stopId;
           return this;
       }

       public Builder setStopName(String stopName) {
           this.stopName = stopName;
           return this;
       }

       public Builder setStopDesc(String stopDesc) {
           this.stopDesc = stopDesc;
           return this;
       }

       public Builder setStopLat(Long stopLat) {
           this.stopLat = stopLat;
           return this;
       }

       public Builder setStopLon(Long stopLon) {
           this.stopLon = stopLon;
           return this;
       }

       public Builder setZoneId(String zoneId) {
           this.zoneId = zoneId;
           return this;
       }

       public Builder setStopUrl(String stopUrl) {
           this.stopUrl = stopUrl;
           return this;
       }

       public Builder setWheelchairBoarding(Integer wheelchairBoarding) {
           this.wheelchairBoarding = wheelchairBoarding;
           return this;
       }

        public Stop build(){
            return new Stop(this);
        }
   }



    @Override
    public String toString() {
        return "Stop{" +
                "id=" + id +
                ", stopId='" + stopId + '\'' +
                ", stopName='" + stopName + '\'' +
                ", stopDesc='" + stopDesc + '\'' +
                ", stopLat=" + stopLat +
                ", stopLon=" + stopLon +
                ", zoneId='" + zoneId + '\'' +
                ", stopUrl='" + stopUrl + '\'' +
                ", wheelchairBoarding=" + wheelchairBoarding +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStopId() {
        return stopId;
    }

    public Stop setStopId(String stopId) {
        this.stopId = stopId;
        return this;
    }

    public String getStopName() {
        return stopName;
    }

    public Stop setStopName(String stopName) {
        this.stopName = stopName;
        return this;
    }

    public String getStopDesc() {
        return stopDesc;
    }

    public Stop setStopDesc(String stopDesc) {
        this.stopDesc = stopDesc;
        return this;
    }

    public Long getStopLat() {
        return stopLat;
    }

    public Stop setStopLat(Long stopLat) {
        this.stopLat = stopLat;
        return this;
    }

    public Long getStopLon() {
        return stopLon;
    }

    public Stop setStopLon(Long stopLon) {
        this.stopLon = stopLon;
        return this;
    }

    public String getZoneId() {
        return zoneId;
    }

    public Stop setZoneId(String zoneId) {
        this.zoneId = zoneId;
        return this;
    }

    public String getStopUrl() {
        return stopUrl;
    }

    public Stop setStopUrl(String stopUrl) {
        this.stopUrl = stopUrl;
        return this;
    }

    public Integer getWheelchairBoarding() {
        return wheelchairBoarding;
    }

    public Stop setWheelchairBoarding(Integer wheelchairBoarding) {
        this.wheelchairBoarding = wheelchairBoarding;
        return this;
    }
}

/***
 {"stop_id":"GENEVA",
  "stop_name":"Geneva",
  "stop_desc":"",
  "stop_lat":41.8816667,
  "stop_lon":-88.31,
  "zone_id":"H",
  "stop_url":"https://metrarail.com/maps-schedules/train-lines/UP-W/stations/GENEVA",
  "wheelchair_boarding":1}
 ***/
