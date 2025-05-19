@echo off
set MAVEN_VERSION=3.9.9
set MAVEN_DIR=maven\apache-maven-%MAVEN_VERSION%
set MAVEN_ARCHIVE=apache-maven-%MAVEN_VERSION%-bin.zip
set MAVEN_URL=https://dlcdn.apache.org/maven/maven-3/%MAVEN_VERSION%/binaries/%MAVEN_ARCHIVE%

REM Nếu thư mục Maven chưa tồn tại thì tải và giải nén
if not exist "%MAVEN_DIR%" (
    echo Downloading Maven %MAVEN_VERSION%...
    mkdir maven
    powershell -Command "Invoke-WebRequest -Uri %MAVEN_URL% -OutFile %MAVEN_ARCHIVE%"
    echo Extracting Maven...
    powershell -Command "Expand-Archive -Path %MAVEN_ARCHIVE% -DestinationPath maven"
    del %MAVEN_ARCHIVE%
)

REM Thiết lập biến môi trường cho phiên làm việc hiện tại
set M2_HOME=%CD%\%MAVEN_DIR%
set PATH=%M2_HOME%\bin;%PATH%

echo Maven is set up at %M2_HOME%
echo You can run "mvn -version" now.
