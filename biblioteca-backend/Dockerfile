FROM openjdk:11

LABEL author="lucianoortizsilva@gmail.com"

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src

EXPOSE 8080