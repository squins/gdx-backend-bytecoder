
let canvas1 = document.getElementById("canvas1");
canvas1.audio = function(url) {
    return new Audio(url);
}
canvas1.mat4 = function() {
    return glMatrix.mat4;
}
canvas1.assetBaseUrl = function() {
    const href = window.location.href;
    const dir = href.substring(0, href.lastIndexOf('/')) + "/";
    return dir + "assets"
}