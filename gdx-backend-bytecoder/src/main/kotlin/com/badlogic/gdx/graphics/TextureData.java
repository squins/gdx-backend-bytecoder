package com.badlogic.gdx.graphics;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.graphics.glutils.MipMapGenerator;

public interface TextureData {
    public enum TextureDataType {
        Pixmap, Custom
    }

    /** @return the {@link TextureDataType} */
    public TextureDataType getType ();

    /** @return whether the TextureData is prepared or not. */
    public boolean isPrepared ();

    /** Prepares the TextureData for a call to {@link #consumePixmap()} or {@link #consumeCustomData(int)}. This method can be
     * called from a non OpenGL thread and should thus not interact with OpenGL. */
    public void prepare ();

    /** Returns the {@link Pixmap} for upload by Texture. A call to {@link #prepare()} must precede a call to this method. Any
     * internal data structures created in {@link #prepare()} should be disposed of here.
     *
     * @return the pixmap. */
    public Pixmap consumePixmap ();

    /** @return whether the caller of {@link #consumePixmap()} should dispose the Pixmap returned by {@link #consumePixmap()} */
    public boolean disposePixmap ();

    /** Uploads the pixel data to the OpenGL ES texture. The caller must bind an OpenGL ES texture. A call to {@link #prepare()}
     * must preceed a call to this method. Any internal data structures created in {@link #prepare()} should be disposed of here. */
    public void consumeCustomData (int target);

    /** @return the width of the pixel data */
    public int getWidth ();

    /** @return the height of the pixel data */
    public int getHeight ();

    /** @return the {@link Format} of the pixel data */
    public Format getFormat ();

    /** @return whether to generate mipmaps or not. */
    public boolean useMipMaps ();

    /** @return whether this implementation can cope with a EGL context loss. */
    public boolean isManaged ();

    /** Provides static method to instantiate the right implementation (Pixmap, ETC1, KTX).
     * @author Vincent Bousquet */
    public static class Factory {

        public static TextureData loadFromFile (FileHandle file, boolean useMipMaps) {
            // DISABLED: performance System.out.// DISABLED: performance println("TextureData: loadFromFile and file= " + (file == null) + " useMipMaps = " + useMipMaps);
            return loadFromFile(file, null, useMipMaps);
        }

        public static TextureData loadFromFile (FileHandle file, Format format, boolean useMipMaps) {
            // DISABLED: performance System.out.println("TextureData: loadFromFile and file= " + (file == null) + " useMipMaps = " + useMipMaps+ "format: " + (format != null));
            if (file == null) return null;
            // DISABLED: performance System.out.// DISABLED: performance println("File props: "  + file.name());
            return new FileTextureData(file, new Pixmap(file), format, useMipMaps);
        }

    }
}