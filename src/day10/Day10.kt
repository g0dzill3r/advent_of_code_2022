package day10

import readInput

fun main (args: Array<String>) {
    val example = false
    val input = readInput (10, example)
    val opcodes = OpCode.parse (input)

    // part1
    Computer ().let { cpu ->
        cpu.trace ()

        var total = 0
        val doSample = { tick: Int -> tick == 20 || (tick > 20 && ((tick - 20) % 40 == 0)) }
        cpu.addListener { c, opcode, tick ->
            if (doSample (c.clock)) {
                total += c.clock * c.x
            }
        }
        cpu.execute(opcodes)
        println ("[part1]")
        println ("total=$total")
    }

    // part2
    Computer ().let { cpu ->
        val crt = StringBuffer ()

        cpu.addListener { c, _, _ ->
            val tick = c.clock - 1
            val pos = tick % 40
            if (tick > 0 && tick % 40 == 0) {
                crt.append("\n")
            }
            if (pos in c.x - 1 .. c.x + 1) {
                crt.append ("██")
            } else {
                crt.append ("░░")
            }
        }
        cpu.execute (opcodes)
        println ("\n[part2]")
        println (crt)
    }

    return
}