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

We are making a libGDX backend to run any libgdx app (whether written in Java or another JVM language) on the web using WebAssembly or JavaScript.  
Bytecoder transpiles byte code to wasm32 or js. 

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
#### Which backend to use
The sample app supports js and wasm.

This can be set in the properties of the `gdx-backend-bytecoder-example` `pom`:

    <bytecoder-mavenplugin.backend>
    
with 1 of these properties: `js` or `wasm_llvm`, default on `js`.

#### Install LLVM for Bytecoder with wasm_llvm backend

##### Install LLVM on Windows

Windows 10 Home Edition is currently unsupported.

Windows 10 Pro users: download  Ubuntu 18.04 LTS from Windows store.

Open the Ubuntu console and follow Linux steps below.

#### Install LLVM on Linux

For Linux distributions using `apt` (including Windows Ubuntu sub system), there is a script provided by LLVM. 

Run it:

    wget https://apt.llvm.org/llvm.sh
    chmod +x llvm.sh
    sudo ./llvm.sh 10

For other distributions please consult the [LLVM download page](https://releases.llvm.org/download.html).

#### Mac

* Download Mac package from https://releases.llvm.org/download.html#10.0.0
* Extract it to a directory (e.g. /opt/clang+llvm-10.0.0-x86_64-apple-darwin)
* link the executables used by Bytecoder to /usr/local/bin. Those are my links:

Commands to create links:

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

Start a webserver in `target/bytecoder`. We assume port 8096 is used. Content type for `.wasm` files must be `application/wasm` and for `.js` files, `text/javascript`.

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
