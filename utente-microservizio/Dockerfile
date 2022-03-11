FROM openjdk:13-alpine
RUN addgroup -S spring && adduser -S spring -G spring
EXPOSE 8080
ARG DEPENDENCY=target
ADD ${DEPENDENCY}/*.jar utente.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/utente.jar"]