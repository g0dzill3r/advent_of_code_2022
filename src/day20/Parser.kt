package day20

import readInput

data class Coordinate (val value: Long, val index: Int) {
    override fun toString(): String = "($value, @$index)"
}
data class Coordinates (val input: List<Long>) {
    val data = input.mapIndexed { i, coord -> Coordinate (coord, i) }.toMutableList ()

    fun get (index: Int): Coordinate {
        return data[index % data.size]
    }

    fun findValue (value: Long): Int {
        for (i in data.indices) {
            if (data[i].value == value) {
                return i
            }
        }
        return -1
    }

    fun find (index: Int): Int {
        if (index < 0 || index >= data.size) {
            throw IllegalArgumentException ("Invalid index: $index")
        }
        for (i in data.indices) {
            if (data[i].index == index) {
                return i
            }
        }
        throw IllegalStateException ("Index not found")
    }

    fun indices (): List<Int> = data.map { it.index }

    fun mix (which: Int, debug: (Any) -> Unit = {}) {
        val origin = find (which)
        val rec = data[origin]

        if (rec.value == 0L) {
            return
        }

        data.removeAt (origin)
        var dest = origin + rec.value
        if (dest in 0 until data.size) {
            data.add (dest.toInt (), rec)
        } else if (dest == data.size.toLong()) {
            data.add (rec)
        }  else if (dest > data.size){
            dest %= data.size
            data.add (dest.toInt (), rec)
        } else {
            dest = data.size + dest % data.size
            data.add (dest.toInt (), rec)
        }
    }

    fun mix () {
        for (i in input.indices) {
            mix (i)
        }
        return
    }

    fun dump (which: Int) {
        val index = find (which)

        val maxWidth = data.fold (0) { acc, v -> Math.max (acc, v.value.toString ().length) }
        val format = { value: Long, selected: Boolean  ->
            if (selected) {
                String.format ("[%${maxWidth}d] ", value)
            } else {
                String.format (" %${maxWidth}d  ", value)
            }
        }

        for (i in data.size - 3 until data.size) {
            print (format (data[i].value, false))
        }
        print (" | ")
        for (i in data.indices) {
            print (format (data[i].value, i == index))
        }
        print (" | ")
        for (i in 0 .. 2) {
            print (format (data[i].value, false))
        }
        println ()
    }

    fun dump () = println (toString ())

    fun <T> coordinates (func: (Int, Coordinate)-> T): List<T> = data.mapIndexed { i, c -> func (i, c) }
    fun coordinates (): List<Long> = data.map {it.value }

    override fun toString(): String {
        return StringBuffer().apply {
            append ("data: ${data.joinToString (", ")}\n")
            append ("indices: ${indices()}\n")
            append ("coords : ${coordinates()}")

        }.toString ()
    }

    companion object {
        fun parse (input: String): Coordinates {
            return Coordinates (input.trim ().split ("\n").map { it.toLong () })
        }
    }
}

fun loadCoordinates (example: Boolean): Coordinates = Coordinates.parse (readInput (20, example))

fun main (args: Array<String>) {
    val example = true
    val coords = loadCoordinates(example)
    coords.dump ()
    coords.mix ()
    coords.dump ()
    return
}


// EOF