FROM openjdk:17-oracle

WORKDIR /vulnerableapp
COPY ./target/vulnerable-application.jar /vulnerableapp

EXPOSE 8080

ENTRYPOINT ["java","-jar","vulnerable-application.jar"]
