package com.romedawg.domain.Metra;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.romedawg.utils.ProductDeserializer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Map;
//
//@Entity
//@Table(name="product")
@JsonDeserialize(using = ProductDeserializer.class)
public class Product {


    private String id;
    private String name;
    private String brandName;
    private String ownerName;
    private String alertUrl;

    public Product(String id, String myid, String name, String brandName, String ownerName, String alertUrl) {
        this.id = id;
        this.name = name;
        this.brandName = brandName;
        this.ownerName = ownerName;
        this.alertUrl = alertUrl;
    }

    public Product(Builder builder) {

        this.id = builder.id;
        this.name = builder.name;
        this.brandName = builder.brandName;
        this.ownerName = builder.ownerName;
        this.alertUrl = builder.alertUrl;
    }

    public Product() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    @SuppressWarnings("unchecked")
//    @JsonProperty("brand")
//    private void unpackNested(Map<String,Object> brand) {
//        this.brandName = (String)brand.get("name");
//        Map<String,String> owner = (Map<String,String>)brand.get("owner");
//        this.ownerName = owner.get("name");
//    }

    // standard getters and setters
    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAlertUrl() {
        return alertUrl;
    }

    public void setAlertUrl(String alertUrl) {
        this.alertUrl = alertUrl;
    }

    public static class Builder {
        public String id;
        private String myid;
        private String name;
        private String brandName;
        private String ownerName;
        private String alertUrl;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getOwnerName() {
            return ownerName;
        }

        public void setOwnerName(String ownerName) {
            this.ownerName = ownerName;
        }

        public String getAlertUrl() {
            return alertUrl;
        }

        public void setAlertUrl(String alertUrl) {
            this.alertUrl = alertUrl;
        }

        public Product build(){
            return new Product(this);
        }
    }
}

/**
 {
 "id": "957c43f2-fa2e-42f9-bf75-6e3d5bb6960a",
 "name": "The Best Product",
 "brand": {
     "id": "9bcd817d-0141-42e6-8f04-e5aaab0980b6",
     "name": "ACME Products",
     "owner": {
         "id": "b21a80b1-0c09-4be3-9ebd-ea3653511c13",
         "name": "Ultimate Corp, Inc."
         }
     },
 "alert": {
     "url": {
         "translation": [{"text": "metrarail.com/riding-metra/service-updates?Source=0&Website=1&OnBoard=0&Email=1&Twitter=1"}]
     }
  }
 }
 **/