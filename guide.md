# Guide
### How to install JDK 17.0.15
```
sudo apt install openjdk-17-jdk -y
```
you can check later by:
```
java -version
```
17.0.15
### How to install tomcat 9.0.105 
- in Linux:
    ```
    chmod +x setup-tomcat.sh
    ./setup-tomcat.sh 
    ```
- in Window:
    ```
    setup-tomcat.bat
    ```
### How to run tomcat
```
./tomcat/apache-tomcat-9.0.105/bin/startup.sh 
```

### How in install maven
- in linux
  ```
  chmod +x setup-maven.sh
  ./setup-maven.sh
  ```
- in Window
  ```
  setup-maven.bat
  ```
### How to run
```
./maven/apache-maven-3.9.9/bin/mvn clean package
cp target/lovely-project.war tomcat/apache-tomcat-9.0.105/webapps/
./tomcat/apache-tomcat-9.0.105/bin/shutdown.sh
./tomcat/apache-tomcat-9.0.105/bin/startup.sh
```