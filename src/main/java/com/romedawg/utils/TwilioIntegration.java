package com.romedawg.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.romedawg.controller.MetraController;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwilioIntegration {

    private static final Logger log = LoggerFactory.getLogger(TwilioIntegration.class);

    public static void sendMessage(String messageBody) {


        try {
            Message message = Message.creator(
                    new PhoneNumber("+17082997663"),  // To number
                    new PhoneNumber("+19896254794asdf"),  // From number
                    messageBody                       // SMS body
            ).create();
        }catch (Exception e) {
            log.error("failed to map stopTime data to stopTime data object");
            e.printStackTrace();
        }

        log.debug("Completed sending twilio message");
    }

}
