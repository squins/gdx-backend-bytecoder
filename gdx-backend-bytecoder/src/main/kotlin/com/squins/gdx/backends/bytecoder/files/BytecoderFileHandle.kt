package com.squins.gdx.backends.bytecoder.files

import com.badlogic.gdx.Files.FileType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.GdxRuntimeException
import com.squins.gdx.backends.bytecoder.BytecoderApplication
import com.squins.gdx.backends.bytecoder.preloader.Preloader
import java.io.*
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

open class BytecoderFileHandle : FileHandle {
    var preloader: Preloader
    private var bytecoderFile: String

    constructor(preloader: Preloader, fileName: String, type: FileType) {
        println("Constructur BytecoderFileHandle, fileName ${fileName}, props: ${fileName.length} + ${fileName.decapitalize()} and type: ${type.name}")
        if (type != FileType.Internal && type != FileType.Classpath) throw GdxRuntimeException("FileType '$type' Not supported in Bytecoder backend")
        this.preloader = preloader
        this.bytecoderFile = fixSlashes(fileName)
        this.type = type
    }

    constructor(path: String) {
        this.type = FileType.Internal
        preloader = (Gdx.app as BytecoderApplication).preloader
        this.bytecoderFile = fixSlashes(path)
    }

    /** @return The full url to an asset, e.g. http://localhost:8080/assets/data/shotgun-e5f56587d6f025bff049632853ae4ff9.ogg
     */
//    val assetUrl: String
//        get() = preloader.baseUrl + preloader.assetNames[bytecoderFile, bytecoderFile]

    override fun path(): String {
        return bytecoderFile
    }

    override fun name(): String {
        val index: Int = bytecoderFile.lastIndexOf('/')
        return if (index < 0) bytecoderFile else bytecoderFile.substring(index + 1)
    }

    override fun extension(): String {
        val name = name()
        val dotIndex = name.lastIndexOf('.')
        return if (dotIndex == -1) "" else name.substring(dotIndex + 1)
    }

    override fun nameWithoutExtension(): String {
        val name = name()
        val dotIndex = name.lastIndexOf('.')
        return if (dotIndex == -1) name else name.substring(0, dotIndex)
    }

    /** @return the path and filename without the extension, e.g. dir/dir2/bytecoderFile.png -> dir/dir2/bytecoderFile
     */
    override fun pathWithoutExtension(): String {
        val path: String = bytecoderFile
        val dotIndex = path.lastIndexOf('.')
        return if (dotIndex == -1) path else path.substring(0, dotIndex)
    }

    override fun type(): FileType {
        return type
    }

    /** Returns a java.io.File that represents this file handle. Note the returned file will only be usable for
     * [FileType.Absolute] and [FileType.External] file handles.  */
    fun file(): File {
        throw GdxRuntimeException("file() not supported in Bytecoder backend")
    }

    /** Returns a stream for reading this file as bytes.
     * @throw GdxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    override fun read(): InputStream {
        println("file to read: $bytecoderFile")
        val inputStream = preloader.read(bytecoderFile)
        if(inputStream == null){
            throw GdxRuntimeException(bytecoderFile + " does not exist")
        } else
            return inputStream
    }

    /** Returns a buffered stream for reading this file as bytes.
     * @throw GdxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    override fun read(bufferSize: Int): BufferedInputStream {
        return BufferedInputStream(read(), bufferSize)
    }

    /** Returns a reader for reading this file as characters.
     * @throw GdxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    override fun reader(): Reader {
        return InputStreamReader(read())
    }

    /** Returns a reader for reading this file as characters.
     * @throw GdxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    override fun reader(charset: String): Reader {
        return try {
            InputStreamReader(read(), charset)
        } catch (e: UnsupportedEncodingException) {
            throw GdxRuntimeException("Encoding '$charset' not supported", e)
        }
    }

    /** Returns a buffered reader for reading this file as characters.
     * @throw GdxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    override fun reader(bufferSize: Int): BufferedReader {
        return BufferedReader(reader(), bufferSize)
    }

    /** Returns a buffered reader for reading this file as characters.
     * @throw GdxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    override fun reader(bufferSize: Int, charset: String): BufferedReader {
        return BufferedReader(reader(charset), bufferSize)
    }

    /** Reads the entire file into a string using the platform's default charset.
     * @throw GdxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    override fun readString(): String {
        return readString(Charsets.UTF_8.displayName())
    }

    /** Reads the entire file into a string using the specified charset.
     * @throw GdxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    override fun readString(charset: String): String {
        return if (preloader.isText(bytecoderFile)) preloader.texts.get(bytecoderFile) else try {
            String(readBytes(), Charsets.UTF_8)
        } catch (e: UnsupportedEncodingException) {
            throw IllegalStateException(e)
        }
    }

    /** Reads the entire file into a byte array.
     * @throw GdxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    override fun readBytes(): ByteArray {
        var length = length().toInt()
        if (length == 0) length = 512
        var buffer = ByteArray(length)
        var position = 0
        val input = read()
        try {
            while (true) {
                val count = input.read(buffer, position, buffer.size - position)
                if (count == -1) break
                position += count
                if (position == buffer.size) {
                    // Grow buffer.
                    val newBuffer = ByteArray(buffer.size * 2)
                    System.arraycopy(buffer, 0, newBuffer, 0, position)
                    buffer = newBuffer
                }
            }
        } catch (ex: IOException) {
            throw GdxRuntimeException("Error reading file: $this", ex)
        } finally {
            try {
                input.close()
            } catch (ignored: IOException) {
            }
        }
        if (position < buffer.size) {
            // Shrink buffer.
            val newBuffer = ByteArray(position)
            System.arraycopy(buffer, 0, newBuffer, 0, position)
            buffer = newBuffer
        }
        return buffer
    }

    /** Reads the entire file into the byte array. The byte array must be big enough to hold the file's data.
     * @param bytes the array to load the file into
     * @param offset the offset to start writing bytes
     * @param size the number of bytes to read, see [.length]
     * @return the number of read bytes
     */
    override fun readBytes(bytes: ByteArray, offset: Int, size: Int): Int {
        val input = read()
        var position = 0
        try {
            while (true) {
                val count = input.read(bytes, offset + position, size - position)
                if (count <= 0) break
                position += count
            }
        } catch (ex: IOException) {
            throw GdxRuntimeException("Error reading file: $this", ex)
        } finally {
            try {
                input.close()
            } catch (ignored: IOException) {
            }
        }
        return position - offset
    }

    override fun map(): ByteBuffer {
        throw GdxRuntimeException("Cannot map files in Bytecoder backend")
    }

    override fun map(mode: FileChannel.MapMode): ByteBuffer {
        throw GdxRuntimeException("Cannot map files in Bytecoder backend")
    }

    /** Returns a stream for writing to this file. Parent directories will be created if necessary.
     * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
     * @throw GdxRuntimeException if this file handle represents a directory, if it is a [FileType.Classpath] or
     * [FileType.Internal] file, or if it could not be written.
     */
    override fun write(append: Boolean): OutputStream {
        throw GdxRuntimeException("Cannot write to files in Bytecoder backend")
    }

    /** Reads the remaining bytes from the specified stream and writes them to this file. The stream is closed. Parent directories
     * will be created if necessary.
     * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
     * @throw GdxRuntimeException if this file handle represents a directory, if it is a [FileType.Classpath] or
     * [FileType.Internal] file, or if it could not be written.
     */
    override fun write(input: InputStream, append: Boolean) {
        throw GdxRuntimeException("Cannot write to files in Bytecoder backend")
    }

    /** Returns a writer for writing to this file using the default charset. Parent directories will be created if necessary.
     * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
     * @throw GdxRuntimeException if this file handle represents a directory, if it is a [FileType.Classpath] or
     * [FileType.Internal] file, or if it could not be written.
     */
    override fun writer(append: Boolean): Writer {
        return writer(append, "UTF-8")
    }

    /** Returns a writer for writing to this file. Parent directories will be created if necessary.
     * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
     * @param charset May be null to use the default charset.
     * @throw GdxRuntimeException if this file handle represents a directory, if it is a [FileType.Classpath] or
     * [FileType.Internal] file, or if it could not be written.
     */
    override fun writer(append: Boolean, charset: String): Writer {
        throw GdxRuntimeException("Cannot write to files in Bytecoder backend")
    }

    /** Writes the specified string to the file using the default charset. Parent directories will be created if necessary.
     * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
     * @throw GdxRuntimeException if this file handle represents a directory, if it is a [FileType.Classpath] or
     * [FileType.Internal] file, or if it could not be written.
     */
    override fun writeString(string: String, append: Boolean) {
        writeString(string, append, "UTF-8")
    }

    /** Writes the specified string to the file as UTF-8. Parent directories will be created if necessary.
     * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
     * @param charset May be null to use the default charset.
     * @throw GdxRuntimeException if this file handle represents a directory, if it is a [FileType.Classpath] or
     * [FileType.Internal] file, or if it could not be written.
     */
    override fun writeString(string: String, append: Boolean, charset: String) {
        throw GdxRuntimeException("Cannot write to files in Bytecoder backend")
    }

    /** Writes the specified bytes to the file. Parent directories will be created if necessary.
     * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
     * @throw GdxRuntimeException if this file handle represents a directory, if it is a [FileType.Classpath] or
     * [FileType.Internal] file, or if it could not be written.
     */
    override fun writeBytes(bytes: ByteArray, append: Boolean) {
        throw GdxRuntimeException("Cannot write to files in Bytecoder backend")
    }

    /** Writes the specified bytes to the file. Parent directories will be created if necessary.
     * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
     * @throw GdxRuntimeException if this file handle represents a directory, if it is a [FileType.Classpath] or
     * [FileType.Internal] file, or if it could not be written.
     */
    override fun writeBytes(bytes: ByteArray, offset: Int, length: Int, append: Boolean) {
        throw GdxRuntimeException("Cannot write to files in Bytecoder backend")
    }

    /** Returns the paths to the children of this directory. Returns an empty list if this file handle represents a file and not a
     * directory. On the desktop, an [FileType.Internal] handle to a directory on the classpath will return a zero length
     * array.
     * @throw GdxRuntimeException if this file is an [FileType.Classpath] file.
     */
    override fun list(): Array<FileHandle> {
        return preloader.list(bytecoderFile)
    }

    /** Returns the paths to the children of this directory that satisfy the specified filter. Returns an empty list if this file
     * handle represents a file and not a directory. On the desktop, an [FileType.Internal] handle to a directory on the
     * classpath will return a zero length array.
     * @throw GdxRuntimeException if this file is an [FileType.Classpath] file.
     */
    override fun list(filter: FileFilter): Array<FileHandle> {
        return preloader.list(bytecoderFile, filter)
    }

    /** Returns the paths to the children of this directory that satisfy the specified filter. Returns an empty list if this file
     * handle represents a file and not a directory. On the desktop, an [FileType.Internal] handle to a directory on the
     * classpath will return a zero length array.
     * @throw GdxRuntimeException if this file is an [FileType.Classpath] file.
     */
    override fun list(filter: FilenameFilter): Array<FileHandle> {
        return preloader.list(bytecoderFile, filter)
    }

    /** Returns the paths to the children of this directory with the specified suffix. Returns an empty list if this file handle
     * represents a file and not a directory. On the desktop, an [FileType.Internal] handle to a directory on the classpath
     * will return a zero length array.
     * @throw GdxRuntimeException if this file is an [FileType.Classpath] file.
     */
    override fun list(suffix: String): Array<FileHandle> {
        return preloader.list(bytecoderFile, suffix)
    }

    /** Returns true if this file is a directory. Always returns false for classpath files. On Android, an [FileType.Internal]
     * handle to an empty directory will return false. On the desktop, an [FileType.Internal] handle to a directory on the
     * classpath will return false.  */
    override fun isDirectory(): Boolean {
        return preloader.isDirectory(bytecoderFile)
    }

    /** Returns a handle to the child with the specified name.
     * @throw GdxRuntimeException if this file handle is a [FileType.Classpath] or [FileType.Internal] and the child
     * doesn't exist.
     */
    override fun child(name: String): FileHandle {
        return BytecoderFileHandle(preloader, (if (bytecoderFile.isEmpty()) "" else bytecoderFile + if (file.endsWith("/")) "" else "/") + name,
                FileType.Internal)
    }

    override fun parent(): FileHandle {
        val index: Int = bytecoderFile.lastIndexOf("/")
        var dir = ""
        if (index > 0) dir = bytecoderFile.substring(0, index)
        return BytecoderFileHandle(preloader, dir, type)
    }

    override fun sibling(name: String): FileHandle {
        return parent().child(fixSlashes(name))
    }

    /** @throw GdxRuntimeException if this file handle is a [FileType.Classpath] or [FileType.Internal] file.
     */
    override fun mkdirs() {
        throw GdxRuntimeException("Cannot mkdirs with an internal file: $file")
    }

    /** Returns true if the file exists. On Android, a [FileType.Classpath] or [FileType.Internal] handle to a directory
     * will always return false.  */
    override fun exists(): Boolean {
        return preloader.alreadyDownloaded(bytecoderFile)
    }

    /** Deletes this file or empty directory and returns success. Will not delete a directory that has children.
     * @throw GdxRuntimeException if this file handle is a [FileType.Classpath] or [FileType.Internal] file.
     */
    override fun delete(): Boolean {
        throw GdxRuntimeException("Cannot delete an internal file: $file")
    }

    /** Deletes this file or directory and all children, recursively.
     * @throw GdxRuntimeException if this file handle is a [FileType.Classpath] or [FileType.Internal] file.
     */
    override fun deleteDirectory(): Boolean {
        throw GdxRuntimeException("Cannot delete an internal file: $file")
    }

    /** Copies this file or directory to the specified file or directory. If this handle is a file, then 1) if the destination is a
     * file, it is overwritten, or 2) if the destination is a directory, this file is copied into it, or 3) if the destination
     * doesn't exist, [.mkdirs] is called on the destination's parent and this file is copied into it with a new name. If
     * this handle is a directory, then 1) if the destination is a file, GdxRuntimeException is thrown, or 2) if the destination is
     * a directory, this directory is copied into it recursively, overwriting existing files, or 3) if the destination doesn't
     * exist, [.mkdirs] is called on the destination and this directory is copied into it recursively.
     * @throw GdxRuntimeException if the destination file handle is a [FileType.Classpath] or [FileType.Internal] file,
     * or copying failed.
     */
    override fun copyTo(dest: FileHandle) {
        throw GdxRuntimeException("Cannot copy to an internal file: $dest")
    }

    /** Moves this file to the specified file, overwriting the file if it already exists.
     * @throw GdxRuntimeException if the source or destination file handle is a [FileType.Classpath] or
     * [FileType.Internal] file.
     */
    override fun moveTo(dest: FileHandle) {
        throw GdxRuntimeException("Cannot move an internal file: $file")
    }

    /** Returns the length in bytes of this file, or 0 if this file is a directory, does not exist, or the size cannot otherwise be
     * determined.  */
    override fun length(): Long {
        // TODO: hardcoded length for badlogic.jpg
        if (bytecoderFile.contains("badlogic.jpg")) {
            throw IllegalStateException("Hardcoded for badlogic!")
        }
        return 68465L
    }

    /** Returns the last modified time in milliseconds for this file. Zero is returned if the file doesn't exist. Zero is returned
     * for [FileType.Classpath] files. On Android, zero is returned for [FileType.Internal] files. On the desktop, zero
     * is returned for [FileType.Internal] files on the classpath.  */
    override fun lastModified(): Long {
        return 0
    }

    override fun toString(): String {
        return bytecoderFile
    }

    companion object {
        private fun fixSlashes(path: String): String {
            var pathToFix = path
            pathToFix = pathToFix.replace('\\', '/')
            if (pathToFix.endsWith("/")) {
                pathToFix = pathToFix.substring(0, pathToFix.length - 1)
            }
            return pathToFix
        }
    }
}