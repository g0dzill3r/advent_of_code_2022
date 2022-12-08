package day8

/**
 *
 */

data class TreeGrid (val data: List<List<Int>>) {
    val cols: Int  = data[0].size
    val rows: Int = data.size
    fun height (x: Int, y: Int): Int = data[y][x]
    fun row (y: Int): List<Int> = data[y]
    fun col (x: Int): List<Int> = data.map { it [x] }
    val trees: Int
        get () = cols * rows

    fun visit (func: TreeGrid.(x: Int, y: Int) -> Unit) {
        for (x in 0 until cols) {
            for (y in 0 until rows) {
                this.func (x, y)
            }
        }
        return
    }

    fun assess (x: Int, y: Int): List<List<Int?>> {
        return data.mapIndexed { iy, row  ->
            row.mapIndexed { ix, height  ->
                if (x == ix || y == iy) {
                    height
                } else {
                    null
                }
            }
        }
    }

    fun dump () {
        data.dump ()
        return
    }

    fun visibleMatrix (): List<List<Boolean>> {
        return data.mapIndexed { y, row ->
            row.mapIndexed { x, _ ->
                isVisible (x, y)
            }
        }
    }

    fun dumpVisible () {
        visibleMatrix().forEach {
            it.forEach {
                if (it) {
                    print ("* ")
                } else {
                    print (". ")
                }
            }
            println ()
        }
    }

    val visible: Int
        get () {
            var total = 0
            visit { x, y ->
                if (isVisible (x, y)) {
                    total ++
                }
            }
            return total
        }

    val maxScenicScore: Int
        get () {
            val scores = mutableListOf<Int> ()
            visit { x, y, ->
                scores.add (getScenicScore(x, y))
            }
            return scores.maxOf { it }
        }

    fun getViews (x: Int, y: Int): List<List<Int>> {
        val xs = row (y)
        val ys = col (x)
        return listOf (
            xs.subList(0, x).reversed (),
            xs.subList(x + 1, xs.size),
            ys.subList(0, y).reversed (),
            ys.subList(y + 1, ys.size)
        )
    }

    fun getViewingDistance (height: Int, view: List<Int>): Int {
        var distance = 0
        for (tree in view) {
            distance ++
            if (tree >= height) {
                break
            }
        }
        return distance
    }

    fun getScenicScores (x: Int, y: Int): List<Int> {
        val result = mutableListOf<Int> ()
        val height = height (x, y)
        getViews (x, y).forEach {
            val score = getViewingDistance(height, it)
            result.add (score)
        }
        return result
    }

    fun getScenicScore (x: Int, y: Int): Int {
        var result = 1
        getScenicScores(x, y).forEach {
            result *= it
        }
        return result
    }

    fun isVisible (x: Int, y: Int): Boolean {
        if (x == 0 || x == cols - 1 || y == 0 || y == rows - 1) {
            return true
        }

        val height = height (x, y)
        var visible = false
        getViews(x, y).forEach {
            if (height > it.maxOf { it }) {
                visible = true
            }
        }
        return visible
    }

    companion object {
        fun loadGrid (str: String): TreeGrid {
            val split = str.split ("\n")
            val rows = ArrayList<List<Int>> (split.size)
            split.forEach {
                val row = it.map { it.digitToInt() }
                rows.add (row)
            }
            return TreeGrid (rows)
        }
    }
}

// EOF