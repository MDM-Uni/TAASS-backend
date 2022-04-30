FROM adoptopenjdk/openjdk11
#RUN addgroup --system spring && adduser --system spring --group spring
EXPOSE 8080
ARG DEPENDENCY=target
ADD ${DEPENDENCY}/*.jar ospedale.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/ospedale.jar"]