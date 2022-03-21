FROM adoptopenjdk/openjdk11
#RUN addgroup --system spring && adduser --system spring --group spring
EXPOSE 8082
ARG DEPENDENCY=target
ADD ${DEPENDENCY}/*.jar negozio.jar
ENTRYPOINT ["java","-jar","/negozio.jar"]