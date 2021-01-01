FROM java:8-jdk-alpine
MAINTAINER 杨湘绥 <yangxiangsui@sutpc.com>

VOLUME /tmp
ADD algo-server-1.0.jar algo-server.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/algo-server.jar"]
EXPOSE 8187