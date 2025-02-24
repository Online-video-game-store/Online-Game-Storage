# Dockerfile
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
VOLUME /tmp
COPY target/eureka-server-*.jar eureka-server.jar
ENTRYPOINT ["java", "-jar", "eureka-server.jar"]
EXPOSE 7777
