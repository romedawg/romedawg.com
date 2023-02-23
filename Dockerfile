#Download base image ubuntu 20.04
FROM openjdk:11

LABEL maintaier="admin@romedawg.com"
LABEL version="0.1"
LABEL description="This is custom Docker Image for romedawg.com"

COPY /build/libs/romedawg.jar /opt/romedawg.jar
WORKDIR /opt/

CMD ["java", "-jar", "-Dspring.profiles.active=dev", "/opt/romedawg.jar"]

#CMD ["java",
#     "-jar",
#     "-Xms4096M",
#     "-Xmx6000M",
#     "-Dcom.sun.management.jmxremote",
#     "-Dcom.sun.management.jmxremote.port=9010",
#     "-Dcom.sun.management.jmxremote.local.only=false",
#     "-Dcom.sun.management.jmxremote.authenticate=false",
#     "-Dcom.sun.management.jmxremote.ssl=false ",
#     "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}",
#     "/opt/romedawg.jar"]
