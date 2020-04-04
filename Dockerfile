FROM java:8


COPY target/cafe-manager-1.0-SNAPSHOT.jar cafe-manager-1.0-SNAPSHOT.jar


ENTRYPOINT ["java","-jar","cafe-manager-1.0-SNAPSHOT.jar"]
