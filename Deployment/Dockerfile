FROM tomcat:9.0.58-jdk17-openjdk-slim
#FROM tomcat:8-jdk17-openjdk-slim

LABEL author="Developer Operations" email="devops@thesunflowerlab.com"

WORKDIR /usr/local/tomcat/

RUN apt-get update && \
    apt-get install -y --no-install-recommends wget fonts-dejavu fonts-dejavu-core openssl && \
    rm -rf /var/lib/apt/lists/* && \
    rm -rf webapps/ROOT

COPY ./*.war webapps/ROOT.war
COPY ./setenv.sh bin/
COPY trust-certs/* /usr/local/share/ca-certificates/

RUN update-ca-certificates && \
    keytool -storepasswd -new CBhVg5uZsgBG -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit && \
    ls -1 /usr/local/share/ca-certificates | while read cert; do \
      openssl x509 -outform der -in /usr/local/share/ca-certificates/$cert -out $cert.der; \
      keytool -import -alias $cert -keystore $JAVA_HOME/lib/security/cacerts -trustcacerts -file $cert.der -storepass CBhVg5uZsgBG -noprompt; \
      rm $cert.der; \
    done

CMD ["catalina.sh", "run"]
