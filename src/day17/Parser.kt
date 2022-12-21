package day17

import readInput

enum class JetDirection (val symbol: Char){
    LEFT ('<'),
    RIGHT ('>');

    companion object {
        private val map = mutableMapOf<Char, JetDirection> ().apply {
            JetDirection.values ().forEach { put (it.symbol, it) }
        }

        fun fromSymbol (symbol: Char): JetDirection = map[symbol] as JetDirection
    }
}

data class JetSequence (val raw: String) {
    val iterator: TrackableIterator<JetDirection>
        get () {
            return object: TrackableIterator<JetDirection> {
                override var current = 0
                override val size = raw.length
                override fun hasNext() = true
                override fun next(): JetDirection {
                    val next = current ++
                    current %= raw.length
                    return JetDirection.fromSymbol (raw[next])
                }
            }
        }
}

fun loadJetSequence (example: Boolean): JetSequence {
    val input = readInput (17, example)
    return JetSequence (input)
}

fun main (args: Array<String>) {
    val example = true
    val seq = loadJetSequence(example)
    val iter = seq.iterator
    repeat (5) {
        repeat (seq.raw.length) {
            print (iter.next ().symbol)
        }
        println ()
    }
    return
}

// EOF