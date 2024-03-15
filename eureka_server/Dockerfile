FROM gradle:8.5-jdk21 AS build

WORKDIR /app

COPY build.gradle .
COPY settings.gradle .

COPY src src

RUN gradle build --no-daemon

FROM openjdk:21 AS run

RUN #adduser --system --group app-user

COPY --from=build /app/build/libs/eureka_server-1.0.0.jar eureka_server.jar

EXPOSE 8089

CMD ["java", "-jar", "eureka_server.jar"]