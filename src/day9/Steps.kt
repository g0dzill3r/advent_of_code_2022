package day9

enum class Direction (val dx: Int = 0, val dy: Int = 0) {
    UP (dy = 1),
    DOWN (dy = -1),
    LEFT (dx = -1),
    RIGHT (dx = 1);

    companion object {
        private val map = mutableMapOf<Char, Direction>().apply {
            Direction.values().forEach {
                put (it.name[0], it)
            }
        }

        fun decode (str: String): Direction = decode (str[0])
        fun decode (c: Char): Direction = map[c] as Direction
    }
}

data class Step (val direction: Direction, val count: Int) {
    companion object {
        fun parseSteps (str: String): List<Step> {
            return str.split ("\n").map {
                val pair = it.split (" ")
                Step (Direction.decode (pair[0]), pair[1].toInt())
            }
        }
    }
}


// EOF
