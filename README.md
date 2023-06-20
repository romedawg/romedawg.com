# Romedawg.com

# Java playground for roman

## Personal Goal
How to ease my commute

## Problem domain
When commuting any number of scenarios can occur, I am trying to eliminate  friction.
    - I need to check 3 different sites to validate information(otherwise I can miss a train, bus, etc.. )  
    - I can also take 2 different trains or stop(depending on when i leave or where I'm going) consolidating this info is helpful.
    - No alerting(when train is delayed, etc..)
    - No alerting(if nearby bike is not available)

My goal is to create automatic alert, texting(when next train is available), etc.. so all i have to do is leave the office and 
catch a train.

Commute
 - Eliminate Multiple places to look for train/bus times/bike availability.
 - Train delays.
   - Alerting if a train is late/time switched
   - Alerts via Slack and/or Twilio integration
 - Bike Alert
   - I'm taking the 5:05 train, there's no bikes available when I need to leave the office, so I need to go to a different location
 - Is a bus/train late
   - Add tracker information, helps w/ pickups, etc..

Notes
 - SMS based text messages for communicating with family members.
 - Twilio integration -  can send and receive notes based on topics
   - Example, I can send a text message with Costco - Grassfed butter or Hardware - ducktape, shovel, rope
   - I could then send a text - Get Hardware and be given a list of items I need.
   

# how to deploy?
Build the jar file

Credentials needed for jar
```
export POSTGRES_HOST=postgres
export POSTGRES_DATABASE=romedawg
export POSTGRES_USERNAME=roman
export POSTGRES_PASSWORD=password
export METRA_API_USERNAME=username
export METRA_API_PASSWORD=password
export SLACK_WEBHOOK=https://hooks.slack.com/services/###Webhoook
export SPRING_PROFILES_ACTIVE=dev
export HEAP_MEMORY_GB=6000M
export TWILIO_API_KEY=key
export TWILIO_API_TOKEN=token
```

```
./gradlew build

# you can run test the jar localy
java -jar -Dspring.profiles.active=dev ./build/libs/romedawg.jar
```

Create the docker container
```
docker build -t romedawg .
```

Run the container
```
docker run -e POSTGRES_HOST="172.17.0.1" -e POSTGRES_DATABASE="romedawg" -e POSTGRES_USERNAME="postgres" -e POSTGRES_PASSWORD="password"  -p 8080:8080 --name romedawg romedawg

# PASS IN AN ENV FILE WITH THE CREDS BELOW
docker run -e POSTGRES_HOST="172.17.0.1" --env-file mycredentials.txt -p 8080:8080 romedawg

# Running via docker credentials file
./gradlew build; docker build -t romedawg .; docker run -e POSTGRES_HOST="172.17.0.1" --env-file mycredentials.txt -p 8080:8080 --name romedawg romedawg


# Running via Jar, enabling JMX
java -jar -Dspring.profiles.active=dev \
-Dcom.sun.management.jmxremote \
-Dcom.sun.management.jmxremote.port=9010 \
-Dcom.sun.management.jmxremote.rmi.port=9010 \
-Dcom.sun.management.jmxremote.local.only=false \
-Dcom.sun.management.jmxremote.authenticate=false \
-Dcom.sun.management.jmxremote.ssl=false \
./build/libs/romedawg.jar

# New Relic Monitoring
java -javaagent:./newrelic/newrelic.jar -jar ./build/libs/romedawg.jar
```

# what components are needed?

## ENV Variables
- SPRING_PROFILES_ACTIVE=dev
- POSTGRES_HOST=[localhost|ipaddr]
- POSTGRES_DATABASE=romedawg 
- POSTGRES_USERNAME=romedawg 
- POSTGRES_PASSWORD=password 
- METRA_API_USERNAME=key 
- METRA_API_PASSWORD=password
- SLACK_WEBHOOK=<SLACK_WEBHOOK>
- TWILIO_API_KEY=TWILIO_KEY
- TWILIO_API_TOKEN=TWILIO_TOKEN


## Setting up postgres
https://hub.docker.com/repository/docker/romedawg/postgres-14

pull down a postgres image and run it locally
Set these variables
```
POSTGRES_DATABASE=romedawg 
ADMIN_USER=roman
ADMIN_PASSWORD=password
```
 - docker pull romedawg/postgres-14
 - docker run -d -e ADMIN_USER=roman -e ADMIN_PASSWORD=password -e POSTGRES_DATABASE=romedawg -p 5432:5432 --name postgres romedawg/postgres-14


#### TODO
- Alerts
    - Flatten Alert JSON data(multiple nested objects)
    - If a train emergency/delay occurs
      - Slack alert? text alert?
- Deploy to ECS using https://github.com/romedawg/aws_infra
  - Provision AWS infrastructure to support this
- Cleanup StopTimes(as those can be refreshed)
- Views
    - Viewing schedules based on train likes to and from City
- Stop times
   - Check if trip_id + arrival_time + departure_time already exist? otherwise skip add.
- Domain Joins - stoptime - stop, etc..
  - Custom queries
- Add Divvy Data
- Add Pace Bus Data
- Display View
- TESTS!

### Divvy Json feeds
https://gbfs.divvybikes.com/gbfs/gbfs.json
```
"feeds": [
        {
          "name": "system_information",
          "url": "https://gbfs.divvybikes.com/gbfs/en/system_information.json"
        },
        {
          "name": "station_information",
          "url": "https://gbfs.divvybikes.com/gbfs/en/station_information.json"
        },
        {
          "name": "station_status",
          "url": "https://gbfs.divvybikes.com/gbfs/en/station_status.json"
        },
        {
          "name": "free_bike_status",
          "url": "https://gbfs.divvybikes.com/gbfs/en/free_bike_status.json"
        },
        {
          "name": "system_hours",
          "url": "https://gbfs.divvybikes.com/gbfs/en/system_hours.json"
        },
        {
          "name": "system_calendar",
          "url": "https://gbfs.divvybikes.com/gbfs/en/system_calendar.json"
        },
        {
          "name": "system_regions",
          "url": "https://gbfs.divvybikes.com/gbfs/en/system_regions.json"
        },
        {
          "name": "system_alerts",
          "url": "https://gbfs.divvybikes.com/gbfs/en/system_alerts.json"
        }
      ]
    }
```

# Monitoring
Acuator/Micrometer are setup and expose prometheus metrics

 - You can use `docker-compose /tests/romedawg.test.yml up` to test locally
   - http://localhost:9090/ - prometheus instance