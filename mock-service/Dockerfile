FROM openjdk:21
COPY ./build/libs/mock-1.0.0.jar mock.jar

EXPOSE 8089

ENTRYPOINT ["java","-jar","mock.jar"]