package day10

import day9.repeat

typealias ComputerListener = (Computer, OpCode, Int) -> Unit
class Computer {
    var clock = 1
        private set
    var x: Int = 1

    private val lers = mutableListOf<ComputerListener> ()
    fun addListener (ler: ComputerListener) { lers.add (ler) }

    fun execute (opcodes: List<OpCode>) {
        opcodes.forEach { execute (it) }
    }

    fun execute (opcode: OpCode) {

        opcode.cycles.repeat { tick ->
            lers.forEach { it (this, opcode, tick) }
            opcode.execute (this, tick)
            clock ++
        }
        opcode.execute (this, opcode.cycles)
        return
    }

    fun trace () {
        addListener { c, opcode, tick ->
            println (String.format ("c:%05d x:%05d t:%d o:%s", c.clock, c.x, tick, opcode))
        }
    }
    
    override fun toString () : String {
        return String.format ("c:%05d x:%05d", clock, x)
    }
}

// EOF