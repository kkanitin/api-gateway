FROM gradle:8.5-jdk21 AS build

WORKDIR /app

COPY build.gradle .
COPY settings.gradle .

COPY src src

RUN gradle build --no-daemon

FROM openjdk:21 AS run

COPY --from=build /app/build/libs/identity-1.0.0.jar identity.jar

EXPOSE 8082

CMD ["java", "-jar", "identity.jar"]