# Romedawg.com

# Java playground for roman

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

