FROM openjdk:21
COPY ./build/libs/eureka_server-1.0.0.jar eureka_server.jar

EXPOSE 8089

ENTRYPOINT ["java","-jar","eureka_server.jar"]