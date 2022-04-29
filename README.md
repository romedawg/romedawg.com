# Romedawg.com

# Java playground for roman

## Problem domain
Overarching goal is to reduce friction.  I need to check 3 different sites to validate information 
otherwise I can miss a train, bus, etc..  or a train is delayed, other there's no bike available.

This is also a place to add other items, TBD

Commute
 - Eliminate Multiple places to look for train/bus times.  Divvy bikes(Are they available at the station i expect)
 - Train delays.
   - Alerting if a train is late/time switched
 - Bike Alert
   - I'm taking the 5:05 train, there's no bikes available when i need to leave the office, so I need to go to a different location
 - Is a bus/train late
   - Add tracker information, helps w/ pickups, etc..
   

# how to deploy?
Build the jar file
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
docker run -e POSTGRES_HOST="172.17.0.1" -e POSTGRES_DATABASE="romedawg" -e POSTGRES_USERNAME="postgres" -e POSTGRES_PASSWORD="password"  -p 8080:8080 romedawg

# PASS IN AN ENV FILE WITH THE CREDS BELOW
docker run -e POSTGRES_HOST="172.17.0.1" --env-file mycredentials.txt -p 8080:8080 romedawg

./gradlew build; docker build -t romedawg .;docker build -t romedawg .; docker run -e POSTGRES_HOST="172.17.0.1" --env-file mycredentials.txt -p 8080:8080 romedawg
```

# what components are needed?
postgres(See below how to setup locally)

POSTGRES_HOST=[localhost|ipaddr]
POSTGRES_DATABASE=romedawg 
POSTGRES_USERNAME=roman 
POSTGRES_PASSWORD=password 
METRA_API_USERNAME=key 
METRA_API_PASSWORD=password


## Setting up postgres
https://hub.docker.com/repository/docker/romedawg/postgres-14

pull down a postgres image and run it locally
 - docker pull romedawg/postgres-14
 - docker run -e ADMIN_USER=roman -e ADMIN_PASSWORD=password -e POSTGRES_DATABASE=romedawg -p 5432:5432 --name postgres romedawg/postgres-14


#### TODO
- Alerting if updates fail
  - Sent to Slack channel?
  - Send a text alert? integrate Twilio
- Alerts(if data refreshes fail)
    - If a train emergency/delay occurs
    - Flatten Alert JSON data(multiple nested objects)
- Deploy to ECS
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