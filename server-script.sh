#!/bin/bash

# Using server script from aroxu; Which is fork from monun's. See his GitHub for more information. (https://github.com/arxou/server-script/)
# Original script is monun's server-script; Which is under MIT License. See his GitHub for more information. (https://github.com/monun/server-script/)

# Author BaeHyeonWoo

download() {
  wget -c --content-disposition -P "$2" -N "$1" 2>&1 | tail -1
}

server=https://github.com/aroxu/server-script/releases/latest/download/server_linux_x64.zip

if type -p bsdtar
then
    echo "Found Bsdtar in PATH"
else
    echo "Bsdtar was not found on this machine. Please install with your Package Manager. Exiting..."
    exit
fi

for i in "${server[@]}"; do
  download_result=$(download "$i" "./.server")
  echo "$download_result <- $i"
done


if [ -d "./.server" ]
then
    cd .server || return
else
    mkdir .server
    cd .server || return
fi

if [ -f "server_linux_x64.zip" ]
then
  bsdtar -xf server_linux_x64.zip -C "./"
  rm -rf ./server_linux_x64.zip
  chmod +x ./server
  ./server
else
  echo "Something went wrong! Try manually download linux version server from: https://github.com/arxou/server-script/releases."
  echo "Exiting..."
  exit
fi

echo "Exiting..."
exit