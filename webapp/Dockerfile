FROM openjdk:17-alpine
ARG JAR_FILE=target/webapp.jar
COPY ${JAR_FILE} webapp.jar
ENTRYPOINT ["java","-jar","/webapp.jar"]