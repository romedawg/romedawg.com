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



}

/***
 {"id":"9a4000c1-8414-4c4c-9074-2e811a70dd31",
  "is_deleted":false,
  "trip_update":null,
  "vehicle":null,
  "alert":
    {"active_period":
        [{"start":
         {
            "low":"2022-01-03T19:04:47.000Z","high":0,"unsigned":true},
            "end":{"low":"2022-12-31T09:00:00.000Z","high":0,"unsigned":true}
         }
         ]
        ,"informed_entity":[{"agency_id":null,"route_id":null,"route_type":null,"trip":null,"stop_id":"VANBUREN"}],"cause":1,"effect":8,"url":{"translation":[{"text":"http://metrarail.com/metra/en/home.html?Twitter=0&Website=1&OnBoard=0&Email=0&Category=Elevator","language":"en-US"}]},"header_text":{"translation":[{"text":"Metra Alert  - The VanBuren Street Michigan Avenue elevator temporarily out of service ","language":"en-US"}]},"description_text":{"translation":[{"text":"The VanBuren Michigan Ave  elevator is temporarily out of service. Recommended alternative accessible locations are Museum Campus and Millenium Station. Metra will provide  updated information once service has been restored. ","language":"en-US"}]}}}
 ***/
