package com.romedawg;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication()
public class Application {

    public static void main(String[] args) {
        System.out.println("twilio api key" + System.getenv("TWILIO_API_KEY") );
        Twilio.init(System.getenv("TWILIO_API_KEY"), System.getenv("TWILIO_API_TOKEN"));

        SpringApplication.run(Application.class, args);

    }

}
