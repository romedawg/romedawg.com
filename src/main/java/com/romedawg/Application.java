package com.romedawg;

import com.twilio.Twilio;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.ServletContext;

@EnableScheduling
@SpringBootApplication()
public class Application {

    public static void main(String[] args) {
        Twilio.init(System.getenv("TWILIO_API_KEY"), System.getenv("TWILIO_API_TOKEN"));

        SpringApplication.run(Application.class, args);

    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry meterRegistry){
        return new TimedAspect(meterRegistry);
    }

}

