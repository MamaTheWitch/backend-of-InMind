# https://spring.io/guides/gs/spring-boot-docker/ (Spring Boot with Docker)
# https://www.baeldung.com/executable-jar-with-maven (How to Create an Executable JAR with Maven)

FROM openjdk:17-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /app
ARG JAR_FILE=target/*-spring-boot.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]