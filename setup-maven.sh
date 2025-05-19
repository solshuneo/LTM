#!/bin/bash

# Phiên bản Maven muốn tải
MAVEN_VERSION=3.9.9
MAVEN_DIR="maven/apache-maven-$MAVEN_VERSION"
MAVEN_ARCHIVE="apache-maven-$MAVEN_VERSION-bin.tar.gz"
MAVEN_URL="https://dlcdn.apache.org/maven/maven-3/$MAVEN_VERSION/binaries/$MAVEN_ARCHIVE"

# Nếu chưa có folder Maven thì tải và giải nén
if [ ! -d "$MAVEN_DIR" ]; then
  echo "Downloading Maven $MAVEN_VERSION..."
  mkdir -p maven
  wget -q $MAVEN_URL -O $MAVEN_ARCHIVE
  echo "Extracting Maven..."
  tar -xzf $MAVEN_ARCHIVE -C maven
  rm $MAVEN_ARCHIVE
fi

# Set biến môi trường Maven cho session hiện tại
export M2_HOME="$(pwd)/$MAVEN_DIR"
export PATH="$M2_HOME/bin:$PATH"

echo "Maven is set up at $M2_HOME"
echo "You can run 'mvn -version' now."
