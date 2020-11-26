#!/usr/bin/env bash
#
# installs llvm and some other dependencies, required to build bytecoder projects with wasm_llvm backend.
#

# exit on first error
set -e

sudo apt-get update -y
sudo apt-get install -y llvm
#sudo chmod +x .travis/deploy.sh
sudo wget https://apt.llvm.org/llvm-snapshot.gpg.key
sudo apt-key add llvm-snapshot.gpg.key
sudo add-apt-repository -y "deb http://apt.llvm.org/xenial/   llvm-toolchain-xenial-10  main"
sudo apt-get -q update
sudo apt-get install -y clang-10 lldb-10 lld-10 clangd-10
#wget https://github.com/gohugoio/hugo/releases/download/v0.59.0/hugo_0.59.0_Linux-64bit.tar.gz
#tar xzf hugo_0.59.0_Linux-64bit.tar.gz
#sudo chmod +x hugo
#git clone https://github.com/matcornic/hugo-theme-learn.git ./manual/themes/hugo-theme-learn

