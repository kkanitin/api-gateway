FROM gradle:8.5-jdk21 AS build

WORKDIR /app

COPY build.gradle .
COPY settings.gradle .

COPY src src

RUN gradle build --no-daemon

FROM openjdk:21 AS run

COPY --from=build /app/build/libs/mock-1.0.0.jar mock.jar

EXPOSE 8080

CMD ["java", "-jar", "mock.jar"]