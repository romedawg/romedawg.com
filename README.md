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

```

# what components are needed?
 - postgres

export POSTGRES_HOST=localhost
export POSTGRES_DATABASE="romedawg"
export POSTGRES_USERNAME="postgres"
export POSTGRES_PASSWORD="password"

METRA_API_USERNAME=f1f57661005181a1238b7d958fdb889f
METRA_API_PASSWORD=e9152c2ae51643ca236a3521d46d4f39


## Setting up postgres localy
https://hub.docker.com/repository/docker/romedawg/postgres-14

pull down a postgres image and run it locall
 - docker pull romedawg/postgres-14
 - docker run -e ADMIN_USER=roman -e ADMIN_PASSWORD=password -e POSTGRES_DATABASE=posgres -p 5432:5432 --name postgres romedawg/postgres-14

