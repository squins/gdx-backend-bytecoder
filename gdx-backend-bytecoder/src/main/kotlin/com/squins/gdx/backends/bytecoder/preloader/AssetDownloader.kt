package com.squins.gdx.backends.bytecoder.preloader

import com.badlogic.gdx.utils.GdxRuntimeException
import com.squins.gdx.backends.bytecoder.api.web.HTMLAudioElement
import com.squins.gdx.backends.bytecoder.api.web.HTMLImageElement
import de.mirkosertic.bytecoder.api.web.*

@Suppress("UNCHECKED_CAST")
class AssetDownloader {
    private val document = Window.window().document()

    fun load(url: String, type: AssetType, mimeType: String, listener: AssetLoaderListener<*>) {
        // DISABLED: performance println("Called AssetDownloader.load($url) type is ${type.code} mimetype is $mimeType listener is $listener")
        // DISABLED: performance println("(type == AssetFilter.AssetType.Image)
        when (type.code) {
            AssetType.Text.code -> {
                loadText(url, listener as AssetLoaderListener<String>)
            }
            AssetType.Image.code -> {
                loadImage(url, mimeType, listener as AssetLoaderListener<HTMLImageElement>)
            }
            AssetType.Binary.code -> {
                loadBinary(url, listener as AssetLoaderListener<Blob>)
            }
            AssetType.Audio.code -> {
                loadAudio(url, listener as AssetLoaderListener<HTMLAudioElement>)
            }
//            AssetType.Directory.code -> {
//                listener.onSuccess(null)
//            }
            else -> throw GdxRuntimeException("Unsupported asset type $type")
        }

    }

    private fun loadBinary(url: String, listener: AssetLoaderListener<Blob>) {}

    fun loadText(url: String, listener: AssetLoaderListener<String>) {
        val w = Window.window()
        val c = Console.console()
        val fetched = arrayOfNulls<Any>(0)
        w.fetch(url).then { response ->
            c.log("Data received")
            response.text().then { responseText ->
                fetched[0] = "ok"
                listener.onSuccess(responseText)
            }
        }
        c.log("Fetched")
        var counter = 0
        while (fetched[0] == null && counter++ < 1000) {
            c.log("Waiting")
        }
        //		throw new RuntimeException("NOT YET DONE");
//		TXmlHttpRequest request = TXmlHttpRequest.Companion.create();
//		request.setOnReadyStateChange(new ReadyStateChangeHandler() {
//			@Override
//			public void onReadyStateChange (TXmlHttpRequest xhr) {
//				if (xhr.getReadyState() == TXmlHttpRequest.DONE) {
//					if (xhr.getStatus() != 200) {
//						listener.onFailure();
//					} else {
//						listener.onSuccess(xhr.getResponseText());
//					}
//				}
//			}
//		});
//		setOnProgress(request, listener);
//		request.open("GET", url);
//		request.setRequestHeader("Content-Type", "text/plain; charset=utf-8");
//		request.send();
    }

    private fun loadAudio(url: String, listener: AssetLoaderListener<HTMLAudioElement>) {
        val audio = createAudio()
        audio.addEventListener<Event>("canplaythrough") { listener.onSuccess(audio) }
        audio.setSrc(url)

//		loadBinary(url, new AssetLoaderListener<HtmlAudioElement>() {
//			@Override
//			public void onProgress (double amount) {
//				listener.onProgress(amount);
//			}
//
//			@Override
//			public void onFailure () {
//				listener.onFailure();
//			}
//
//			@Override
//			public void onSuccess(HtmlAudioElement result) {
//				listener.onSuccess(result);
//			}
//		});
    }

    private fun loadImage(url: String, mimeType: String, listener: AssetLoaderListener<HTMLImageElement>) {
        // DISABLED: performance println("("loadImage")
        loadImage(url, mimeType, null, listener)
    }

    fun loadImage(url: String, mimeType: String, crossOrigin: String?, listener: AssetLoaderListener<HTMLImageElement>) {
        // DISABLED: performance println("("loadImage with props: $url mimeType $mimeType crossOrigin $crossOrigin")
        val image = createImage()
        if (crossOrigin != null) {
            image.setCrossOrigin("crossOrigin")
        }
        image.addEventListener("load", object : EventListener<Event> {
            override fun run(aEvent: Event) {
                listener.onSuccess(image)
            }
        })
        if (isUseInlineBase64) {
            //fix toBase64() if necessary
            // DISABLED: performance println("("UseInlineBase64 not supported")
        } else {
            // DISABLED: performance println("("image.setSrc: $url");
            image.setSrc(url)
        }
    }

    interface ImgEventListener {
        fun onEvent(event: Event)
    }

    private fun createImage(): HTMLImageElement {
        return document.createElement("img")
    }

    private fun createAudio(): HTMLAudioElement {
        return document.createElement("audio")
    }

    var isUseBrowserCache = true
    private val isUseInlineBase64 = false
}