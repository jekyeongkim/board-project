# Docker file

# jdk 11 Image Start
FROM openjdk:17
# 인자 정리 - Jar
ARG JAR_FILE=build/libs/*.jar
# Jar File Copy
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]