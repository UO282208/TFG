FROM openjdk:17
WORKDIR /app
COPY target/spring-boot-0.0.1-SNAPSHOT.jar /app/tfg-springboot.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/tfg-springboot.jar"]