package com.badlogic.gdx.graphics;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.squins.gdx.backends.bytecoder.BytecoderFileHandle;
import com.squins.gdx.backends.bytecoder.api.web.HtmlImageElement;
import com.squins.gdx.backends.bytecoder.preloader.AssetDownloader;
import com.squins.gdx.backends.bytecoder.preloader.AssetLoaderListener;
import de.mirkosertic.bytecoder.api.web.CanvasImageSource;
import de.mirkosertic.bytecoder.api.web.CanvasRenderingContext2D;
import de.mirkosertic.bytecoder.api.web.Window;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

public class Pixmap implements Disposable {

    public static Map<Integer, Pixmap> pixmaps = new HashMap<>();

    static int nextId = 0;

    public enum Format {
        Alpha, Intensity, LuminanceAlpha, RGB565, RGBA4444, RGB888, RGBA8888;

        public static int toGlFormat (Format format) {
            if (format == Alpha) return GL20.GL_ALPHA;
            if (format == Intensity) return GL20.GL_ALPHA;
            if (format == LuminanceAlpha) return GL20.GL_LUMINANCE_ALPHA;
            if (format == RGB565) return GL20.GL_RGB;
            if (format == RGB888) return GL20.GL_RGB;
            if (format == RGBA4444) return GL20.GL_RGBA;
            if (format == RGBA8888) return GL20.GL_RGBA;
            throw new GdxRuntimeException("unknown format: " + format);
        }

        public static int toGlType (Format format) {
            if (format == Alpha) return GL20.GL_UNSIGNED_BYTE;
            if (format == Intensity) return GL20.GL_UNSIGNED_BYTE;
            if (format == LuminanceAlpha) return GL20.GL_UNSIGNED_BYTE;
            if (format == RGB565) return GL20.GL_UNSIGNED_SHORT_5_6_5;
            if (format == RGB888) return GL20.GL_UNSIGNED_BYTE;
            if (format == RGBA4444) return GL20.GL_UNSIGNED_SHORT_4_4_4_4;
            if (format == RGBA8888) return GL20.GL_UNSIGNED_BYTE;
            throw new GdxRuntimeException("unknown format: " + format);
        }
    }

    /** Blending functions to be set with {@link Pixmap#setBlending}.
     * @author mzechner */
    public enum Blending {
        None, SourceOver
    }

//    /** Filters to be used with {@link Pixmap#drawPixmap(Pixmap, int, int, int, int, int, int, int, int)}.
//     *
//     * @author mzechner */
    public enum Filter {
        NearestNeighbour, BiLinear
    }

    int width;
    int height;
    Format format;
    HTMLCanvasElement canvas;
    CanvasRenderingContext2D context;
    int id;
    IntBuffer buffer;
    int r = 255, g = 255, b = 255;
    float a;
    String color = make(r, g, b, a);
    static String clearColor = make(255, 255, 255, 1.0f);
    Blending blending = Blending.SourceOver;
    Filter filter = Filter.BiLinear;
    CanvasPixelArray pixels;
    private final HtmlImageElement htmlImageElement;
//    private VideoElement videoElement;

    public Pixmap(FileHandle file) {
        this(getImageFromPreloaded(file));
        // DISABLED: performance System.out.// DISABLED: performance println("File path: " + file.path() + ", path:" +  file.name());
    }

    private static HtmlImageElement getImageFromPreloaded(FileHandle file) {
        final BytecoderFileHandle bytecoderFileHandle = (BytecoderFileHandle) file;

        final String path = file.path();
        final ObjectMap<String, HtmlImageElement> images = bytecoderFileHandle.preloader.getImages();
        return images.get(path);
    }

    public static void downloadFromUrl(String url, final DownloadPixmapResponseListener responseListener) {
        new AssetDownloader().loadImage(url, null, "anonymous", new AssetLoaderListener<HtmlImageElement>() {
            @Override
            public void onProgress(double amount) {
                // nothing to do
            }

            @Override
            public void onFailure() {
                responseListener.downloadFailed(new Exception("Failed to download image"));
            }

            @Override
            public void onSuccess(HtmlImageElement result) {
                responseListener.downloadComplete(new Pixmap(result));
            }
        });
    }

    public CanvasRenderingContext2D getContext() {
        ensureCanvasExists();
        return context;
    }

    private static HTMLCanvasElement.Composite getComposite () {
        return HTMLCanvasElement.Composite.SOURCE_OVER;
    }

    public Pixmap (HtmlImageElement img) {
        this(-1, -1, img);
    }


    public Pixmap (int width, int height, Format format) {
        this(width, height, (HtmlImageElement)null);
    }

    private Pixmap(int width, int height, HtmlImageElement htmlImageElement) {
        // DISABLED: performance System.out.println("Pixmap constructor for HtmlImageElement, src ");
        // DISABLED: performance System.out.println("getting imag src");
        // DISABLED: performance System.out.println("image.getSrc: " + htmlImageElement.getSrc() + "image.getWidth: " + htmlImageElement.getWidth()
//                + "image.getSrc: " + htmlImageElement.getHeight());
        // DISABLED: performance System.out.println("Got source");
        this.htmlImageElement = htmlImageElement;
        this.width = htmlImageElement != null ? htmlImageElement.getWidth() : width;
        this.height = htmlImageElement != null ? htmlImageElement.getHeight() : height;
        this.format = Format.RGBA8888;

        buffer = BufferUtils.newIntBuffer(1);
        id = nextId++;
        buffer.put(0, id);
        pixmaps.put(id, this);
    }

//    private Pixmap(int width, int height, VideoElement videoElement) {
//        this.videoElement = videoElement;
//        this.width = videoElement != null ? videoElement.getWidth() : width;
//        this.height = videoElement != null ? videoElement.getHeight() : height;
//        this.format = Format.RGBA8888;
//
//        buffer = BufferUtils.newIntBuffer(1);
//        id = nextId++;
//        buffer.put(0, id);
//        pixmaps.put(id, this);
//    }

    private void create () {
        canvas = HTMLCanvasElement.createIfSupported();
        canvas.getCanvasElement().setWidth(width);
        canvas.getCanvasElement().setHeight(height);
        context = canvas.getContext2d();
        context.setGlobalCompositeOperation(getComposite().toString());
    }

    public static String make (int r2, int g2, int b2, float a2) {
        return "rgba(" + r2 + "," + g2 + "," + b2 + "," + a2 + ")";
    }

    public void setBlending (Blending blending) {
        this.blending = blending;
        this.ensureCanvasExists();
        this.context.setGlobalCompositeOperation(getComposite().toString());
    }

    /** @return the currently set {@link Blending} */
    public Blending getBlending () {
        return blending;
    }

    public void setFilter (Filter filter) {
        this.filter = filter;
    }

    /** @return the currently set {@link Filter} */
    public Filter getFilter () {
        return filter;
    }

    public Format getFormat () {
        return format;
    }

    public int getGLInternalFormat () {
        return GL20.GL_RGBA;
    }

    public int getGLFormat () {
        return GL20.GL_RGBA;
    }

    public int getGLType () {
        return GL20.GL_UNSIGNED_BYTE;
    }

    public int getWidth () {
        return width;
    }

    public int getHeight () {
        return height;
    }

    public ByteBuffer getPixels () {
        // DISABLED: performance System.out.// DISABLED: performance println("Pixmap.getPixels in:" + buffer.get(0));
        final ByteBuffer byteBuffer = BufferUtils.newByteBuffer(4);
        byteBuffer.asIntBuffer().put(buffer.array());

        // DISABLED: performance System.out.println("Pixmap.getPixels byteBuffer returning: " +byteBuffer.get(0));

        return byteBuffer;
    }

    @Override
    public void dispose () {
        pixmaps.remove(id);
    }

    public HTMLCanvasElement getCanvasElement () {
        ensureCanvasExists();
        return canvas.getCanvasElement();
    }

    private void ensureCanvasExists () {
        if (canvas == null) {
            create();
            if (htmlImageElement != null) {
                context.setGlobalCompositeOperation(HTMLCanvasElement.Composite.COPY.toString());
                context.drawImage((CanvasImageSource) htmlImageElement, 0, 0);
                context.setGlobalCompositeOperation(getComposite().toString());
            }
//            if (videoElement != null) {
//                context.setGlobalCompositeOperation(Composite.COPY);
//                context.drawImage(videoElement, 0, 0);
//                context.setGlobalCompositeOperation(getComposite());
//            }
        }
    }

    public boolean canUseImageElement () {
        return canvas == null && htmlImageElement != null;
    }

    public HtmlImageElement getImageElement () {
        return htmlImageElement;
    }

//    public boolean canUseVideoElement () {
//        return canvas == null && videoElement != null;
//    }

//    public VideoElement getVideoElement () {
//        return videoElement;
//    }

    public void setColor (int color) {
        ensureCanvasExists();
        r = (color >>> 24) & 0xff;
        g = (color >>> 16) & 0xff;
        b = (color >>> 8) & 0xff;
        a = (color & 0xff) / 255f;
        this.color = make(r, g, b, a);
        context.setFillStyle(this.color);
        context.setStrokeStyle(this.color);
    }

    public void setColor (float r, float g, float b, float a) {
        ensureCanvasExists();
        this.r = (int)(r * 255);
        this.g = (int)(g * 255);
        this.b = (int)(b * 255);
        this.a = a;
        color = make(this.r, this.g, this.b, this.a);
        context.setFillStyle(color);
        context.setStrokeStyle(this.color);
    }

    public void setColor (Color color) {
        setColor(color.r, color.g, color.b, color.a);
    }

    public void fill () {
        ensureCanvasExists();
        context.clearRect(0, 0, getWidth(), getHeight());
        rectangle(0, 0, getWidth(), getHeight(), DrawType.FILL);
    }

// /**
// * Sets the width in pixels of strokes.
// *
// * @param width The stroke width in pixels.
// */
// public void setStrokeWidth (int width);

    public void drawLine (int x, int y, int x2, int y2) {
        line(x, y, x2, y2, DrawType.STROKE);
    }

    public void drawRectangle (int x, int y, int width, int height) {
        rectangle(x, y, width, height, DrawType.STROKE);
    }

    public void drawPixmap (Pixmap pixmap, int x, int y) {
        HTMLCanvasElement image = pixmap.getCanvasElement();
        image(image, 0, 0, image.getWidth(), image.getHeight(), x, y, image.getWidth(), image.getHeight());
    }

    public void drawPixmap (Pixmap pixmap, int x, int y, int srcx, int srcy, int srcWidth, int srcHeight) {
        HTMLCanvasElement image = pixmap.getCanvasElement();
        image(image, srcx, srcy, srcWidth, srcHeight, x, y, srcWidth, srcHeight);
    }

    public void drawPixmap (Pixmap pixmap, int srcx, int srcy, int srcWidth, int srcHeight, int dstx, int dsty, int dstWidth,
                            int dstHeight) {
        image(pixmap.canvas, srcx, srcy, srcWidth, srcHeight, dstx, dsty, dstWidth, dstHeight);
    }

    public void fillRectangle (int x, int y, int width, int height) {
        rectangle(x, y, width, height, DrawType.FILL);
    }

    public void drawCircle (int x, int y, int radius) {
        circle(x, y, radius, DrawType.STROKE);
    }

    public void fillCircle (int x, int y, int radius) {
        circle(x, y, radius, DrawType.FILL);
    }

    public void fillTriangle (int x1, int y1, int x2, int y2, int x3, int y3) {
        triangle(x1, y1, x2, y2, x3, y3, DrawType.FILL);
    }

//    public int getPixel (int x, int y) {
//        ensureCanvasExists();
//        if (pixels == null) pixels = context.getImageData(0, 0, width, height).getData();
//        int i = x * 4 + y * width * 4;
//        int r = pixels.get(i + 0) & 0xff;
//        int g = pixels.get(i + 1) & 0xff;
//        int b = pixels.get(i + 2) & 0xff;
//        int a = pixels.get(i + 3) & 0xff;
//        return (r << 24) | (g << 16) | (b << 8) | (a);
//    }

    public void drawPixel (int x, int y) {
        rectangle(x, y, 1, 1, DrawType.FILL);
    }

    public void drawPixel (int x, int y, int color) {
        setColor(color);
        drawPixel(x, y);
    }

    private void circle (int x, int y, int radius, DrawType drawType) {
        ensureCanvasExists();
        if (blending == Blending.None) {
            context.setFillStyle(clearColor);
            context.setStrokeStyle(clearColor);
            context.setGlobalCompositeOperation("destination-out");
            context.beginPath();
            context.arc(x, y, radius, 0, 2 * Math.PI, false);
            fillOrStrokePath(drawType);
            context.closePath();
            context.setFillStyle(color);
            context.setStrokeStyle(color);
            context.setGlobalCompositeOperation(HTMLCanvasElement.Composite.SOURCE_OVER.toString());
        }
        context.beginPath();
        context.arc(x, y, radius, 0, 2 * Math.PI, false);
        fillOrStrokePath(drawType);
        context.closePath();
        pixels = null;
    }

    private void line(int x, int y, int x2, int y2, DrawType drawType) {
        ensureCanvasExists();
        if (blending == Blending.None) {
            context.setFillStyle(clearColor);
            context.setStrokeStyle(clearColor);
            context.setGlobalCompositeOperation("destination-out");
            context.beginPath();
            context.moveTo(x, y);
            context.lineTo(x2, y2);
            fillOrStrokePath(drawType);
            context.closePath();
            context.setFillStyle(color);
            context.setStrokeStyle(color);
            context.setGlobalCompositeOperation(HTMLCanvasElement.Composite.SOURCE_OVER.toString());
        }
        context.beginPath();
        context.moveTo(x, y);
        context.lineTo(x2, y2);
        fillOrStrokePath(drawType);
        context.closePath();
        pixels = null;
    }

    private void rectangle(int x, int y, int width, int height, DrawType drawType) {
        ensureCanvasExists();
        if (blending == Blending.None) {
            context.setFillStyle(clearColor);
            context.setStrokeStyle(clearColor);
            context.setGlobalCompositeOperation("destination-out");
            context.beginPath();
            context.rect(x, y, width, height);
            fillOrStrokePath(drawType);
            context.closePath();
            context.setFillStyle(color);
            context.setStrokeStyle(color);
            context.setGlobalCompositeOperation(HTMLCanvasElement.Composite.SOURCE_OVER.toString());
        }
        context.beginPath();
        context.rect(x, y, width, height);
        fillOrStrokePath(drawType);
        context.closePath();
        pixels = null;
    }

    private void triangle(int x1, int y1, int x2, int y2, int x3, int y3, DrawType drawType) {
        ensureCanvasExists();
        if (blending == Blending.None) {
            context.setFillStyle(clearColor);
            context.setStrokeStyle(clearColor);
            context.setGlobalCompositeOperation("destination-out");
            context.beginPath();
            context.moveTo(x1,y1);
            context.lineTo(x2,y2);
            context.lineTo(x3,y3);
            context.lineTo(x1,y1);
            fillOrStrokePath(drawType);
            context.closePath();
            context.setFillStyle(color);
            context.setStrokeStyle(color);
            context.setGlobalCompositeOperation(HTMLCanvasElement.Composite.SOURCE_OVER.toString());
        }
        context.beginPath();
        context.moveTo(x1,y1);
        context.lineTo(x2,y2);
        context.lineTo(x3,y3);
        context.lineTo(x1,y1);
        fillOrStrokePath(drawType);
        context.closePath();
        pixels = null;
    }

    private void image (HTMLCanvasElement image, int srcX, int srcY, int srcWidth, int srcHeight, int dstX, int dstY, int dstWidth, int dstHeight) {
        ensureCanvasExists();
        if (blending == Blending.None) {
            context.setFillStyle(clearColor);
            context.setStrokeStyle(clearColor);
            context.setGlobalCompositeOperation("destination-out");
            context.beginPath();
            context.rect(dstX, dstY, dstWidth, dstHeight);
            fillOrStrokePath(DrawType.FILL);
            context.closePath();
            context.setFillStyle(color);
            context.setStrokeStyle(color);
            context.setGlobalCompositeOperation(HTMLCanvasElement.Composite.SOURCE_OVER.toString());
        }
        if(srcWidth != 0 && srcHeight != 0 && dstWidth != 0 && dstHeight != 0) {
            context.drawImage((CanvasImageSource) image, srcX, srcY, srcWidth, srcHeight, dstX, dstY, dstWidth, dstHeight);
        }
        pixels = null;
    }

    private void fillOrStrokePath(DrawType drawType) {
        ensureCanvasExists();
        switch (drawType) {
            case FILL:
                context.fill();
                break;
            case STROKE:
                context.stroke();
                break;
        }
    }

    private enum DrawType {
        FILL, STROKE
    }

    public interface DownloadPixmapResponseListener {
        void downloadComplete(Pixmap pixmap);
        void downloadFailed(Throwable t);
    }

    private static class HTMLCanvasElement {
        int width;
        int height;
        CanvasRenderingContext2D context;


        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        enum Composite {
            COPY,
            DESTINATION_ATOP,
            DESTINATION_IN,
            DESTINATION_OUT,
            DESTINATION_OVER,
            LIGHTER,
            SOURCE_ATOP,
            SOURCE_IN,
            SOURCE_OUT,
            SOURCE_OVER,
            XOR
        }

        public static HTMLCanvasElement createIfSupported() {
            return Window.window().document().createElement("canvas");
        }


        public HTMLCanvasElement getCanvasElement() {
            return Window.window().document().createElement("canvas");
        }

        public CanvasRenderingContext2D getContext2d() {
            return context;
        }
    }

    private static class CanvasPixelArray {
    }
}