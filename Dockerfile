FROM openjdk:8u191-jdk-alpine3.9
ADD target/warehouse-1.0.0.jar .
EXPOSE 8089
CMD java -jar warehouse-1.0.0.jar