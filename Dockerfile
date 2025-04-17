FROM openjdk:21-oracle as backend
ARG PORT=5500
EXPOSE ${PORT}
COPY target/MoneyTransferService-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
