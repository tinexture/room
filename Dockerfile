FROM maven:3.8.2-jdk-11 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:11-jdk-slim
COPY --from=build /target/room-management-system.jar room-management-system.jar
# ENV PORT=8080
EXPOSE 4564
ENTRYPOINT ["java","-jar","room-management-system.jar"]
