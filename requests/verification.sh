#!/usr/bin/env bash

set -euo pipefail

base_url="http://localhost:8080"
timeout_seconds=60
started_at="$(date +%s)"

command -v jq >/dev/null 2>&1 || {
  echo "jq is required but not installed" >&2
  exit 1
}

start_body='{
  "payment": {
    "paymentId": "payment-1",
    "customerId": "customer-1",
    "amount": "100.00",
    "currency": "PLN",
    "country": "PL"
  }
}'

echo "POST ${base_url}/v1/verifications"
echo "Request:"
echo "${start_body}"
echo

start_response="$(
  curl -sS \
    -X POST "${base_url}/v1/verifications" \
    -H 'Content-Type: application/json' \
    -d "${start_body}"
)"

echo "Response:"
printf '%s' "${start_response}" | jq .
echo

verification_id="$(printf '%s' "${start_response}" | jq -r '.verificationId')"

while true; do
  get_endpoint="${base_url}/v1/verifications/${verification_id}"

  echo "GET ${get_endpoint}"

  get_response="$(
    curl -sS \
      -X GET "${get_endpoint}"
  )"

  echo "Response:"
  printf '%s' "${get_response}" | jq .
  echo

  status="$(printf '%s' "${get_response}" | jq -r '.status')"

  if [[ "${status}" == "COMPLETED" || "${status}" == "FAILED" ]]; then
    break
  fi

  now="$(date +%s)"
  if (( now - started_at >= timeout_seconds )); then
    echo "Timed out after ${timeout_seconds} seconds waiting for verification ${verification_id}" >&2
    exit 1
  fi

  sleep 1
done
