package day22

import java.lang.IllegalStateException

class Board3D  (board: Board) : Board (board.grid, board.steps) {
    val cubeMap = CubeMap (board)

    /**
     *
     */

    override val nextPoint: Point
        get () {
            val newPoint = pos.move (facing)
            if (grid.containsKey (newPoint)) {
                return newPoint
            }

            val current = cubeMap.pointToRegion (pos)
            val adj = current.getAdjacency (facing)!!
            val target = cubeMap.getRegion (adj.region)

            val sides = cubeMap.sides
            var offset = when (facing) {
                Facing.LEFT, Facing.RIGHT -> pos.y % sides
                Facing.UP, Facing.DOWN -> pos.x % sides
            }
            if (! adj.same) {
                offset = sides - offset -1
            }

            val other = when (adj.side) {
                Facing.LEFT ->  Point (target.max.x, target.min.y + offset)
                Facing.RIGHT -> Point (target.min.x, target.min.y + offset)
                Facing.DOWN -> Point (target.min.x + offset, target.min.y)
                Facing.UP -> Point (target.min.x + offset, target.max.y)
            }

            if (grid[other] == Tile.FLOOR) {
                facing = adj.side
            }
            return other
        }

    fun dumpRegions () {
        for (y in 0 .. ys) {
            for (x in 0 .. xs) {
                val point = Point (x, y)
                val tile = grid [point]
                if (tile != null) {
                    print (cubeMap.pointToRegion (point).index)
                } else {
                    print (' ')
                }
            }
            println ()
        }
    }
}

data class Adjacency (val region: Int, val side: Facing, val same: Boolean)

data class Region (val index: Int, val min: Point, val max: Point) {
    val adjacencies = mutableMapOf<Facing, Adjacency> ()

    fun contains (point: Point): Boolean {
        val (x, y) = point
        return x in min.x .. max.x && y in min.y .. max.y
    }
    fun hasAdjacency (dir: Facing): Boolean = adjacencies.containsKey (dir)
    fun getAdjacency (dir: Facing): Adjacency? = adjacencies.get (dir)
    fun setAdjacency (dir: Facing, adjacency: Adjacency) {
        if (index == adjacency.region) {
            throw IllegalStateException ("Can't be adjacent to yourself: $index")
        }
        val already = adjacencies.get (dir)
        if (already != null) {
            if (already != adjacency) {
                println (adjacency)
                println (already)
              throw IllegalStateException ("Adjacency already registered: $dir")
            } else {
                return
            }
        }
        adjacencies.values.forEach {
             if (it.region == adjacency.region) {
                throw IllegalStateException ("Adjacent region already registered: ${it.region}")
             }
        }
        adjacencies[dir] = adjacency
        return
    }

    fun dump () = println (toString ())
    override fun toString (): String {
        return StringBuffer ().apply {
            append ("Region($index, $min-$max)")
            adjacencies.forEach {
                append (" ${it}")
            }
        }.toString ()
    }
}

data class CubeMap (val board: Board) {
    val longest = Math.max (board.xs, board.ys)
    val sides = longest / 4
    val xw = board.xs / sides
    val yw = board.ys / sides
    val regions = mutableListOf<Region> ()
    val populated = mutableListOf<Region?> ().apply {
        var index = 0
        for (y in 0 until yw) {
            for (x in 0 until xw) {
                val min = Point (x * sides, y * sides)
                if (board.grid.containsKey (min)) {
                    val max = min.add (Point(sides - 1, sides - 1))
                    val region = Region (index++, min, max)
                    regions.add (region)
                    add (region)
                } else {
                    add (null)
                }
            }
        }
    }
    val firstPopulated: Point
        get () {
            for (y in 0 until yw) {
                for (x in 0 until xw) {
                    if (isPopulated(x, y)) {
                        return Point (x, y)
                    }
                }
            }
            throw Exception ()
        }

    init {
        fixedAdjacencies()
        analyzeAdjacencies()
        analyzeAdjacencies()
    }

    private fun toPopulatedIndex (x: Int, y: Int): Int = y * xw +x
    fun isPopulated (x: Int, y: Int): Boolean = populated[toPopulatedIndex(x, y)] != null
    fun getRegion (x: Int, y: Int): Region? = populated[toPopulatedIndex(x, y)]
    fun getRegion (index: Int): Region = regions[index]

    fun pointToRegion (point: Point): Region {
        for (region in regions) {
            if (region.contains (point)) {
                return region
            }
        }
        throw Exception ()
    }

    fun dumpRegion (region: Region){
        println ("Region [${region.index}] - ${region.min}-${region.max}")
        region.adjacencies.forEach {
            println (" $it")
        }
        return
    }
    fun dumpRegions () {
        for (region in regions) {
            dumpRegion (region)
        }
        return
    }
    fun dump () = println (toString ())
    override fun toString (): String {
        return StringBuffer ().apply {
            append ("CubeMap dim=($xw x $yw), sides=$sides\n")
            for (y in 0 until yw){
                for (x in 0 until xw) {
                    if (isPopulated (x, y)) {
                        append (getRegion(x, y)?.index)
                    } else {
                        append ('.')
                    }
                }
                append ("\n")
            }
            regions.forEach {
                append ("${it.index}: $it\n")
            }
        }.toString ()
    }

    /**
     * Utility method to add both mirror adjacencies with one call.
     */

    private fun addAdjacency (from: Int, fromFacing: Facing, to: Int, toFacing: Facing, same: Boolean) {
        getRegion (from).setAdjacency (fromFacing, Adjacency (to, toFacing, same))
        getRegion (to).setAdjacency (toFacing.invert, Adjacency (from, fromFacing.invert, same))
        return
    }

    /**
     * Calculates the fixed adjacencies resulting from the abutting regions.
     */

    fun fixedAdjacencies () {
        for (y in 0 until yw) {
            for (x in 0 until xw) {
                getRegion (x, y)?.let { region ->
                    if (x != 0) {
                        getRegion (x - 1, y)?.let { other ->
                            addAdjacency (region.index, Facing.LEFT, other.index, Facing.LEFT, true)
                        }
                    }
                    if (y != 0) {
                        getRegion (x, y - 1)?.let { other ->
                            addAdjacency (region.index, Facing.UP, other.index, Facing.UP, true)
                        }
                    }
                    if (x != xw - 1) {
                        getRegion (x + 1, y)?.let { other ->
                            addAdjacency (region.index, Facing.RIGHT, other.index, Facing.RIGHT, true)
                        }
                    }
                    if (y != yw - 1) {
                        getRegion (x, y + 1)?.let { other ->
                            addAdjacency (region.index, Facing.DOWN, other.index, Facing.DOWN, true)
                        }
                    }
                }
            }
        }
        return
    }

    fun analyzeAdjacencies () {

        // And now we need to derive the rest by walking around the cube

        for (region in regions) {
            Facing.values ().forEach { facing ->
                if (! region.hasAdjacency (facing)) {
                    region.getAdjacency (facing.left)?.let { adjacent ->
                        val otherRegion = getRegion (adjacent.region)
                        val shift = adjacent.side.right
                        val maybe = otherRegion.getAdjacency (shift)
                        if (maybe != null) {
                            val direction = maybe.side.right.invert
                            val same = facing.same (direction)
                            addAdjacency (region.index, facing, maybe.region, direction, same)
                        }
                    }
                }
            }
        }
        return
    }
}


fun main (args: Array<String>) {
    val example = true
    val board = Board3D (loadBoard (example))
    val cubeMap = CubeMap (board)
    println (cubeMap)
    return
}

// EOF