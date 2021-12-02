FROM openjdk:8u312-oracle
VOLUME /tmp
COPY apiEncryptionKey.jks apiEncryptionKey.jks
COPY target/config-service-1.0.jar ConfigServer.jar
ENTRYPOINT ["java", "-jar","ConfigServer.jar"]