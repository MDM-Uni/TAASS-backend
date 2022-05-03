FROM adoptopenjdk/openjdk11
#RUN addgroup --system spring && adduser --system spring --group spring
EXPOSE 8079
ARG DEPENDENCY=target
ADD ${DEPENDENCY}/*.jar api-gateway.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/api-gateway.jar"]