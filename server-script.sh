#!/bin/bash

# Using server script from aroxu; Which is fork from monun's. See his GitHub for more information. (https://github.com/arxou/server-script/)
# Original script is monun's server-script; Which is under MIT License. See his GitHub for more information. (https://github.com/monun/server-script/)

# Author BaeHyeonWoo

if type -p java; then
    echo "Found Java executable in PATH"
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    echo "Found Java executable in JAVA_HOME."     
    _java="$JAVA_HOME/bin/java"
else
    echo "Java was not found on this machine. Exiting."
    exit
fi

if [[ "$_java" ]]; then
    version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    echo version "$version"
    if [[ "$version" < "16" ]]; then
        echo "Your Java version is less than 16. Cannot run Minecraft Server with java verseion less than 16. Exiting."
        exit
    fi
fi

download() {
  wget -c --content-disposition -P "$2" -N "$1" 2>&1 | tail -1
}

server=https://github.com/aroxu/server-script/releases/latest/download/server_linux_x64.zip

for i in "${server[@]}"; do
  download_result=$(download "$i" "./.server")
  echo "$download_result <- $i"
done

cd ./.server
unzip server_linux_x64.zip
rm -rf ./server_linux_x64.zip
chmod +x ./server
./server