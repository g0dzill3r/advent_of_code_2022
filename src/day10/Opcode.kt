package day10

import readInput

sealed class OpCode (val opcode: String, val cycles: Int) {
    class NOOP : OpCode ("noop", 1) {
        override fun toString (): String = opcode
        override fun execute (cpu: Computer, tick: Int) = Unit
        companion object {
            fun parse (args: List<String>): OpCode = NOOP ()
        }
    }
    class ADDX (val value: Int): OpCode ("addx", 2) {
        override fun toString (): String = "$opcode $value"
        override fun execute (cpu: Computer, tick: Int) {
            if (tick == cycles) {
                cpu.x += value
            }
        }
        companion object {
            fun parse (args: List<String>): OpCode = ADDX (args[1].toInt ())
        }
    }

    abstract fun execute (cpu: Computer, tick: Int)

    companion object {
        fun parse (str: String): List<OpCode> {
            return str.split ("\n").map {
                val args = it.trim ().split (" ")

                when {
                    args[0] == "noop" -> NOOP.parse (args)
                    args[0] == "addx" -> ADDX.parse (args)
                    else -> throw IllegalStateException ("Unrecognized opcode: ${args[0]}")
                }
            }
        }
    }
}

fun main (args: Array<String>) {
    val example = true
    val input = readInput (10, example)
    val opcodes = OpCode.parse (input)
    opcodes.forEachIndexed { i, opcode ->
        println (String.format ("%04d: %s", i, opcode))
    }
    return
}

// EOF