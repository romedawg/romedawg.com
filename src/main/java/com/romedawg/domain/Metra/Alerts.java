package com.romedawg.domain.Metra;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Alerts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    private Boolean isDeleted;


}

/***
 {
 "id": "cms_alert1081",
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
