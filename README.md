# LibGDX Bytecoder backend
Support any libgdx project with any JVM language (Kotlin, Groovy, Scala) on the web!

We have a working [demo] available, and are working hard to make it work for most libGDX app out of the box!


## Table of Contents

* [About the Project](#about-the-project)
  * [Built With](#built-with)
  * [Testing JavaScript API's](#testing-javascript-apis)
* [Getting Started](#getting-started)
  * [Installation](#installation)
* [Usage](#usage)
* [Contact](#contact)
* [Acknowledgements](#acknowledgements)


## About The Project

We are making a libGDX backend to run any libgdx app (whether written in Java or another JVM language) on the web using WebAssembly.  
Bytecoder transpiles byte code to wasm32. 

The status is that a simple libgdx app now can run on the web! See our [demo].

### Built With

* [Libgdx]
* [Kotlin]
* [Bytecoder]

### Testing JavaScript APIs

We are testing JavaScript apis in the [web-demo] project. Audio and Webgl are now ready, in future things like keyboard, mouse interaction when we implement them in Libgdx.

Having a dedicated project to test those apis, makes the Bytecoder backend implementation just dedicated to porting the JS calls to Kotlin.

## Getting Started

To get a local copy up and running follow these simple steps.

### Installation

Clone the repo
```sh
git clone https://github.com/squins/gdx-backend-bytecoder.git
```
#### Libgdx sample app (gradle)

The sample app contains all libgdx features that have been port. JAR files are provided to Bytecoder maven project
using local Maven repo.

Steps to build:

```sh
cd libgdx-sample-app
./gradlew publishToMavenLocal
```

#### Download LLVM for Bytecoder and wasm_llvm backend
Windows:
* Download Ubuntu 18.04 LTS from Windows store, make sure that you have Windows Pro
* Open Ubuntu terminal and run following steps:
    * ```sudo apt-get update -y```
    * ```sudo apt-get install -y llvm```
    * ```sudo chmod +x .travis/deploy.sh```
    * ```sudo wget https://apt.llvm.org/llvm-snapshot.gpg.key```
    * ```sudo apt-key add llvm-snapshot.gpg.key```
    * ```sudo add-apt-repository -y "deb http://apt.llvm.org/xenial/   llvm-toolchain-xenial-10  main"```
    * ```sudo apt-get -q update```
    * ```sudo apt-get install -y clang-10 lldb-10 lld-10 clangd-10```
    * ```wget https://github.com/gohugoio/hugo/releases/download/v0.59.0/hugo_0.59.0_Linux-64bit.tar.gz```
    * ```tar xzf hugo_0.59.0_Linux-64bit.tar.gz```
    * ```sudo chmod +x hugo```
    * ```git clone https://github.com/matcornic/hugo-theme-learn.git ./manual/themes/hugo-theme-learn```

Mac: 
* Download Mac package from https://releases.llvm.org/download.html#10.0.0
* Extract it to a directory (e.g. /opt/clang+llvm-10.0.0-x86_64-apple-darwin)
* link the executables used by Bytecoder to /usr/local/bin. Those are my links:

```<username>@192 bin % pwd
/usr/local/bin```
<username>@192 bin % ls -sathl | grep apple-darwin
    0 lrwxr-xr-x     1 <username>  admin    54B Nov  2 16:56 wasm-ld-10 -> /opt/clang+llvm-10.0.0-x86_64-apple-darwin/bin/wasm-ld
    0 lrwxr-xr-x     1 root        admin    50B Nov  2 16:35 llc-10 -> /opt/clang+llvm-10.0.0-x86_64-apple-darwin/bin/llc
```

#### Bytecoder project for local snapshot

Clone the repo
```sh
git clone https://github.com/squins/Bytecoder.git
```
Checkout branch libgdx-fixes
```sh
git checkout libgdx-fixes
```
Build project
```sh
mvn clean install -DskipTests
```
#### Libgdx project for local snapshot
Clone the repo
```sh
git clone https://github.com/squins/libgdx.git
```
Checkout branch gdx-parent-1.9.11-debug
```sh
git checkout gdx-parent-1.9.11-debug
```
Build project
```sh
mvn clean install -DskipTests
```

#### Libgdx-wasm-with-bytecoder project (root path)
Build project
```sh
mvn clean install -DskipTests
```
### Run the sample app

Start a webserver in target/bytecoder, n.b. content type for `.wasm` files must be `application/wasm`

Go to http://localhost:<port>/

```
gdx-backend-bytecoder-example\target\bytecoder\target\bytecoder 
```
or open 
```
gdx-backend-bytecoder-example\target\bytecoder\target\bytecoder\index.html
```

## Usage

If everything is ok, you should see a play button, when you press it, you will see a red square and hear some music.

## Contact

Squins info: [Squins]

Project Link: [Github]

Demo link: [Demo]

## Acknowledgements

* [Kotlin]
* [Bytecoder]
* [Webgl]
* [Libgdx]

[libgdx]: https://libgdx.com/
[bytecoder]: https://github.com/mirkosertic/Bytecoder
[github]: https://github.com/squins/gdx-backend-bytecoder
[webgl]: https://developer.mozilla.org/nl/docs/Web/API/WebGL_API
[kotlin]: https://kotlinlang.org/
[squins]: https://www.squins.com/
[web-demo]: https://github.com/squins/web-demo
[demo]: https://squins.github.io/gdx-backend-bytecoder-example/
