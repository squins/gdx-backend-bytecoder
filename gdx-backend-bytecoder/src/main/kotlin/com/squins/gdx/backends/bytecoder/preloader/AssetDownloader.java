/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.squins.gdx.backends.bytecoder.preloader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.xhr.client.XMLHttpRequest;
import com.squins.gdx.backends.bytecoder.BytecoderApplication;
import com.squins.gdx.backends.bytecoder.api.web.HtmlAudioElement;
import com.squins.gdx.backends.bytecoder.api.web.HtmlImageElement;
import com.squins.gdx.backends.bytecoder.api.web.http.TXmlHttpRequest;
import de.mirkosertic.bytecoder.api.web.*;

public class AssetDownloader {

	private final Document document;

	public AssetDownloader() {
		useBrowserCache = true;
		useInlineBase64 = false;

		BytecoderApplication application  = (BytecoderApplication) Gdx.app;
		document = application.getLibgdxAppCanvas().ownerDocument();

	}

	public void setUseBrowserCache (boolean useBrowserCache) {
		this.useBrowserCache = useBrowserCache;
	}

	public boolean isUseBrowserCache () {
		return useBrowserCache;
	}

	public void setUseInlineBase64 (boolean useInlineBase64) {
		this.useInlineBase64 = useInlineBase64;
	}

	public boolean isUseInlineBase64 () {
		return useInlineBase64;
	}

	public interface AssetLoaderListener<T> {

		public void onProgress (double amount);

		public void onFailure ();

		public void onSuccess (T result);

	}

	public void load (String url, AssetFilter.AssetType type, String mimeType, AssetLoaderListener<?> listener) {
		switch (type) {
		case Text:
			loadText(url, (AssetLoaderListener<String>)listener);
			break;
		case Image:
			loadImage(url, mimeType, (AssetLoaderListener<HtmlImageElement>)listener);
			break;
		case Binary:
			loadBinary(url, (AssetLoaderListener<Blob>)listener);
			break;
		case Audio:
			loadAudio(url, (AssetLoaderListener<HtmlAudioElement>)listener);
			break;
		case Directory:
			listener.onSuccess(null);
			break;
		default:
			throw new GdxRuntimeException("Unsupported asset type " + type);
		}
	}

	public void loadText (String url, final AssetLoaderListener<String> listener) {
		Window w = Window.window();
		Console c = Console.console();
		Object[] fetched = new Object[0];
		c.log("Fetching");
		w.fetch(url).then(new Promise.Handler<Response>() {
			@Override
			public void handleObject(Response aValue) {
				c.log("Data received");
				aValue.text().then(new StringPromise.Handler() {
					@Override
					public void handleString(String aValue) {
						c.log("String data is " + aValue);
						fetched[0] = "ok";
					}
				});
			}
		});
		c.log("Fetched");
		int counter = 0;
		while(fetched[0] == null && counter++ < 1000) {
			c.log("Waiting");
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

	public void loadBinary (final String url, final AssetLoaderListener<Blob> listener) {
//		XMLHttpRequest request = XMLHttpRequest.create();
//		request.setOnReadyStateChange(new ReadyStateChangeHandler() {
//			@Override
//			public void onReadyStateChange (XMLHttpRequest xhr) {
//				if (xhr.getReadyState() == XMLHttpRequest.DONE) {
//					if (xhr.getStatus() != 200) {
//						listener.onFailure();
//					} else {
//						Int8Array data = TypedArrays.createInt8Array(xhr.getResponseArrayBuffer());
//						listener.onSuccess(new Blob(data));
//					}
//				}
//			}
//		});
//		setOnProgress(request, listener);
//		request.open("GET", url);
//		request.setResponseType(ResponseType.ArrayBuffer);
//		request.send();
	}

	public void loadAudio (String url, final AssetLoaderListener<HtmlAudioElement> listener) {
		loadBinary(url, new AssetLoaderListener<HtmlAudioElement>() {
			@Override
			public void onProgress (double amount) {
				listener.onProgress(amount);
			}

			@Override
			public void onFailure () {
				listener.onFailure();
			}

			@Override
			public void onSuccess(HtmlAudioElement result) {
				listener.onSuccess(result);
			}
		});
	}

	public void loadImage (final String url, final String mimeType, final AssetLoaderListener<HtmlImageElement> listener) {
		loadImage(url, mimeType, null, listener);
	}

	public void loadAudio (final String url, final String mimeType, final AssetLoaderListener<HtmlAudioElement> listener) {
		loadAudio(url, mimeType, null, listener);
	}
	
	public void loadImage (final String url, final String mimeType, final String crossOrigin, final AssetLoaderListener<HtmlImageElement> listener) {
		if (useBrowserCache || useInlineBase64) {
			loadBinary(url, new AssetLoaderListener<Blob>() {
				@Override
				public void onProgress (double amount) {
					listener.onProgress(amount);
				}

				@Override
				public void onFailure () {
					listener.onFailure();
				}

				@Override
				public void onSuccess (Blob result) {
					final HtmlImageElement image = createImage();
					if (crossOrigin != null) {
						image.crossOrigin("crossOrigin");
					}
					hookImgListener(image, new ImgEventListener() {
						@Override
						public void onEvent (NativeEvent event) {
							if (event.getType().equals("error"))
								listener.onFailure();
							else
								listener.onSuccess(image);
						}
					});
					if (isUseInlineBase64()) {
						image.setSrc("data:" + mimeType + ";base64," + result.toBase64());
					} else {
						image.setSrc(url);
					}
				}

			});
		} else {
			final HtmlImageElement image = createImage();
			if (crossOrigin != null) {
				image.crossOrigin("crossOrigin");
			}
			hookImgListener(image, new ImgEventListener() {
				@Override
				public void onEvent (NativeEvent event) {
					if (event.getType().equals("error"))
						listener.onFailure();
					else
						listener.onSuccess(image);
				}
			});
			image.setSrc(url);
		}
	}

	private static interface ImgEventListener {
		public void onEvent (Event event);
	}

	static native void hookImgListener (HtmlImageElement img, ImgEventListener h) /*-{
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
	}-*/;

	HtmlImageElement createImage ()  {
		return document.createElement("img");
	}
		/*-{
			return new Image();
	}-*/;
;

	private boolean useBrowserCache;

	private boolean useInlineBase64;

}
