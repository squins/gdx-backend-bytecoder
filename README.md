# LibGDX - Kotlin with Bytecoder || WebAssembly

This project can be used to compile a native app to the web, using [bytecoder], which returns a wasm (WebAssebmly) file, in a target build folder.


## Table of Contents

* [About the Project](#about-the-project)
  * [Built With](#built-with)
* [Getting Started](#getting-started)
  * [Installation](#installation)
* [Usage](#usage)
* [Contact](#contact)
* [Acknowledgements](#acknowledgements)


## About The Project

This project is still under development and will actually be used to run an app on the web.
Since the app is written in kotlin and libgdx, GWT was not an option, so after a few weeks of research we found that bytecoder could be an option and here it is :smile:

### Built With

* [Libgdx]
* [Kotlin]
* [Bytecoder]

## Getting Started

To get a local copy up and running follow these simple steps.

### Installation

1. Clone the repo
```sh
git clone https://github.com/squins/libgdx-wasm-with-bytecoder.git
```
#### Libgdx sample app (gradle)
Before
```sh
cd libgdx-sample-app
```
2. Build gradle on libgdx project
```sh
..\gradlew build
```
3. Publish to Maven local
```sh
..\gradlew publishToMavenLocal
```
#### Bytecoder kotlin app (Maven)
4. Install Maven (root path)
```
mvn clean install
```
####
5. Start webserver in target/bytecoder, n.b. content type must be application/wasm
```
gdx-backend-bytecoder-example\target\bytecoder\target\bytecoder 
```
or open 
```
gdx-backend-bytecoder-example\target\bytecoder\target\bytecoder\index.html
```

## Usage

If everything is ok, you should see a play button, when you press it, you will see a yellow square and hear some music

## Contact

Squins info: [squins]

Project Link: [github]

## Acknowledgements

* [kotlin]
* [bytecoder]
* [webgl]
* [libgdx]

[libgdx]: https://libgdx.com/
[bytecoder]: https://github.com/mirkosertic/Bytecoder
[github]: https://github.com/squins/libgdx-wasm-with-bytecoder
[webgl]: https://developer.mozilla.org/nl/docs/Web/API/WebGL_API
[kotlin]: https://kotlinlang.org/
[squins]: https://www.squins.com/
