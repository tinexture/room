#
# Build stage
#
FROM maven:3.8.2-jdk-11 AS build
COPY . .
RUN mvn clean package -Pprod -DskipTests

#
# Package stage
#
FROM openjdk:11-jdk-slim
COPY --from=build /target/room-management-system.war room-management-system.war
# ENV PORT=8080
EXPOSE 9999
ENTRYPOINT ["java","-jar","room-management-system.war"]