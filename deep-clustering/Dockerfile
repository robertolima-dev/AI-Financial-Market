FROM openjdk:8-jdk-alpine
MAINTAINER Marcos Vinicius Meneses "marcos.vinicius@maliexchange.com"
ENV ACTIVE_PROFILE production
VOLUME ["/tmp"]
COPY build/libs/deep-clustering-1.0.jar app.jar
EXPOSE 9001
CMD ["java","-jar","-Dspring.profiles.active=${ACTIVE_PROFILE}","app.jar" ]
