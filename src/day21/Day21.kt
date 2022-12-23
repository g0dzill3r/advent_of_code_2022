package day21

val ROOT = "root"
val HUMN = "humn"

fun main (args: Array<String>) {
    val example = false

    // part1
    loadJobs (example).let { jobs ->
        val eval = jobs.eval ()
        println ("part1=$eval")
    }

    // part2
    loadJobs (example).let { jobs ->
        val root = jobs.root
        jobs.map [ROOT] = Expression.Calculation (root.left, Operation.EQUALS, root.right)

        // remove humn
        jobs.map.remove ("humn")
        val expr = jobs.root
        val left = jobs.eval (expr.left)
        val right = jobs.eval (expr.right)

        // solution
        var solve = Expression.Calculation (left, Operation.EQUALS, right)
        var solved = false
        while (! solved) {
            val result = jobs.simplify (solve)
            solve = result.first
            solved = result.second
        }
        println ("part2=${solve}")
    }

    return
}







// EOF