FROM openjdk:11
ADD target/search-api-0.0.1-SNAPSHOT.jar search-api.jar
EXPOSE 8200
ENTRYPOINT ["java","-jar","search-api.jar"]