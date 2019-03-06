FROM openjdk:8-jdk-alpine
MAINTAINER Marcos Vinicius Meneses "marcos.vinicius@maliexchange.com"
ENV ACTIVE_PROFILE production
VOLUME ["/tmp"]
COPY build/libs/crypfy-moneymanager-1.4.3.jar app.jar
EXPOSE 8083
CMD ["java","-jar","-Dspring.profiles.active=${ACTIVE_PROFILE}","app.jar" ]
