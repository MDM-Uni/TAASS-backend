FROM adoptopenjdk/openjdk11
# RUN addgroup -S spring && adduser -S spring -G spring
EXPOSE 8080
ARG DEPENDENCY=target
ADD ${DEPENDENCY}/*.jar negozio.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/negozio.jar"]