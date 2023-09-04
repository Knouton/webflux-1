FROM alpine:3.13

RUN apk add openjdk11
COPY ./target/webflux-0.0.1-SNAPSHOT.jar /webflux-0.0.1-SNAPSHOT.jar

CMD ["java","-jar","webflux-0.0.1-SNAPSHOT.jar"]