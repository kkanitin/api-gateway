FROM openjdk:21
COPY ./build/libs/api_gateway-1.0.0.jar api_gateway.jar

EXPOSE 8089

ENTRYPOINT ["java","-jar","api_gateway.jar"]