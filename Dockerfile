#Download base image ubuntu 20.04
FROM openjdk:11

LABEL maintaier="admin@romedawg.com"
LABEL version="0.1"
LABEL description="This is custom Docker Image for romedawg.com"

COPY /build/libs/romedawg.jar /opt/romedawg.jar
WORKDIR /opt/

CMD ["java", "-jar", "-Dspring.profiles.active=dev", "/opt/romedawg.jar"]

