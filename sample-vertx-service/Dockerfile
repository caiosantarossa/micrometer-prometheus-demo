FROM openjdk:8-jre-alpine

ADD target/sample-vertx-service.jar /application/app.jar

WORKDIR /application

CMD /usr/bin/java $JAVA_OPTS -jar app.jar