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

// EOF