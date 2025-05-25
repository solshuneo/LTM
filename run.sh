./maven/apache-maven-3.9.9/bin/mvn clean package
cp target/lovely-project.war tomcat/apache-tomcat-9.0.105/webapps/
./tomcat/apache-tomcat-9.0.105/bin/shutdown.sh
./tomcat/apache-tomcat-9.0.105/bin/startup.sh