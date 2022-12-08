import java.io.File
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(day: Int) = File("src/day${day}", "day${day}.txt")
    .readText(Charset.defaultCharset ())

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')
