#Download base image ubuntu 20.04
FROM ubuntu:22.04

LABEL maintaier="admin@romedawg.com"
LABEL description="This is custom Docker Image for romedawg.com"


ARG VERSION=1.0.0
ARG PROFILE

ENV APP_NAME=listomania
ENV VERSION=${VERSION}
ENV PROFILE=${PROFILE}
ENV NEW_RELIC_HOME=/opt/newrelic
ENV JAVA_JAR_DIR=/opt
ENV JAVA_JAR_FILE=${JAVA_JAR_DIR}/listomania-${VERSION}.jar
ENV NEW_RELIC_LABELS=listomania


COPY /build/libs/listomania-${VERSION}.jar ${JAVA_JAR_FILE}
COPY /newrelic "${NEW_RELIC_HOME}"
COPY docker-entrypoint.sh /usr/local/bin/docker-entrypoint.sh
COPY docker-healthcheck.sh /usr/local/bin/docker-healthcheck.sh
WORKDIR /opt/

RUN set -x \
  && apt-get update -y \
  && apt-get install -y openjdk-21-jdk openjdk-21-jre \
  && apt-get install -y gettext \
  && rm -rf /var/cache/*

RUN chmod +x /usr/local/bin/docker-entrypoint.sh

# Basic
#CMD ["java", "-jar", "-Dspring.profiles.active=dev", "/opt/romedawg.jar"]

# Configuring New Relic Agent
#CMD [ "java", "-javaagent:/opt/newrelic/newrelic.jar", "-Xms4096M", "-Xmx6000M","-Dcom.sun.management.jmxremote","-Dcom.sun.management.jmxremote.port=9010", "-Dcom.sun.management.jmxremote.local.only=false","-Dcom.sun.management.jmxremote.authenticate=false","-Dcom.sun.management.jmxremote.ssl=false","-jar", "-Dspring.profiles.active=${PROFILE}", "/opt/listomania-1.0.0.jar"]

ENTRYPOINT [ "/usr/local/bin/docker-entrypoint.sh"]

# Testing jmx_agent exporter
#CMD ["java", "-javaagent:/Users/romanrafacz/Downloads/jmx_exporter/jmx_prometheus_javaagent_java6/target/jmx_prometheus_javaagent_java6-0.18.1-SNAPSHOT.jar=12345:/tmp/config.yaml", "-jar", "-Dspring.profiles.active=dev", "/opt/romedawg.jar"]
