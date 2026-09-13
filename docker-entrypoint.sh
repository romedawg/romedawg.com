#!/usr/bin/env bash

set -ux
set -o pipefail

HEAP_MEMORY=${HEAP_MEMORY:-2048m}
STACK_SIZE=${STACK_SIZE:-4m}
NEW_RELIC_CONFIG_TEMPLATE="${NEW_RELIC_HOME}/templates/newrelic.yml.tmpl"
NEW_RELIC_CONFIG="${NEW_RELIC_HOME}/newrelic.yml"

## Configure Rest API Authentication
envsubst < "${NEW_RELIC_CONFIG_TEMPLATE}" > "${NEW_RELIC_CONFIG}"
chmod 600 "${NEW_RELIC_CONFIG}"

NEW_RELIC_OPTS=("-javaagent:${NEW_RELIC_HOME}/newrelic.jar" "-Dnewrelic.config.labels='${NEW_RELIC_LABELS}")

JAVA_OPTS+=("-Dcom.sun.management.jmxremote")
JAVA_OPTS+=("-Dcom.sun.management.jmxremote.port=9010")
JAVA_OPTS+=("-Dcom.sun.management.jmxremote.rmi.port=9010")
JAVA_OPTS+=("-Dcom.sun.management.jmxremote.ssl=false")
JAVA_OPTS+=("-Dcom.sun.management.jmxremote.authenticate=false")
JAVA_OPTS+=("-Djava.rmi.server.hostname=0.0.0.0")
JAVA_OPTS+=("-Djava.awt.headless=true")
JAVA_OPTS+=("-Xms${HEAP_MEMORY}")
JAVA_OPTS+=("-Xmx${HEAP_MEMORY}")
JAVA_OPTS+=("-Xss${STACK_SIZE}")
JAVA_OPTS+=("-XX:+DisableExplicitGC")
JAVA_OPTS+=("-XX:+UseG1GC")
JAVA_OPTS+=("-XX:+AlwaysPreTouch")
JAVA_OPTS+=("-XX:+UseStringDeduplication")
JAVA_OPTS+=("-XX:+ParallelRefProcEnabled")
JAVA_OPTS+=("-Dspring.profiles.active=${PROFILE}")

SETUP_DIR="/var/logs"
#SETUP_LOG="/var/logs/setup.log"
#
#mkdir -p "${SETUP_DIR}"
#echo -n '' > "${SETUP_LOG}" # truncate the file which can possibly be not empty
#
#echo "BEFORE SETUP ################################"
#
#/usr/local/bin/setup.sh $$ | tee "${SETUP_LOG}" &

echo "Start from ${JAVA_JAR_FILE}"

# shellcheck disable=SC2206
COMMAND_OPTS=("${JAVA_OPTS[@]}" "${NEW_RELIC_OPTS[@]}")
java "${COMMAND_OPTS[@]}" -jar "${JAVA_JAR_FILE}" "${JENKINS_OPTS[@]}"
