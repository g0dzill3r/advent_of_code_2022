package tmp

fun main() {

    // Fold for a sum of numbers

    val nums = listOf (1, 2, 3, 4, 5, 6, 7, 8)
    val sum = nums.fold (0) {
        acc, i -> acc + i
    }
    println (sum)

    // Fibonnacci as a sequence

    val seq = fibonacci(10)
    seq.forEach {
        println (it)
    }
    return
}

/**
 * A function to return the fibonacci numbers as a Sequence<Int>.
 */
fun fibonacci (count: Int): Sequence<Int> {
    assert (count > 2)
    return sequence {
        var a = 1
        var b = 1

        for (i in 1 .. count) {
            yield (a)
            val c = a + b
            a = b
            b = c
        }
    }
}

// EOF