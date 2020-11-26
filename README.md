# LibGDX Bytecoder backend
Support any libgdx project with any JVM language (Kotlin, Groovy, Scala) on the web!

We have a working [demo] available, and are working hard to make it work for most libGDX apps out of the box!


## Table of Contents

* [About the Project](#about-the-project)
  * [Built With](#built-with)
  * [Testing JavaScript API's](#testing-javascript-apis)
* [Getting Started](#getting-started)
  * [Installation](#installation)
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
##### Windows

Download Ubuntu 18.04 LTS from Windows store, make sure that you have Windows Pro

Follow Ubuntu / Debian steps below.

#### Ubuntu 18.04

Execute the following command:

    \curl -sSL https://raw.githubusercontent.com/squins/gdx-backend-bytecoder/issue-14-simply-wasm-llvm-build/wasm-llvm-ubuntu-18.04/llvm-install.sh | bash

#### Other linux editions

Bytecoder should work with any LLVM-10 version. If you use a different Linux with LLVM-10, please create a PR with 
instructions, and we will add it here.

#### Mac

* Download Mac package from https://releases.llvm.org/download.html#10.0.0
* Extract it to a directory (e.g. /opt/clang+llvm-10.0.0-x86_64-apple-darwin)
* link the executables used by Bytecoder to /usr/local/bin. Those are my links:

```
bash
<username>@192 bin % pwd
/usr/local/bin
<username>@192 bin % ls -sathl | grep apple-darwin
    0 lrwxr-xr-x     1 <username>  admin    54B Nov  2 16:56 wasm-ld-10 -> /opt/clang+llvm-10.0.0-x86_64-apple-darwin/bin/wasm-ld
    0 lrwxr-xr-x     1 root        admin    50B Nov  2 16:35 llc-10 -> /opt/clang+llvm-10.0.0-x86_64-apple-darwin/bin/llc
```

Created with:

```
bash
ln -s /opt/clang+llvm-10.0.0-x86_64-apple-darwin/bin/wasm-ld /usr/local/bin/wasm-ld-10
ln -s /opt/clang+llvm-10.0.0-x86_64-apple-darwin/bin/llc /usr/local/bin/llc-10

```

#### Bytecoder project for local snapshot

We have made changes in Bytecoder, which are currently being merged by the maintainer.

So we need a local snapshot in the Maven repo as of now.

Clone the repo
   
    git clone https://github.com/squins/Bytecoder.git

Checkout branch libgdx-fixes

    git checkout libgdx-fixes

Build project
    
    mvn clean install -DskipTests

#### Libgdx-wasm-with-bytecoder project (root path)
Build project

    mvn clean install -DskipTests

### Run the sample app

Start a webserver in `target/bytecoder`. We assume port 8096 is used. Content type for `.wasm` files must be `application/wasm`.

Go to <http://localhost:8096>

Click the 'Play' button to start the app. When you press it, you will see a red square and hear some music. 
This comes from the libGDX game written in Kotlin.

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
