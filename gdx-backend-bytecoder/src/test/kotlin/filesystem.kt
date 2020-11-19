import java.io.File
import java.net.URISyntaxException

public fun resolveProjectRootDir(): File {
    try {

        val classInProject = Noop::class.java

        val uri = classInProject.getResource(".").toURI()
        var file = File(uri)
        while (!File(file, "pom.xml").exists()) {
            file = file.parentFile
        }

        return file
    } catch (e: URISyntaxException) {
        throw RuntimeException(e)
    }
}

class Noop