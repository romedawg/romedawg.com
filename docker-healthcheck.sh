#!/bin/bash
set -x

RESPONSE=$(curl -s -o /tmp/health_body -w "%{http_code}" localhost:8080/health)

if [ "$RESPONSE" -ne 200 ]; then
  exit 1
fi

BODY=$(cat /tmp/health_body)
EXPECTED='{"healthcheck":"healthy"}'

if [ "$BODY" = "$EXPECTED" ]; then
  exit 0
else
  exit 1
fi