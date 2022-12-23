package day19

import kotlin.math.sign

data class Score (val list: List<Int>) {
    operator fun compareTo (other: Score): Int {
        if (list.size != other.list.size) {
            throw IllegalArgumentException ("Mismatched scores: ${list.size} != ${other.list.size}")
        }
        list.forEachIndexed { i, score0 ->
            val score1 = other.list[i]
            if (score0 != score1) {
                return (score0 - score1).sign
            }
        }

        return 0
    }
}

fun main (args: Array<String>) {
    val s0 = Score (listOf (1, 0, 0, 0))
    val s1 = Score (listOf (0, 1, 0, 0))
    val s2 = Score (listOf (0, 0, 1, 0))
    val s3 = Score (listOf (0, 0, 0, 1))
    val s4 = Score (listOf (0, 0, 0, 0))

    val list = listOf (s0, s1, s2, s3, s4)

    list.forEach { a ->
        list.forEach { b ->
            println ("$a ? $b = ${a.compareTo (b)} ${a <= b}")
        }
    }

    return
}

// EOF