FROM openjdk:8u312-oracle
VOLUME /tmp
COPY target/user-service-1.0.jar UserService.jar
ENTRYPOINT ["java", "-jar","UserService.jar"]