package day12

import readInput
import java.io.File
import java.nio.charset.Charset

fun readTerrain (example: Boolean): Terrain {
    val input = readInput (12, example)
    val terrain = Terrain (input)
    return terrain
}

fun readTiny (): Terrain = Terrain (File("src/day12", "day12-tiny.txt").readText(Charset.defaultCharset ()))

fun time (func: () -> Unit) {
    val start = System.currentTimeMillis()
    try {
        func ()
    }
    catch (e: Throwable) {
        e.printStackTrace ()
    }
    val elapsed = System.currentTimeMillis() - start
    println ("${elapsed.commify ()}ms")
    return
}

fun Number.commify (): String {
    val str = this.toString ()
    val buf = StringBuffer ()
    str.reversed().mapIndexed { i, c ->
        if (i > 0 && i % 3 == 0 && i != str.length) {
            buf.append (",")
        }
        buf.append (c)
    }
    return buf.toString ().reversed ()
}

// EOF