FROM adoptopenjdk/openjdk11:jdk-11.0.5_10-alpine
ADD . /src
WORKDIR /src
RUN ./mvnw package -DskipTests
EXPOSE 8090
ENTRYPOINT ["java","-jar","target/wekaclassificationalgorithmprovider.jar"]