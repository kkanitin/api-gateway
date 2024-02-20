@echo on

@REM build jar file
CD eureka_server
call gradlew clean build
CD ../gateway
call gradlew clean build
CD ../identity
call gradlew clean build
CD ../mock-service
call gradlew clean build

CD ..
call docker-compose up --build -d