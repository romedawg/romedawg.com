package com.romedawg;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.ServletContext;

@EnableScheduling
@SpringBootApplication()
public class Application {

    public static void main(String[] args) {
        Twilio.init(System.getenv("TWILIO_API_KEY"), System.getenv("TWILIO_API_TOKEN"));

        SpringApplication.run(Application.class, args);

    }

}

