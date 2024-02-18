FROM openjdk:21
COPY ./build/libs/identity-1.0.0.jar identity.jar

EXPOSE 8082

ENTRYPOINT ["java","-jar","identity.jar"]