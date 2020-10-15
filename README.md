# LibGDX Bytecoder backend
Support any libgdx project with any JVM language (Kotlin, Groovy, Scala) on the web!


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

This project is still under development and will actually be used to run an app on the web.
Since the app is written in kotlin and libgdx, GWT was not an option, so after a few weeks of research we found that bytecoder could be an option and here it is :smile:

### Built With

* [Libgdx]
* [Kotlin]
* [Bytecoder]

### Testing JavaScript APIs

We are testing JavaScript apis in the [web-demo](https://github.com/squins/web-demo) project. Currently working on Audio and Webgl, in future also things like keyboard, mouse interaction when we implement them in Libgdx.

Having a dedicated project to test those apis, makes the Bytecoder backend implementation just dedicated to porting the JS calls to Kotlin.

## Getting Started

To get a local copy up and running follow these simple steps.

### Installation

Clone the repo
```sh
git clone https://github.com/squins/libgdx-wasm-with-bytecoder.git
```
#### Libgdx sample app (gradle)

The sample app contains all libgdx features that have been port. JAR files are provided to Bytecoder maven project
using local Maven repo.

Steps to build:

```sh
cd libgdx-sample-app
./gradlew publishToMavenLocal
```

#### Build Bytecoder Maven project

Install Maven (root path)
```
mvn clean install
```
####

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
