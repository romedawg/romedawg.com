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

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public String getStopDesc() {
        return stopDesc;
    }

    public void setStopDesc(String stopDesc) {
        this.stopDesc = stopDesc;
    }

    public Long getStopLat() {
        return stopLat;
    }

    public void setStopLat(Long stopLat) {
        this.stopLat = stopLat;
    }

    public Long getStopLon() {
        return stopLon;
    }

    public void setStopLon(Long stopLon) {
        this.stopLon = stopLon;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getStopUrl() {
        return stopUrl;
    }

    public void setStopUrl(String stopUrl) {
        this.stopUrl = stopUrl;
    }

    public Integer getWheelchairBoarding() {
        return wheelchairBoarding;
    }

    public void setWheelchairBoarding(Integer wheelchairBoarding) {
        this.wheelchairBoarding = wheelchairBoarding;
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
