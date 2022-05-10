package com.romedawg.utils;

import com.romedawg.schedules.MetraUpdates;
import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Date;

public class Utils {

    @Value("${SLACK_WEBHOOK}")
    private static String webHookUrl;

    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    public static StringBuffer makeHttpRequest(String URL, String username, String password){

        log.info("Executing http request to " + URL);
        StringBuffer buffer = new StringBuffer();

        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authenticatedValue = "Basic " + new String(encodedAuth);

        // Used for alerting to Slack
        int httpStatusCode = 0;

        try {
            java.net.URL url = new URL(URL);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestProperty("Authorization", authenticatedValue );
            httpConn.setRequestMethod("GET");
            httpStatusCode = httpConn.getResponseCode();

            log.debug(String.format("Response code is %s for URL: %s", httpStatusCode, URL));

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            String inputLine;
            while ((inputLine = bufReader.readLine()) != null){
                buffer.append(inputLine);
            }
        }catch (Exception e){
            log.error(String.format("Response code is %s for URL: %s", httpStatusCode, URL));
            SlackAlerts("HTTP REQUEST failed for URL: " + URL + " STATUS CODE: " + httpStatusCode);
            e.printStackTrace();
        }
        return buffer;
    }


    private static void SlackAlerts(String slackMessage) {
        Slack slack = Slack.getInstance();
        log.info(webHookUrl);

        Date date = new Date();

        Payload payload = Payload.builder().text(date + " - " + slackMessage).build();

        try {
            WebhookResponse response = slack.send(webHookUrl, payload);
            log.info("response from slack: " + response);
        } catch (IOException e){
            e.printStackTrace();
        }

        log.error(slackMessage);

    }
}
