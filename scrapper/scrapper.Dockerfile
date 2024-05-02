FROM openjdk:21-slim
COPY target/scrapper.jar scrapper.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/scrapper.jar"]
