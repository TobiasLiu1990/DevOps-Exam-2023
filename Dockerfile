FROM maven:3.6-jdk-11 as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

FROM adoptopenjdk/openjdk11:alpine-slim
COPY --from=builder /app/target/*.jar /app/application.jar

#ENV AWS_REGION=eu-west-1

ENTRYPOINT ["java","-jar","/app/application.jar"]