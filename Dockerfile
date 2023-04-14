#Download base image ubuntu 20.04
FROM openjdk:11

LABEL maintaier="admin@romedawg.com"
LABEL version="0.1"
LABEL description="This is custom Docker Image for romedawg.com"

COPY /build/libs/romedawg.jar /opt/romedawg.jar
WORKDIR /opt/

# Tjos wprls
#CMD ["java", "-jar", "-Dspring.profiles.active=dev", "/opt/romedawg.jar"]

CMD ["java", "-Xms4096M", "-Xmx6000M","-Dcom.sun.management.jmxremote","-Dcom.sun.management.jmxremote.port=9010", "-Dcom.sun.management.jmxremote.local.only=false","-Dcom.sun.management.jmxremote.authenticate=false","-Dcom.sun.management.jmxremote.ssl=false","-jar", "-Dspring.profiles.active=dev", "/opt/romedawg.jar"]
#     "-Dcom.sun.management.jmxremote",
#     "-Dcom.sun.management.jmxremote.port=9010",
#     "-Dcom.sun.management.jmxremote.local.only=false",
#     "-Dcom.sun.management.jmxremote.authenticate=false",
#     "-Dcom.sun.management.jmxremote.ssl=false ",
#     "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}",
#     "-jar",
#     "/opt/romedawg.jar"]

# Testing jmx_agent exporter
#CMD ["java", "-javaagent:/Users/romanrafacz/Downloads/jmx_exporter/jmx_prometheus_javaagent_java6/target/jmx_prometheus_javaagent_java6-0.18.1-SNAPSHOT.jar=12345:/tmp/config.yaml", "-jar", "-Dspring.profiles.active=dev", "/opt/romedawg.jar"]

#java -jar -Dspring.profiles.active=dev \
#-Dcom.sun.management.jmxremote \
#-Dcom.sun.management.jmxremote.port=9010 \
#-Dcom.sun.management.jmxremote.rmi.port=9010 \
#-Dcom.sun.management.jmxremote.local.only=false \
#-Dcom.sun.management.jmxremote.authenticate=false \
#-Dcom.sun.management.jmxremote.ssl=false \
#/Users/romanrafacz/workspace/romans_projects/romedawg/build/libs/romedawg.jar
