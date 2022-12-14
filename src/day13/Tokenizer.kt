package day13

import peekable
import readInput

sealed class Token {
    class LEFT_BRACKET : Token ()
    class RIGHT_BRACKET : Token ()
    class COMMA : Token ()
    class NUMBER (val value: Int): Token ()
}

class Tokenizer (str: String) : Iterator<Token> {
    val iter = str.iterator().peekable ()

    override fun hasNext(): Boolean {
        return iter.hasNext ()
    }

    fun expect (token: Token) {
        val next = next ()
        if (next != token) {
            throw IllegalArgumentException ("Unexpected token: $token != $next")
        }
    }

    override fun next(): Token {
        while (true) {
            val c = iter.next ()
            when (c) {
                '[' -> return Token.LEFT_BRACKET ()
                ']' -> return Token.RIGHT_BRACKET ()
                ',' -> return Token.COMMA ()
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                    val buf = StringBuffer ()
                    buf.append (c)
                    while (iter.hasNext () && iter.peek ().isDigit ()) {
                        buf.append (iter.next ())
                    }
                    return Token.NUMBER (buf.toString ().toInt ())
                }
                else -> throw IllegalStateException ("Illegal character: $c")
            }
        }

        // NOT REACHED
    }
}

/**
 * Tokenization test.
 */

fun main (args: Array<String>) {
    val example = true

    val dump = { str: String ->
        val tokens = Tokenizer(str).asSequence()
        println("$str\n${tokens.joinToString(", ")}\n")
    }

    val input = readInput(13, example).split("\n\n")
    input.forEach { packets ->
        val (a, b) = packets.split("\n")
        dump(a)
        dump(b)
    }

    return
}

// EOF