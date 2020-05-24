########################################
# STAGE ONE: BUILD MAVEN PROJECT IMAGE #
########################################
FROM maven:3.6.3-jdk-11 as maven-builder
WORKDIR /usr/src/app
COPY . .
RUN mvn package

#################################
# STAGE TWO: BUILD Tomcat IMAGE #
#################################
FROM tomcat:10.0.0-jdk11
COPY --from=maven-builder /usr/src/app/target/Loghme-Server.war $CATALINA_HOME/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]