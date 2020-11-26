#!/usr/bin/env bash
#
# installs llvm and some other dependencies, required to build bytecoder projects with wasm_llvm backend.
#

# exit on first error
set -e

fatal () { echo "fatal: $*"; exit 1; }
logged () { echo "+ $*"; eval "$*"; }
cmd () { logged "$*" || fatal "command failed: $*"; }

cmd sudo apt-get update -y
cmd sudo apt-get install -y llvm
#sudo chmod +x .travis/deploy.sh
cmd sudo wget https://apt.llvm.org/llvm-snapshot.gpg.key
cmd sudo apt-key add llvm-snapshot.gpg.key
echo "+ sudo add-apt-repository ..."
sudo add-apt-repository -y "deb http://apt.llvm.org/bionic/   llvm-toolchain-bionic-10  main"
cmd sudo apt-get -q update
cmd sudo apt-get install -y clang-10
cmd sudo apt-get install -y lldb-10
cmd sudo apt-get install -y lld-10
cmd sudo apt-get install -y clangd-10

#wget https://github.com/gohugoio/hugo/releases/download/v0.59.0/hugo_0.59.0_Linux-64bit.tar.gz
#tar xzf hugo_0.59.0_Linux-64bit.tar.gz
#sudo chmod +x hugo
#git clone https://github.com/matcornic/hugo-theme-learn.git ./manual/themes/hugo-theme-learn

