#!/bin/bash

TOMCAT_VERSION=9.0.105
TOMCAT_DIR="tomcat"
TOMCAT_ARCHIVE="apache-tomcat-${TOMCAT_VERSION}.tar.gz"
TOMCAT_URL="https://downloads.apache.org/tomcat/tomcat-9/v${TOMCAT_VERSION}/bin/${TOMCAT_ARCHIVE}"

# Tạo folder nếu chưa có
mkdir -p "$TOMCAT_DIR"

# Kiểm tra đã tải chưa
if [ ! -f "$TOMCAT_DIR/bin/startup.sh" ]; then
  echo "➡️  Tải Apache Tomcat $TOMCAT_VERSION ..."
  curl -L "$TOMCAT_URL" -o "$TOMCAT_ARCHIVE"

  echo "📦  Giải nén..."
  tar -xzf "$TOMCAT_ARCHIVE"
  mv "apache-tomcat-${TOMCAT_VERSION}" "$TOMCAT_DIR"

  echo "🧹  Xoá file nén..."
  rm "$TOMCAT_ARCHIVE"

  echo "✅ Đã cài đặt Tomcat vào ./$TOMCAT_DIR/"
else
  echo "✅ Tomcat đã tồn tại ở $TOMCAT_DIR/"
fi
