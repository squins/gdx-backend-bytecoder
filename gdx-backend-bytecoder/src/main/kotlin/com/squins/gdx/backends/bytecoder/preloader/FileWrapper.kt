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

import com.badlogic.gdx.Files
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.StreamUtils
import com.squins.gdx.backends.bytecoder.makeAndLogIllegalArgumentException
import java.io.*

/** Used in PreloaderBundleGenerator to ease my pain. Since we emulate the original FileHandle, i have to make a copy...
 * @author mzechner
 * @author Nathan Sweet
 */

const val tagFW = "FileWrapper"

open class FileWrapper {
    var file: File? = null
    protected var type: Files.FileType? = null

    protected constructor()

    /** Creates a new absolute FileHandle for the file name. Use this for tools on the desktop that don't need any of the backends.
     * Do not use this constructor in case you write something cross-platform. Use the [Files] interface instead.
     * @param fileName the filename.
     */
    constructor(fileName: String) {
        file = File(fileName)
        type = Files.FileType.Absolute
    }

    /** Creates a new absolute FileHandle for the [File]. Use this for tools on the desktop that don't need any of the
     * backends. Do not use this constructor in case you write something cross-platform. Use the [Files] interface instead.
     * @param file the file.
     */
    constructor(file: File) {
        this.file = file
        type = Files.FileType.Absolute
    }

    protected constructor(fileName: String, type: Files.FileType) {
        this.type = type
        file = File(fileName)
    }

    protected constructor(file: File, type: Files.FileType) {
        this.file = file
        this.type = type
    }

    fun path(): String {
        return file!!.path
    }

    fun name(): String {
        return file!!.name
    }

    fun extension(): String {
        val name = file!!.name
        val dotIndex = name.lastIndexOf('.')
        return if (dotIndex == -1) "" else name.substring(dotIndex + 1)
    }

    fun nameWithoutExtension(): String {
        val name = file!!.name
        val dotIndex = name.lastIndexOf('.')
        return if (dotIndex == -1) name else name.substring(0, dotIndex)
    }

    fun type(): Files.FileType? {
        return type
    }

    /** Returns a java.io.File that represents this file handle. Note the returned file will only be usable for
     * [FileType.Absolute] and [FileType.External] file handles.  */
    fun file(): File {
        return if (type == Files.FileType.External) File(Gdx.files.externalStoragePath, file!!.path) else file!!
    }

    /** Returns a stream for reading this file as bytes.
     * @throw GdxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    fun read(): InputStream {
        return if (type == Files.FileType.Classpath || type == Files.FileType.Internal && !file!!.exists()
                || type == Files.FileType.Local && !file!!.exists()) {
            FileWrapper::class.java.getResourceAsStream("/" + file!!.path.replace('\\', '/'))
                    ?: throw makeAndLogIllegalArgumentException(tagFW, "File not found: $file ($type)")
        } else try {
            FileInputStream(file())
        } catch (ex: Exception) {
            if (file().isDirectory) throw makeAndLogIllegalArgumentException(tagFW, "Cannot open a stream to a directory: $file ($type), exc: $ex")
            throw makeAndLogIllegalArgumentException(tagFW, "Error reading file: $file ($type), exc: $ex")
        }
    }

    /** Returns a buffered stream for reading this file as bytes.
     * @throw GdxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    fun read(bufferSize: Int): BufferedInputStream {
        return BufferedInputStream(read(), bufferSize)
    }

    /** Returns a reader for reading this file as characters.
     * @throw GdxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    fun reader(): Reader {
        return InputStreamReader(read())
    }

    /** Returns a reader for reading this file as characters.
     * @throw GdxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    fun reader(charset: String): Reader {
        return try {
            InputStreamReader(read(), charset)
        } catch (ex: UnsupportedEncodingException) {
            throw makeAndLogIllegalArgumentException(tagFW, " Error reading file: $this, exc: $ex")
        }
    }

    /** Returns a buffered reader for reading this file as characters.
     * @throw GdxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    fun reader(bufferSize: Int): BufferedReader {
        return BufferedReader(InputStreamReader(read()), bufferSize)
    }

    /** Returns a buffered reader for reading this file as characters.
     * @throw GdxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    fun reader(bufferSize: Int, charset: String): BufferedReader {
        return try {
            BufferedReader(InputStreamReader(read(), charset), bufferSize)
        } catch (ex: UnsupportedEncodingException) {
            throw makeAndLogIllegalArgumentException(tagFW, "Error reading file: $this, exc: $ex")
        }
    }
    /** Reads the entire file into a string using the specified charset.
     * @throw GdxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    /** Reads the entire file into a string using the platform's default charset.
     * @throw GdxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    @JvmOverloads
    fun readString(charset: String? = null): String {
        var fileLength = length().toInt()
        if (fileLength == 0) fileLength = 512
        val output = StringBuilder(fileLength)
        var reader: InputStreamReader? = null
        try {
            reader = if (charset == null) InputStreamReader(read()) else InputStreamReader(read(), charset)
            val buffer = CharArray(256)
            while (true) {
                val length = reader.read(buffer)
                if (length == -1) break
                output.append(buffer, 0, length)
            }
        } catch (ex: IOException) {
            throw makeAndLogIllegalArgumentException(tagFW, "Error reading layout file: $this, exc: $ex")
        } finally {
            StreamUtils.closeQuietly(reader)
        }
        return output.toString()
    }

    /** Reads the entire file into a byte array.
     * @throw GdxRuntimeException if the file handle represents a directory, doesn't exist, or could not be read.
     */
    fun readBytes(): ByteArray {
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
            throw makeAndLogIllegalArgumentException(tagFW, "Error reading file: $this, exc: $ex")
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
    fun readBytes(bytes: ByteArray, offset: Int, size: Int): Int {
        val input = read()
        var position = 0
        try {
            while (true) {
                val count = input.read(bytes, offset + position, size - position)
                if (count <= 0) break
                position += count
            }
        } catch (ex: IOException) {
            throw makeAndLogIllegalArgumentException(tagFW, "Error reading file: $this, exc: $ex")
        } finally {
            try {
                input.close()
            } catch (ignored: IOException) {
            }
        }
        return position - offset
    }

    /** Returns a stream for writing to this file. Parent directories will be created if necessary.
     * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
     * @throw GdxRuntimeException if this file handle represents a directory, if it is a [FileType.Classpath] or
     * [FileType.Internal] file, or if it could not be written.
     */
    fun write(append: Boolean): OutputStream {
        if (type == Files.FileType.Classpath) throw makeAndLogIllegalArgumentException(tagFW, "Cannot write to a classpath file: $file")
        if (type == Files.FileType.Internal) throw makeAndLogIllegalArgumentException(tagFW, "Cannot write to an internal file: $file")
        parent().mkdirs()
        return try {
            FileOutputStream(file(), append)
        } catch (ex: Exception) {
            if (file().isDirectory) throw makeAndLogIllegalArgumentException(tagFW, "Cannot open a stream to a directory: $file ($type), exc: $ex")
            throw makeAndLogIllegalArgumentException(tagFW, "Error writing file: $file ($type), exc: $ex")
        }
    }

    /** Reads the remaining bytes from the specified stream and writes them to this file. The stream is closed. Parent directories
     * will be created if necessary.
     * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
     * @throw GdxRuntimeException if this file handle represents a directory, if it is a [FileType.Classpath] or
     * [FileType.Internal] file, or if it could not be written.
     */
    fun write(input: InputStream?, append: Boolean) {
        var output: OutputStream? = null
        try {
            output = write(append)
            val buffer = ByteArray(4096)
            while (true) {
                val length = input!!.read(buffer)
                if (length == -1) break
                output.write(buffer, 0, length)
            }
        } catch (ex: Exception) {
            throw makeAndLogIllegalArgumentException(tagFW, "Error stream writing to file: $file ($type), exc: $ex")
        } finally {
            try {
                input?.close()
            } catch (ignored: Exception) {
            }
            try {
                output?.close()
            } catch (ignored: Exception) {
            }
        }
    }
    /** Returns a writer for writing to this file. Parent directories will be created if necessary.
     * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
     * @param charset May be null to use the default charset.
     * @throw GdxRuntimeException if this file handle represents a directory, if it is a [FileType.Classpath] or
     * [FileType.Internal] file, or if it could not be written.
     */
    /** Returns a writer for writing to this file using the default charset. Parent directories will be created if necessary.
     * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
     * @throw GdxRuntimeException if this file handle represents a directory, if it is a [FileType.Classpath] or
     * [FileType.Internal] file, or if it could not be written.
     */
    @JvmOverloads
    fun writer(append: Boolean, charset: String? = null): Writer {
        if (type == Files.FileType.Classpath) throw makeAndLogIllegalArgumentException(tagFW, "Cannot write to a classpath file: $file")
        if (type == Files.FileType.Internal) throw makeAndLogIllegalArgumentException(tagFW, "Cannot write to an internal file: $file")
        parent().mkdirs()
        return try {
            val output = FileOutputStream(file(), append)
            if (charset == null) OutputStreamWriter(output) else OutputStreamWriter(output, charset)
        } catch (ex: IOException) {
            if (file()!!.isDirectory) throw makeAndLogIllegalArgumentException(tagFW, "Cannot open a stream to a directory: $file ($type), exc: $ex")
            throw makeAndLogIllegalArgumentException(tagFW, "Error writing file: $file ($type), exc: $ex")
        }
    }
    /** Writes the specified string to the file as UTF-8. Parent directories will be created if necessary.
     * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
     * @param charset May be null to use the default charset.
     * @throw GdxRuntimeException if this file handle represents a directory, if it is a [FileType.Classpath] or
     * [FileType.Internal] file, or if it could not be written.
     */
    /** Writes the specified string to the file using the default charset. Parent directories will be created if necessary.
     * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
     * @throw GdxRuntimeException if this file handle represents a directory, if it is a [FileType.Classpath] or
     * [FileType.Internal] file, or if it could not be written.
     */
    @JvmOverloads
    fun writeString(string: String, append: Boolean, charset: String? = null) {
        var writer: Writer? = null
        try {
            writer = writer(append, charset)
            writer.write(string)
        } catch (ex: Exception) {
            throw makeAndLogIllegalArgumentException(tagFW, "Error writing file: $file ($type), exc: $ex")
        } finally {
            StreamUtils.closeQuietly(writer)
        }
    }

    /** Writes the specified bytes to the file. Parent directories will be created if necessary.
     * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
     * @throw GdxRuntimeException if this file handle represents a directory, if it is a [FileType.Classpath] or
     * [FileType.Internal] file, or if it could not be written.
     */
    fun writeBytes(bytes: ByteArray, append: Boolean) {
        val output = write(append)
        try {
            output.write(bytes)
        } catch (ex: IOException) {
            throw makeAndLogIllegalArgumentException(tagFW, "Error writing file: $file ($type), exc: $ex")
        } finally {
            try {
                output.close()
            } catch (ignored: IOException) {
            }
        }
    }

    /** Writes the specified bytes to the file. Parent directories will be created if necessary.
     * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
     * @throw GdxRuntimeException if this file handle represents a directory, if it is a [FileType.Classpath] or
     * [FileType.Internal] file, or if it could not be written.
     */
    fun writeBytes(bytes: ByteArray?, offset: Int, length: Int, append: Boolean) {
        val output = write(append)
        try {
            output.write(bytes, offset, length)
        } catch (ex: IOException) {
            throw makeAndLogIllegalArgumentException(tagFW, "Error writing file: $file ($type), exc: $ex")
        } finally {
            try {
                output.close()
            } catch (ignored: IOException) {
            }
        }
    }

    /** Returns the paths to the children of this directory. Returns an empty list if this file handle represents a file and not a
     * directory. On the desktop, an [FileType.Internal] handle to a directory on the classpath will return a zero length
     * array.
     * @throw GdxRuntimeException if this file is an [FileType.Classpath] file.
     */
    fun list(): Array<FileWrapper?> {
        if (type == Files.FileType.Classpath) throw makeAndLogIllegalArgumentException(tagFW, "Cannot list a classpath directory: $file")
        val relativePaths = file()!!.list() ?: return arrayOfNulls(0)
        val handles = arrayOfNulls<FileWrapper>(relativePaths.size)
        var i = 0
        val n = relativePaths.size
        while (i < n) {
            handles[i] = child(relativePaths[i])
            i++
        }
        return handles
    }

    /** Returns the paths to the children of this directory with the specified suffix. Returns an empty list if this file handle
     * represents a file and not a directory. On the desktop, an [FileType.Internal] handle to a directory on the classpath
     * will return a zero length array.
     * @throw GdxRuntimeException if this file is an [FileType.Classpath] file.
     */
    fun list(suffix: String?): Array<FileWrapper?> {
        if (type == Files.FileType.Classpath) throw makeAndLogIllegalArgumentException(tagFW, "Cannot list a classpath directory: $file")
        val relativePaths = file().list() ?: return arrayOfNulls(0)
        var handles = arrayOfNulls<FileWrapper>(relativePaths.size)
        var count = 0
        var i = 0
        val n = relativePaths.size
        while (i < n) {
            val path = relativePaths[i]
            if (!path.endsWith(suffix!!)) {
                i++
                continue
            }
            handles[count] = child(path)
            count++
            i++
        }
        if (count < relativePaths.size) {
            val newHandles = arrayOfNulls<FileWrapper>(count)
            System.arraycopy(handles, 0, newHandles, 0, count)
            handles = newHandles
        }
        return handles
    }

    /** Returns true if this file is a directory. Always returns false for classpath files. On Android, an [FileType.Internal]
     * handle to an empty directory will return false. On the desktop, an [FileType.Internal] handle to a directory on the
     * classpath will return false.  */
    val isDirectory: Boolean
        get() = if (type == Files.FileType.Classpath) false else file().isDirectory

    /** Returns a handle to the child with the specified name.
     * @throw GdxRuntimeException if this file handle is a [FileType.Classpath] or [FileType.Internal] and the child
     * doesn't exist.
     */
    fun child(name: String): FileWrapper {
        return if (file!!.path.isEmpty()) FileWrapper(File(name), type!!) else FileWrapper(File(file, name), type!!)
    }

    fun parent(): FileWrapper {
        var parent = file!!.parentFile
        if (parent == null) {
            parent = if (type == Files.FileType.Absolute) File("/") else File("")
        }
        return FileWrapper(parent, type!!)
    }

    /** @throw GdxRuntimeException if this file handle is a [FileType.Classpath] or [FileType.Internal] file.
     */
    fun mkdirs(): Boolean {
        if (type == Files.FileType.Classpath) throw makeAndLogIllegalArgumentException(tagFW, "Cannot mkdirs with a classpath file: $file")
        if (type == Files.FileType.Internal) throw makeAndLogIllegalArgumentException(tagFW, "Cannot mkdirs with an internal file: $file")
        return file().mkdirs()
    }

    /** Returns true if the file exists. On Android, a [FileType.Classpath] or [FileType.Internal] handle to a directory
     * will always return false.  */
    fun exists(): Boolean {
        when (type) {
            Files.FileType.Internal -> {
                return if (file!!.exists()) true else FileWrapper::class.java.getResource("/" + file!!.path.replace('\\', '/')) != null
            }
            Files.FileType.Classpath -> return FileWrapper::class.java.getResource("/" + file!!.path.replace('\\', '/')) != null
        }
        return file().exists()
    }

    /** Deletes this file or empty directory and returns success. Will not delete a directory that has children.
     * @throw GdxRuntimeException if this file handle is a [FileType.Classpath] or [FileType.Internal] file.
     */
    fun delete(): Boolean {
        if (type == Files.FileType.Classpath) throw makeAndLogIllegalArgumentException(tagFW, "Cannot delete a classpath file: $file")
        if (type == Files.FileType.Internal) throw makeAndLogIllegalArgumentException(tagFW, "Cannot delete an internal file: $file")
        return file()!!.delete()
    }

    /** Deletes this file or directory and all children, recursively.
     * @throw GdxRuntimeException if this file handle is a [FileType.Classpath] or [FileType.Internal] file.
     */
    fun deleteDirectory(): Boolean {
        if (type == Files.FileType.Classpath) throw makeAndLogIllegalArgumentException(tagFW, "Cannot delete a classpath file: $file")
        if (type == Files.FileType.Internal) throw makeAndLogIllegalArgumentException(tagFW, "Cannot delete an internal file: $file")
        return deleteDirectory(file())
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
    fun copyTo(dest: FileWrapper) {
        var dest = dest
        val sourceDir = isDirectory
        if (!sourceDir) {
            if (dest.isDirectory) dest = dest.child(name())
            copyFile(this, dest)
            return
        }
        if (dest.exists()) {
            if (!dest.isDirectory) throw makeAndLogIllegalArgumentException(tagFW, "Destination exists but is not a directory: $dest")
        } else {
            dest.mkdirs()
            if (!dest.isDirectory) throw makeAndLogIllegalArgumentException(tagFW, "Destination directory cannot be created: $dest")
        }
        if (!sourceDir) dest = dest.child(name())
        copyDirectory(this, dest)
    }

    /** Moves this file to the specified file, overwriting the file if it already exists.
     * @throw GdxRuntimeException if the source or destination file handle is a [FileType.Classpath] or
     * [FileType.Internal] file.
     */
    fun moveTo(dest: FileWrapper) {
        if (type == Files.FileType.Classpath) throw makeAndLogIllegalArgumentException(tagFW, "Cannot move a classpath file: $file")
        if (type == Files.FileType.Internal) throw makeAndLogIllegalArgumentException(tagFW, "Cannot move an internal file: $file")
        copyTo(dest)
        delete()
    }

    /** Returns the length in bytes of this file, or 0 if this file is a directory, does not exist, or the size cannot otherwise be
     * determined.  */
    fun length(): Long {
        return file().length()
    }

    /** Returns the last modified time in milliseconds for this file. Zero is returned if the file doesn't exist. Zero is returned
     * for [FileType.Classpath] files. On Android, zero is returned for [FileType.Internal] files. On the desktop, zero
     * is returned for [FileType.Internal] files on the classpath.  */
    fun lastModified(): Long {
        return file()!!.lastModified()
    }

    override fun toString(): String {
        return file!!.path
    }

    companion object {
        fun tempFile(prefix: String?): FileWrapper {
            return try {
                FileWrapper(File.createTempFile(prefix, null))
            } catch (ex: IOException) {
                throw makeAndLogIllegalArgumentException(tagFW, "Unable to create temp file., exc: $ex")
            }
        }

        fun tempDirectory(prefix: String?): FileWrapper {
            return try {
                val file = File.createTempFile(prefix, null)
                if (!file.delete()) throw IOException("Unable to delete temp file: $file")
                if (!file.mkdir()) throw IOException("Unable to create temp directory: $file")
                FileWrapper(file)
            } catch (ex: IOException) {
                throw makeAndLogIllegalArgumentException(tagFW, "Unable to create temp file., exc: $ex")
            }
        }

        private fun deleteDirectory(file: File?): Boolean {
            if (file!!.exists()) {
                val files = file.listFiles()
                if (files != null) {
                    var i = 0
                    val n = files.size
                    while (i < n) {
                        if (files[i].isDirectory) deleteDirectory(files[i]) else files[i].delete()
                        i++
                    }
                }
            }
            return file.delete()
        }

        private fun copyFile(source: FileWrapper?, dest: FileWrapper) {
            try {
                dest.write(source!!.read(), false)
            } catch (ex: Exception) {
                throw makeAndLogIllegalArgumentException(tagFW, """Error copying source file: ${source!!.file} (${source.type},
To destination: ${dest.file} (${dest.type}, exc: $ex""")
            }
        }

        private fun copyDirectory(sourceDir: FileWrapper?, destDir: FileWrapper) {
            destDir.mkdirs()
            val files = sourceDir!!.list()
            var i = 0
            val n = files.size
            while (i < n) {
                val srcFile = files[i]
                val destFile = destDir.child(srcFile!!.name())
                if (srcFile.isDirectory) copyDirectory(srcFile, destFile) else copyFile(srcFile, destFile)
                i++
            }
        }
    }
}