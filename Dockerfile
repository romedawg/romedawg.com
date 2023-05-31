#Download base image ubuntu 20.04
FROM openjdk:11

LABEL maintaier="admin@romedawg.com"
LABEL version="0.1"
LABEL description="This is custom Docker Image for romedawg.com"

COPY /build/libs/romedawg.jar /opt/romedawg.jar
COPY /newrelic /opt/newrelic
WORKDIR /opt/

# Basic
#CMD ["java", "-jar", "-Dspring.profiles.active=dev", "/opt/romedawg.jar"]

# Configuring New Relic Agent
CMD ["java", "-javaagent:/opt/newrelic/newrelic.jar", "-Xms4096M", "-Xmx6000M","-Dcom.sun.management.jmxremote","-Dcom.sun.management.jmxremote.port=9010", "-Dcom.sun.management.jmxremote.local.only=false","-Dcom.sun.management.jmxremote.authenticate=false","-Dcom.sun.management.jmxremote.ssl=false","-jar", "-Dspring.profiles.active=dev", "/opt/romedawg.jar"]

# Testing jmx_agent exporter
#CMD ["java", "-javaagent:/Users/romanrafacz/Downloads/jmx_exporter/jmx_prometheus_javaagent_java6/target/jmx_prometheus_javaagent_java6-0.18.1-SNAPSHOT.jar=12345:/tmp/config.yaml", "-jar", "-Dspring.profiles.active=dev", "/opt/romedawg.jar"]
