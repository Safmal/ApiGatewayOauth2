FROM openjdk:11

RUN mkdir /opt/apiGateway
ADD /target/apigatewayoauth2-0.0.1-SNAPSHOT.jar /opt/apiGateway/AGO.jar
WORKDIR /opt/apiGateway

CMD java -jar AGO.jar