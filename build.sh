#!/bin/bash

# Build JAR files
pushd eureka_server >/dev/null
./gradlew clean build
popd >/dev/null

pushd gateway >/dev/null
./gradlew clean build
popd >/dev/null

pushd identity >/dev/null
./gradlew clean build
popd >/dev/null

pushd mock-service >/dev/null
./gradlew clean build
popd >/dev/null

# Start Docker Compose
docker-compose up --build -d