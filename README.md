# LibGDX - WebAssembly with Bytecoder

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
git clone https://github.com/squins/libgdx-wasm-width-bytecoder.git
```
#### Libgdx app (gradle)
2. Build gralde on libgdx project
```sh
..\gradlew build
```
3. Publish to Maven local
```sh
..\gradlew publishToMavenLocal
```
#### Bytecoder kotlin app (Maven)
4. Install Maven
```
mvn install
```
####
5. Open index.html from bytecoder folder
```
\target\bytecoder\index.html
```
## Usage

If everything is ok, you should see a red square in the left corner

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
[github]: https://github.com/squins/libgdx-wasm-width-bytecoder
[webgl]: https://developer.mozilla.org/nl/docs/Web/API/WebGL_API
[kotlin]: https://kotlinlang.org/
[squins]: https://www.squins.com/
