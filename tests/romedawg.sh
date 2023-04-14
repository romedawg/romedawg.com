#!/bin/bash
set -o pipefaile
set -ux
#/bin/bash -c "trap : TERM INT; /opt/examples/json_exporter --config.file /opt/examples/testing_config.yml & wait"
#/bin/bash -c "trap : TERM INT; /opt/examples/json_exporter --config.file /opt/examples/blueshift_config.yml & wait"

API_KEY=""
BLUESHIFT_DIR="/opt/examples/blueshift_metrics"

function startHttpServer() {
  apt-get update;apt-get install python3
  cd /opt/examples;python3 -m http.server 8000 &
}

function loadBlueshiftData() {
  echo "load blueshift data to prometheus"
  curl "http://localhost:7979/probe?module=default&target=http://localhost:8000/blueshift_metrics/blueshift.json"
}

echo "Starting HTTP server"
startHttpServer

## TODO adjust scrape_blueshift_data.sh for custom metrics
#echo "Generate blueshift data from New Relic, last 30 days"
#bash "${BLUESHIFT_DIR}"/scrape_blueshift_data.sh "${API_KEY}" "${BLUESHIFT_DIR}"
#echo "loading blueshift metrics done"

HTTP_SERVER_STATUS_CODE=""
while [ "${HTTP_SERVER_STATUS_CODE}" != 200 ];
  do
    HTTP_SERVER_STATUS_CODE=$(curl --write-out %{http_code} --silent --output /dev/null http://localhost:8000)
  done

echo "http server running, moving on"

echo "starting json_exporter"
/opt/examples/json_exporter --config.file /opt/examples/blueshift_config.yml &

JSON_EXPORTER_STATUS_CODE=""
while [ "${JSON_EXPORTER_STATUS_CODE}" != 200 ];
  do
    JSON_EXPORTER_STATUS_CODE=$(curl --write-out %{http_code} --silent --output /dev/null http://localhost:7979)
  done

echo "json exporter running, loading blueshift data"

loadBlueshiftData

sleep infinity
