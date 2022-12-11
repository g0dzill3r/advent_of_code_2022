package day11

import readInput
import java.math.BigInteger
import java.util.regex.Pattern

private val REGEX = """
^Monkey (\d+):\s+Starting items: ([0-9, ]+)\s+Operation: new = old\s([+*])\s([\w\d]+)\s+Test: divisible by (\d+)\s+If true: throw to monkey (\d+)\s+If false: throw to monkey (\d+)${'$'}
""".trimIndent()

data class Monkeys (
    val monkeys: List<Monkey>
) {
    val clamp = monkeys.fold (1) { acc, i -> acc * i.test.toInt () }
    var round: Int = 0
        private set

    fun playRound (monkey: Monkey, worry: (BigInteger) -> BigInteger) {
        while (monkey.items.isNotEmpty()) {
            val (throwTo, bored) = monkey.inspect (worry)
            monkeys[throwTo].items.add (bored.mod (clamp.toBigInteger()))
        }
        return
    }
    fun playRound (worry: (BigInteger) -> BigInteger) {
        monkeys.forEach {
            playRound (it, worry)
        }
        round ++
        return
    }

    fun dump () {
        println ("after round $round")
        monkeys.forEach {
            println ("m${it.index} (${it.inspections}): ${it.items}:")
        }
    }

    companion object {
        fun parse (str: String): Monkeys {
            val parts = str.split ("\n\n")
            val monkeys =  parts.map { Monkey.parse (it) }
            return Monkeys (monkeys)
        }
    }
}

data class Monkey (
    val index: Int,
    val items: MutableList<BigInteger>,
    val operation: Pair<String, String>,
    val test: BigInteger,
    val ifTrue: Int,
    val ifFalse: Int
) {
    var inspections: Int = 0

    fun evaluate (item: BigInteger): BigInteger {
        val (op, rexpr) = operation
        val rval = if (rexpr == "old") item else rexpr.toBigInteger()
        return when (op) {
            "+" -> item + rval
            "*" -> item * rval
            else -> throw Exception ("Invalid op: $op")
        }
    }

    fun inspect (worry: (BigInteger) -> BigInteger): Pair<Int, BigInteger> {
        val item = items.removeAt(0)
        val inspect = evaluate (item)
        val bored = worry (inspect)
        val throwTo = if (bored.mod (test) == 0.toBigInteger()) ifTrue else ifFalse
        inspections ++
        return Pair<Int, BigInteger> (throwTo, bored)
    }

    companion object {
        private val pattern by lazy {
            Pattern.compile (REGEX)
        }

        fun parse (str: String): Monkey {
            val match = pattern.matcher (str)
            if (! match.matches()) {
                throw IllegalArgumentException ("No match: $str")
            }

            var i = 1
            val monkey = match.group (i++).toInt ()
            val items = match.group (i++).split (",").map { it.trim ().toBigInteger() }.toMutableList()
            val op = Pair<String, String> (match.group (i++), match.group (i++))
            val test = match.group (i++).toBigInteger ()
            val ifTrue = match.group (i++).toInt ()
            val ifFalse = match.group (i++).toInt ()

            return Monkey (monkey, items, op, test, ifTrue, ifFalse)
        }
    }
}

fun getMonkeys (example: Boolean): Monkeys {
    val input = readInput (11, example)
    return Monkeys.parse (input)
}

/**
 * Test out the parser.
 */
fun main (args: Array<String>) {
    Boolean.values ().forEach {
        val monkeys = getMonkeys (it)
        monkeys.monkeys.forEach {
            println (it)
        }
    }
    return
}

private fun Boolean.Companion.values () = listOf (true, false)

// EOF