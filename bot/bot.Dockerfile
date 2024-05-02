FROM openjdk:21-slim
COPY target/bot.jar bot.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "/bot.jar"]
