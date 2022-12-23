package day23

import readInput
import java.io.File
import java.lang.IllegalArgumentException
import kotlin.math.sign

data class Point (val x: Int, val y: Int) {
    val adjacent: List<Point>
        get () {
            val list = mutableListOf<Point> ()
            for (x in -1 .. 1) {
                for (y in -1 .. 1) {
                    if (x == 0 && y == 0) {
                        continue
                    }
                    list.add (Point (x, y))
                }
            }
            return list
        }

    fun subtract (other: Point): Point = Point (x - other.x, y - other.y)
    fun add (other: Point): Point = Point (x + other.x, y + other.y)
    fun add (dx: Int, dy: Int): Point = Point (x + dx, y + dy)
}


enum class Direction (val dx: Int, val dy: Int, val symbol: Char) {
    N (0, -1, '↑'), NE (1, -1, '↗'), E (1, 0, '→'), SE (1, 1, '↘'), S (0, 1, '↓'), SW (-1, 1, '↙'), W (-1, 0, '←'), NW (-1, -1, '↖');

    companion object {
        fun fromPoint (point: Point): Direction {
            val dx = point.x.sign
            val dy = point.y.sign
            Direction.values ().forEach {
                if (it.dx == dx && it.dy == dy) {
                    return it
                }
            }
            throw Exception ()
        }
    }
}

enum class Thing (val encoded: Char) {
    ELF ('#'), GROUND ('.');

    companion object {
        private val map = mutableMapOf<Char, Thing> ().apply {
            Thing.values ().forEach {
                this[it.encoded] = it
            }
        }.toMap ()

        fun decode (encoded: Char): Thing = map[encoded] ?: throw IllegalArgumentException ("Invalid encoding: $encoded")
    }
}

class Grid (val data: Map<Point, Thing>) {
    val firstElf: Point
        get () = elves[0]

    val xmin: Int
        get () = elves.fold (firstElf.x) { acc, point -> Math.min (acc, point.x) }
    val xmax: Int
        get () = elves.fold (firstElf.x) { acc, point -> Math.max (acc, point.x) }
    val ymin: Int
        get () = elves.fold (firstElf.y) { acc, point -> Math.min (acc, point.y) }
    val ymax: Int
        get () = elves.fold (firstElf.y) { acc, point -> Math.max (acc, point.y) }

    val elves = mutableListOf<Point> ().apply {
        data.forEach { (point, thing) ->
            if (thing == Thing.ELF) {
                add (point)
            }
        }
    }

    private fun hasAdjacencies (point: Point): Boolean {
        point.adjacent.forEach {
            if (elves.contains (point.add (it))) {
                return true
            }
        }
        return false
    }

    val proposed = mutableListOf<Pair<Point, Point>> ()

    val validMoves: List<Pair<Point, Point>>
        get () {
            val map = mutableMapOf<Point, Int> ()
            proposed.forEach { (_, to) ->
                if (map.containsKey (to)) {
                    map[to] = map[to]!! + 1
                } else {
                    map[to] = 1
                }
            }

            return proposed.filter { (_, to) ->
                map[to] == 1
            }
        }

    private fun rotateDirections () {
        checkDirections.add (checkDirections.removeAt (0))
        return
    }

    private val checkDirections = mutableListOf<List<Direction>> (
        listOf (Direction.N, Direction.NE, Direction.NW),
        listOf (Direction.S, Direction.SE, Direction.SW),
        listOf (Direction.W, Direction.NW, Direction.SW),
        listOf (Direction.E, Direction.NE, Direction.SE)
    )

    private fun check (point: Point): Point? {
        checkDirections.forEach { dirs ->
            if (isEmpty (point, dirs)) {
                return point.add (dirs[0].dx, dirs[0].dy)
            }
        }
        return null
    }

    private fun isEmpty (point: Point, dirs: List<Direction>): Boolean {
        for (dir in dirs) {
            if (elves.contains (point.add (dir.dx, dir.dy))) {
                return false
            }
        }
        return true
    }

    fun round1 () {
        elves.forEach { elf ->
            if (hasAdjacencies (elf)) {
                val maybe = check (elf)
                if (maybe != null) {
                    proposed.add (Pair (elf, maybe))
                }
            }
        }

        rotateDirections()
        return
    }

    /**
     *
     */
    fun round2 () {
        val moves = validMoves
        elves.removeAll (moves.map { it.first })
        elves.addAll (moves.map { it.second })
        proposed.clear()
        return
    }

    fun round () {
        round1()
        round2()
        return
    }

    fun dumpProposed () {
        val valid = validMoves
        proposed.forEach { pair ->
            val (from, to) = pair
            val delta = to.subtract (from)
            val isValid = valid.contains (pair)
        }
        return
    }

    val emptyTiles: Int
        get () {
            var total = 0
            for (y in ymin .. ymax) {
                for (x in xmin .. xmax) {
                    val point = Point (x, y)
                    if (! elves.contains (point)) {
                        total ++
                    }
                }
            }
            return total
        }

    fun dumpMoves () {
        val valid = validMoves

        proposed.forEach {
            println ("$it - ${valid.contains (it)}")
        }

        return
    }

    fun dump () = println (toString())
    override fun toString (): String {
        val valid = validMoves.map { it.first }

        return StringBuffer ().apply {
            for (y in ymin - 1 .. ymax + 1) {
                append (String.format ("%3d: ", y))
                for (x in xmin - 1 .. xmax + 1) {
                    val point = Point (x, y)
                    if (elves.contains (point)) {
                        val move = proposed.firstOrNull { (from, _) -> from == point }
                        if (move != null) {
                            val dir = Direction.fromPoint (move.second.subtract (move.first))
                            append (dir.symbol)
                        } else {
                            append ('#')
                        }
                    } else {
                        append (Thing.GROUND.encoded)
                    }
                }
                append ("\n")
            }
        }.toString ()
    }
    companion object {
        fun parse (input: String): Grid {
            val map = mutableMapOf<Point, Thing> ()
            input.trim ().split ("\n").forEachIndexed { y, row ->
                row.forEachIndexed { x, t ->
                    map.put (Point (x, y), Thing.decode (t))
                }
            }
            return Grid (map)
        }
    }
}

fun loadGrid (example: Boolean): Grid {
    val input = readInput(23, example)
    return Grid.parse (input)
}

fun main (args: Array<String>) {
    val example = false
    val grid = loadGrid (example)
//    val input = File ("src/day23/day23-small.txt").readText()
//    val grid = Grid.parse (input)

    grid.dump ()
//    while (true) {
    repeat (10) {
        grid.round1 ()
        grid.dump ()
        if (grid.validMoves.isEmpty()) {
//            break
        }
        grid.round2 ()
        grid.dump ()
        println (" ======== ")
    }
    println ("part1=${grid.emptyTiles}")
    return
}