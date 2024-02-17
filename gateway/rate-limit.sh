#!/bin/bash

request_count=50  # Adjust the number of requests as needed
#interval_seconds=1  # Adjust the interval between requests

for i in $(seq 1 $request_count); do
    curl --location 'localhost:8090/MOCK-SERVICE-RATE/rate'
    echo " Request $i sent."
#    sleep $interval_seconds
done

echo "All requests completed."
