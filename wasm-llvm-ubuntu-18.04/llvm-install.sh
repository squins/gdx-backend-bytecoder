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
cmd sudo wget https://apt.llvm.org/llvm-snapshot.gpg.key
cmd sudo apt-key add llvm-snapshot.gpg.key
echo "+ sudo add-apt-repository ..."
sudo add-apt-repository -y "deb http://apt.llvm.org/bionic/   llvm-toolchain-bionic-10  main"
cmd sudo apt-get -q update
cmd sudo apt-get install -y clang-10
cmd sudo apt-get install -y lldb-10
cmd sudo apt-get install -y lld-10
cmd sudo apt-get install -y clangd-10
