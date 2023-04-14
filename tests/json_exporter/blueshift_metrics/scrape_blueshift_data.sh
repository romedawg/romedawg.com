#!/bin/bash

set -ueo pipefail

set -x

API_KEY="$1"
DIR="$2"

function payload_by_month {
  local metric=$1
  PAYLOAD='{"query":"{ actor { account(id: 89496) { nrql(query: \"SELECT messageId, numeric(eventInBSCampaign) - numeric(eventInBS) as activationTime, eventInBS, eventInBSCampaign, timestampEpoch FROM '"${metric}"' since 30 days ago LIMIT MAX\") { results } } }}", "variables":""}'
  echo "${PAYLOAD}"
}

# BlueshiftAnalyticsProxyHeartBeat all data per month
#curl https://api.newrelic.com/graphql \
# -H 'Content-Type: application/json' \
# -H "API-Key: ${API_KEY}" \
# --data-binary "$(payload_by_month BlueshiftAnalyticsProxyHeartBeat)" \
# -o "${DIR}/blueshift.json"


# get only metrics where activationTime <= 1500 for the past month
function payload_by_1_point_5_seconds {
  local metric=$1
  PAYLOAD='{"query":"{ actor { account(id: 89496) { nrql(query: \"SELECT messageId, (numeric(eventInBSCampaign) - numeric(eventInBS)) as activationTime, eventInBS, eventInBSCampaign, timestampEpoch FROM '"${metric}"' since 30 days ago LIMIT MAX\") { results } } }}", "variables":""}'
  echo "${PAYLOAD}"
}

curl https://api.newrelic.com/graphql \
 -H 'Content-Type: application/json' \
 -H "API-Key: ${API_KEY}" \
 --data-binary "$(payload_by_1_point_5_seconds BlueshiftAnalyticsProxyHeartBeat)" \
 -o "${DIR}/blueshift.json"