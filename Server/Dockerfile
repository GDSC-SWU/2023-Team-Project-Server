FROM openjdk:17-jdk
LABEL authors="gdsc-swu-2"
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]