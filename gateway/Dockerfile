FROM gradle:8.5-jdk21 AS build

WORKDIR /app

COPY build.gradle .
COPY settings.gradle .

COPY src src

RUN gradle build --no-daemon

FROM openjdk:21 AS run

COPY --from=build /app/build/libs/api_gateway-1.0.0.jar api_gateway.jar

EXPOSE 8090

CMD ["java", "-jar", "api_gateway.jar"]