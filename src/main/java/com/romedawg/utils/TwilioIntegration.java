package com.romedawg.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.romedawg.controller.MetraController;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwilioIntegration {

    private static final Logger log = LoggerFactory.getLogger(TwilioIntegration.class);

    public static void sendMessage(String messageBody) {

        try {
            log.debug("sending twilio message");
            Message message = Message.creator(
                    new PhoneNumber("+1291231233"),  // To number
                    new PhoneNumber("+1989625419"),  // From number
                    "Hello world!"                    // SMS body
            ).create();

            log.debug(message.getSid());
        } catch (final ApiException e) {
            log.error("failed to send twilio messages");
            e.printStackTrace();
        }
        log.debug("Completed sending twilio message");
    }

}
