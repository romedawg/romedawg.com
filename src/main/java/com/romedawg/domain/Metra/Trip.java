package com.romedawg.domain.Metra;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="route_id")
    private String routeId;

    @Column(name="service_id")
    private String serviceId;

    @Column(name="trip_id", unique = true)
    private String tripId;

    @Column(name="trip_headsign")
    private String tripHeadsign;

    @Column(name="block_id")
    private String blockId;

    @Column(name="shape_id")
    private String shapeId;

    @Column(name="direction_id")
    private Integer directionId;

    /***
     * Empty Constructor
     */
    public Trip() {
    }

    private Trip(Builder builder){
        this.routeId = builder.routeId;
        this.serviceId = builder.serviceId ;
        this.tripId = builder.tripId;
        this.tripHeadsign = builder.tripHeadsign;
        this.blockId = builder.blockId;
        this.shapeId = builder.shapeId;
        this.directionId = builder.directionId;
    }

    /***
     * Builder Class
     */
    public static final class Builder{
        private String routeId;
        private String serviceId;
        private String tripId;
        private String tripHeadsign;
        private String blockId;
        private String shapeId;
        private Integer directionId;

        public Builder setRouteId(String routeId) {
            this.routeId = routeId;
            return this;
        }

        public Builder setServiceId(String serviceId) {
            this.serviceId = serviceId;
            return this;
        }

        public Builder setTripId(String tripId) {
            this.tripId = tripId;
            return this;
        }

        public Builder setTripHeadsign(String tripHeadsign) {
            this.tripHeadsign = tripHeadsign;
            return this;
        }

        public Builder setBlockId(String blockId) {
            this.blockId = blockId;
            return this;
        }

        public Builder setShapeId(String shapeId) {
            this.shapeId = shapeId;
            return this;
        }

        public Builder setDirectionId(Integer directionId) {
            this.directionId = directionId;
            return this;
        }

        public Trip build(){
            return new Trip(this);
        }
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", routeId='" + routeId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", tripId='" + tripId + '\'' +
                ", tripHeadsign='" + tripHeadsign + '\'' +
                ", blockId='" + blockId + '\'' +
                ", shapeId='" + shapeId + '\'' +
                ", directionId=" + directionId +
                '}';
    }

    public String getRouteId() {
        return routeId;
    }

    public Trip setRouteId(String routeId) {
        this.routeId = routeId;
        return this;
    }

    public String getServiceId() {
        return serviceId;
    }

    public Trip setServiceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public String getTripId() {
        return tripId;
    }

    public Trip setTripId(String tripId) {
        this.tripId = tripId;
        return this;
    }

    public String getTripHeadsign() {
        return tripHeadsign;
    }

    public Trip setTripHeadsign(String tripHeadsign) {
        this.tripHeadsign = tripHeadsign;
        return this;
    }

    public String getBlockId() {
        return blockId;
    }

    public Trip setBlockId(String blockId) {
        this.blockId = blockId;
        return this;
    }

    public String getShapeId() {
        return shapeId;
    }

    public Trip setShapeId(String shapeId) {
        this.shapeId = shapeId;
        return this;
    }

    public Integer getDirectionId() {
        return directionId;
    }

    public Trip setDirectionId(Integer directionId) {
        this.directionId = directionId;
        return this;
    }
}


//https://gtfsapi.metrarail.com/gtfs/schedule/trips
// {"route_id":"BNSF",
//        "service_id":"A1",
//        "trip_id":"BNSF_BN1200_V1_A",
//        "trip_headsign":"Chicago Union Station",
//        "block_id":"",
//        "shape_id":"BNSF_IB_1",
//        "direction_id":1}
