package com.badlogic.gdx.files;

import java.io.InputStream;
import java.io.OutputStream;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.squins.gdx.backends.bytecoder.BytecoderApplication;
import com.squins.gdx.backends.bytecoder.files.BytecoderFileHandle;


public abstract class FileHandleStream extends BytecoderFileHandle {
    public FileHandleStream (String path) {
        super(((BytecoderApplication)Gdx.app).getPreloader(), path, Files.FileType.Internal);
        // DISABLED: performance System.out.// DISABLED: performance println("FileHandleStream path: " + path);
    }

    public boolean isDirectory () {
        return false;
    }

    public long length () {
        return 0;
    }

    public boolean exists () {
        return true;
    }

    public FileHandle child (String name) {
        throw new UnsupportedOperationException();
    }

    public FileHandle parent () {
        throw new UnsupportedOperationException();
    }

    public InputStream read () {
        throw new UnsupportedOperationException();
    }

    public OutputStream write (boolean overwrite) {
        throw new UnsupportedOperationException();
    }

    public FileHandle[] list () {
        throw new UnsupportedOperationException();
    }

    public void mkdirs () {
        throw new UnsupportedOperationException();
    }

    public boolean delete () {
        throw new UnsupportedOperationException();
    }

    public boolean deleteDirectory () {
        throw new UnsupportedOperationException();
    }

    public void copyTo (FileHandle dest) {
        throw new UnsupportedOperationException();
    }

    public void moveTo (FileHandle dest) {
        throw new UnsupportedOperationException();
    }
}
