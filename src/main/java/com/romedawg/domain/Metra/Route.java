package com.romedawg.domain.Metra;

import javax.persistence.*;

@Entity
@Table(name="route")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "route_id", unique = true)
    private String routeId;

    @Column(name = "route_short_name")
    private String routeShortName;

    @Column(name = "route_long_name")
    private String routeLongName;

    @Column(name = "route_desc")
    private String routeDesc;

    @Column(name = "agency_id")
    private String agencyId;

    @Column(name = "route_type")
    private Integer routeType;

    @Column(name = "route_color")
    private String routeColor;

    @Column(name = "route_text_color")
    private String routeTextColor;

    @Column(name = "route_url")
    private String routeUrl;

    /***
     * Constructor.
     */
    public Route() {
        // empty constructor
    }

    private Route(Builder builder){
        this.routeId = builder.routeId;
        this.routeShortName = builder.routeShortName;
        this.routeLongName =  builder.routeLongName;
        this.routeDesc = builder.routeDesc;
        this.agencyId = builder.agencyId;
        this.routeType = builder.routeType;
        this.routeColor = builder.routeColor;
        this.routeTextColor = builder.routeTextColor;
        this.routeUrl = builder.routeUrl;
    }

    /***
     * Builder class.
     */
    public static final class Builder {
        private String routeId;
        private String routeShortName;
        private String routeLongName;
        private String routeDesc;
        private String agencyId;
        private Integer routeType;
        private String routeColor;
        private String routeTextColor;
        private String routeUrl;

        public Builder setRouteId(String routeId) {
            this.routeId = routeId;
            return this;
        }

        public Builder setRouteShortName(String routeShortName) {
            this.routeShortName = routeShortName;
            return this;
        }

        public Builder setRouteLongName(String routeLongName) {
            this.routeLongName = routeLongName;
            return this;
        }

        public Builder setRouteDesc(String routeDesc) {
            this.routeDesc = routeDesc;
            return this;
        }

        public Builder setAgencyId(String agencyId) {
            this.agencyId = agencyId;
            return this;
        }

        public Builder setRouteType(Integer routeType) {
            this.routeType = routeType;
            return this;

        }

        public Builder setRouteColor(String routeColor) {
            this.routeColor = routeColor;
            return this;
        }

        public Builder setRouteTextColor(String routeTextColor) {
            this.routeTextColor = routeTextColor;
            return this;
        }

        public Builder setRouteUrl(String routeUrl) {
            this.routeUrl = routeUrl;
            return this;
        }

        public Route build(){
            return new Route(this);
        }
    }

    @Override
    public String toString() {
        return "Route{" +
                "routeId='" + routeId + '\'' +
                ", routeShortName='" + routeShortName + '\'' +
                ", routeLongName='" + routeLongName + '\'' +
                ", routeDesc='" + routeDesc + '\'' +
                ", agencyId='" + agencyId + '\'' +
                ", routeType=" + routeType +
                ", routeColor='" + routeColor + '\'' +
                ", routeTextColor='" + routeTextColor + '\'' +
                ", routeUrl='" + routeUrl + '\'' +
                '}';
    }

    public String getRouteId() {
        return routeId;
    }

    public Route setRouteId(String routeId) {
        this.routeId = routeId;
        return this;
    }

    public String getRouteShortName() {
        return routeShortName;
    }

    public Route setRouteShortName(String routeShortName) {
        this.routeShortName = routeShortName;
        return this;
    }

    public String getRouteLongName() {
        return routeLongName;
    }

    public Route setRouteLongName(String routeLongName) {
        this.routeLongName = routeLongName;
        return this;
    }

    public String getRouteDesc() {
        return routeDesc;
    }

    public Route setRouteDesc(String routeDesc) {
        this.routeDesc = routeDesc;
        return this;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public Route setAgencyId(String agencyId) {
        this.agencyId = agencyId;
        return this;
    }

    public Integer getRouteType() {
        return routeType;
    }

    public Route setRouteType(Integer routeType) {
        this.routeType = routeType;
        return this;
    }

    public String getRouteColor() {
        return routeColor;
    }

    public Route setRouteColor(String routeColor) {
        this.routeColor = routeColor;
        return this;
    }

    public String getRouteTextColor() {
        return routeTextColor;
    }

    public Route setRouteTextColor(String routeTextColor) {
        this.routeTextColor = routeTextColor;
        return this;
    }

    public String getRouteUrl() {
        return routeUrl;
    }

    public Route setRouteUrl(String routeUrl) {
        this.routeUrl = routeUrl;
        return this;
    }

}
