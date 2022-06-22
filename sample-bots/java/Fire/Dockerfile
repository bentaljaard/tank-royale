FROM registry.redhat.io/ubi8/openjdk-17-runtime:latest

ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en'


# We make four distinct layers so if there are application changes the library layers can be re-used

COPY --chown=185 build/archive/lib/*.jar /deployments/lib/
COPY --chown=185 build/archive/Fire/* /deployments/


EXPOSE 7654
USER 185
# ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
# ENV JAVA_APP_JAR="/deployments/robocode-tankroyale-server-0.13.4.jar"

CMD cd /deployments && java -cp /deployments/lib/* /deployments/Fire.java