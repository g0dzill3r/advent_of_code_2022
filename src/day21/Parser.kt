package day21

import readInput
import java.lang.IllegalArgumentException
import java.util.regex.Pattern

enum class Operation (val symbol: Char, val calc: (Long, Long) -> Long){
    PLUS ('+', { a, b -> a + b}),
    MINUS ('-', { a, b -> a - b}),
    MULTIPLY ('*', { a, b -> a * b }),
    DIVIDE ('/', { a, b -> a / b}),
    EQUALS ('=', { a, b -> if (a == b) 1 else 0 });

    fun invoke (left: Long, right: Long) = calc (left, right)

    companion object {
        private val map = mutableMapOf<Char, Operation> ().apply {
            values ().forEach {
                put (it.symbol, it)
            }
        }.toMap ()

        fun decode (c: Char): Operation = map[c] as Operation
    }
}

sealed class Expression {
    data class Symbol (val symbol: String): Expression () {
        override fun toString () = "$symbol"
    }
    data class Number (val value: Long) : Expression () {
        override fun toString () = "$value"
    }
    data class Calculation (val left: Expression, val operation: Operation, val right: Expression) : Expression () {
        fun invoke (left: Long, right: Long): Long = operation.invoke(left, right)
        override fun toString () = "($left ${operation.symbol} $right)"
    }

    companion object {
        val REGEX0 = "^([a-z]{4}): (\\d+)$"
        val pattern0 = Pattern.compile (REGEX0)
        val REGEX1 = "^([a-z]{4}): ([a-z]{4}) ([-+*/]) ([a-z]{4})$"
        val pattern1 = Pattern.compile (REGEX1)
        fun parse (str: String, map: MutableMap<String, Expression>)  {
            pattern0.matcher (str).let {
                if (it.matches ()) {
                    val symbol = it.group (1)
                    val expr = Number (it.group (2).toLong ())
                    map.put (symbol, expr)
                    return
                }
            }
            pattern1.matcher (str).let {
                if (it.matches ()) {
                    val symbol = it.group (1)
                    val expr = Calculation (Symbol (it.group (2)), Operation.decode (it.group (3)[0]), Symbol (it.group (4)))
                    map.put (symbol, expr)
                    return
                }
            }
            throw IllegalArgumentException ("Unrecognized input: '$str'")
        }
    }
}

// EOF
