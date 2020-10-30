/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squins.gdx.backends.bytecoder.preloader

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.GdxRuntimeException
import com.squins.gdx.backends.bytecoder.BytecoderApplication
import com.squins.gdx.backends.bytecoder.api.web.HtmlAudioElement
import com.squins.gdx.backends.bytecoder.api.web.HtmlImageElement
import de.mirkosertic.bytecoder.api.web.*

@Suppress("UNCHECKED_CAST")
class AssetDownloader {
    private val document: Document

    interface AssetLoaderListener<T> {
        fun onProgress(amount: Double)
        fun onFailure()
        fun onSuccess(result: T)
    }

    fun load(url: String, type: AssetFilter.AssetType, mimeType: String, listener: AssetLoaderListener<*>) {
        println("Called AssetDownloader.load($url) type is ${type.code} mimetype is $mimeType listener is ${listener.toString()}")
        println(type == AssetFilter.AssetType.Image)
        when (type.code) {
            AssetFilter.AssetType.Text.code -> {
                loadText(url, listener as AssetLoaderListener<String>)
            }
            AssetFilter.AssetType.Image.code -> {
                loadImage(url, mimeType, listener as AssetLoaderListener<HtmlImageElement>)
            }
            AssetFilter.AssetType.Binary.code -> {
                loadBinary(url, listener as AssetLoaderListener<Blob>)
            }
            AssetFilter.AssetType.Audio.code -> {
                loadAudio(url, listener as AssetLoaderListener<HtmlAudioElement>)
            }
//            AssetFilter.AssetType.Directory -> {
//                listener.onSuccess(null)
//            }
            else -> throw GdxRuntimeException("Unsupported asset type $type")
        }
//        when (type) {
//            AssetFilter.AssetType.Text -> loadText(url, listener as AssetLoaderListener<String>)
//            AssetFilter.AssetType.Image -> loadImage(url, mimeType, listener as AssetLoaderListener<HtmlImageElement>)
//            AssetFilter.AssetType.Binary -> loadBinary(url, listener as AssetLoaderListener<Blob>)
//            AssetFilter.AssetType.Audio -> loadAudio(url, listener as AssetLoaderListener<HtmlAudioElement>)
////            AssetFilter.AssetType.Directory ->
//            else -> throw GdxRuntimeException("Unsupported asset type $type")
//        }
    }

    private fun loadBinary(url: String, listener: AssetLoaderListener<Blob>) {}

    fun loadText(url: String, listener: AssetLoaderListener<String>) {
        val w = Window.window()
        val c = Console.console()
        val fetched = arrayOfNulls<Any>(0)
        c.log("Fetching")
        w.fetch(url).then(object : Promise.Handler<Response> {
            override fun handleObject(aValue: Response) {
                c.log("Data received")
                aValue.text().then { aValue ->
                    c.log("String data is $aValue")
                    fetched[0] = "ok"
                }
            }
        })
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

    fun loadAudio(url: String, listener: AssetLoaderListener<HtmlAudioElement>) {
        val audio = createAudio()
        audio.addEventListener<Event>("load", object : EventListener<Event> {
            override fun run(aEvent: Event) {
                listener.onSuccess(audio)
            }
        })

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

    private fun loadImage(url: String, mimeType: String, listener: AssetLoaderListener<HtmlImageElement>) {
        println("loadImage")
        loadImage(url, mimeType, null, listener)
    }

    fun loadImage(url: String, mimeType: String, crossOrigin: String?, listener: AssetLoaderListener<HtmlImageElement>) {
        println("loadImage with props: $url mimeType $mimeType crossOrigin $crossOrigin")
        val image = createImage()
        if (crossOrigin != null) {
            image.setCrossOrigin("crossOrigin")
        }
        image.addEventListener<Event>("load", object : EventListener<Event> {
            override fun run(aEvent: Event) {
                listener.onSuccess(image)
            }
        })
        if (isUseInlineBase64) {
            //fix toBase64() if necessary
            Gdx.app.log("AssetDownloader", "UseInlineBase64 not supported")
        } else {
            println("image.setSrc: $url");
            image.setSrc(url)
        }
        //		if (crossOrigin != null) {
//			image.crossOrigin("crossOrigin");
//		}
//		image.addEventListener("load", new EventListener<Event>() {
//			@Override
//			public void run(Event aEvent) {
//				listener.onSuccess(image);
//			}
//		});
//		image.src(url);
//		}


//		if (useBrowserCache || useInlineBase64) {
//			loadBinary(url, new AssetLoaderListener<Blob>() {
//				@Override
//				public void onProgress (double amount) {
//					listener.onProgress(amount);
//				}
//
//				@Override
//				public void onFailure () {
//					listener.onFailure();
//				}
//
//				@Override
//				public void onSuccess (Blob result) {
//					final HtmlImageElement image = createImage();
//					if (crossOrigin != null) {
//						image.crossOrigin("crossOrigin");
//					}
//					hookImgListener(image, new ImgEventListener() {
//						@Override
//						public void onEvent (NativeEvent event) {
//							if (event.getType().equals("error"))
//								listener.onFailure();
//							else
//								listener.onSuccess(image);
//						}
//					});
//					if (isUseInlineBase64()) {
//						image.src("data:" + mimeType + ";base64," + result.toBase64());
//					} else {
//						image.src(url);
//					}
//				}
//
//			});
//		} else {
//			final HtmlImageElement image = createImage();
//			if (crossOrigin != null) {
//				image.crossOrigin("crossOrigin");
//			}
//			hookImgListener(image, new ImgEventListener() {
//				@Override
//				public void onEvent (NativeEvent event) {
//					if (event.getType().equals("error"))
//						listener.onFailure();
//					else
//						listener.onSuccess(image);
//				}
//			});
//			image.setSrc(url);
//		}
    }

    interface ImgEventListener {
        fun onEvent(event: Event)
    }

    private fun createImage(): HtmlImageElement {
        return document.createElement("img")
    }

    /*-{
			return new Image();
	}-*/
    private fun createAudio(): HtmlAudioElement {
        return document.createElement("AUDIO")
    }

    /*-{
			return new Audio();
	}-*/
    var isUseBrowserCache = true
    val isUseInlineBase64 = false

    companion object {
        external fun hookImgListener(img: HtmlImageElement, h: ImgEventListener) /*-{
		img
				.addEventListener(
						'load',
						function(e) {
							h.@com.badlogic.gdx.backends.gwt.preloader.AssetDownloader.ImgEventListener::onEvent(Lcom/google/gwt/dom/client/NativeEvent;)(e);
						}, false);
		img
				.addEventListener(
						'error',
						function(e) {
							h.@com.badlogic.gdx.backends.gwt.preloader.AssetDownloader.ImgEventListener::onEvent(Lcom/google/gwt/dom/client/NativeEvent;)(e);
						}, false);
	}-*/
    }

    init {
        val application = Gdx.app as BytecoderApplication
        document = application.libgdxAppCanvas.ownerDocument()
    }
}