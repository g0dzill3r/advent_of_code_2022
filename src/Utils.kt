import java.io.File
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(day: Int, example: Boolean = false) =
    File("src/day${day}", "day${day}${if (example) "-example" else ""}.txt")
        .readText(Charset.defaultCharset ())

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun <T> Iterator<T>.peekable () : PeekableIterator<T> = PeekableIterator<T> (this)
class PeekableIterator<T> (val wrapped: Iterator<T>): Iterator<T> {
    private var saved: T? = null;

    override fun hasNext(): Boolean {
        return if (saved != null) {
            true
        } else {
            wrapped.hasNext ()
        }
    }

    fun peek (): T {
        if (saved == null) {
            saved = wrapped.next ()
        }
        return saved!!
    }

    override fun next(): T {
        return saved?.let {
            val tmp = saved
            saved = null
            tmp
        } ?: wrapped.next ()
    }
}