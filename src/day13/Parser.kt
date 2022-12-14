package day13

import PeekableIterator
import peekable
import readInput

/**
 *
 */

class Packets (val data: List<Pair<List<Any>, List<Any>>>) {
    val unsorted: List<List<Any>>
        get () {
            return mutableListOf<List<Any>>().apply {
                data.forEach {
                    val (a, b) = it
                    add (a)
                    add (b)
                }
            }
        }

    fun sorted (additional: List<List<Any>> = listOf()): List<List<Any>> {
        return mutableListOf<List<Any>> ().apply {
            addAll (unsorted)
            addAll (additional)
        }.sortedWith { a, b -> compare (a, b) }
    }

    companion object {
        fun parse (input: String): Packets {
            val list = mutableListOf<Pair<List<Any>, List<Any>>> ()
            val pairs = input.split ("\n\n")
            pairs.forEach {
                val (a, b) = it.split ("\n")
                list.add (Pair (parsePacket (a), parsePacket (b)))
            }
            return Packets (list)
        }

        fun parsePacket (str: String): List<Any> {
            val toker = Tokenizer (str).peekable()
            return parsePacket (toker)
        }

        fun parsePacket (toker: PeekableIterator<Token>): List<Any> {
            val list = mutableListOf<Any> ()
            var token = toker.next ()
            if (token !is Token.LEFT_BRACKET) {
                throw IllegalArgumentException ("Unexpected token: $token")
            }
            while (true) {
                token = toker.peek ()
                when (token) {
                    is Token.LEFT_BRACKET -> list.add (parsePacket (toker))
                    is Token.RIGHT_BRACKET -> {
                        toker.next ()
                        break
                    }
                    is Token.NUMBER -> {
                        toker.next ()
                        list.add (token.value)
                    }
                    is Token.COMMA -> {
                        toker.next ()
                    }
                }
            }
            return list
        }
    }
}

fun loadPackets (example: Boolean): Packets {
    val input = readInput (13, example)
    return Packets.parse (input)
}

fun main (args: Array<String>) {
    val example = true

    // Tokenization test

    val dump = { str: String ->
        val tokens = Tokenizer (str).asSequence()
        println ("str\n    " + tokens.joinToString(", "))
    }

    val input = readInput (13, example).split ("\n\n")
    input.forEach { packets ->
        val (a, b) = packets.split ("\n")
        dump (a)
        dump (b)
    }

    // Parsing test

    val dump2 = { str: String ->
        val packets = Packets.parsePacket (str)
        println ("$str\n$packets\n---")
    }

    input.forEach { packets ->
        val (a, b) = packets.split ("\n")
        dump2 (a)
        dump2 (b)
    }

    return
}
// EOF