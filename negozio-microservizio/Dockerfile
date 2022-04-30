FROM adoptopenjdk/openjdk11
#RUN addgroup --system spring && adduser --system spring --group spring
EXPOSE 8080
RUN mkdir img
ADD src/main/resources/img/* img/
ADD src/main/resources/products.csv .
ARG DEPENDENCY=target
ADD ${DEPENDENCY}/*.jar negozio.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/negozio.jar"]