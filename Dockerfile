#Download base image ubuntu 20.04
FROM ubuntu:20.04

LABEL maintaier="admin@romedawg.com"
LABEL version="0.1"
LABEL description="This is custom Docker Image for romedawg.com"

COPY . /opt/gradle/source
WORKDIR /opt/gradle/source
RUN gradle build

# Update Ubuntu Software repository & install postgres
RUN set -x \
    && apt-get update \
    && apt-get -y install default-jre default-jdk \
    && apt-get -y install software-properties-common
