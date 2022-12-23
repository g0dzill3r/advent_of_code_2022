package day19

val TIMELIMIT = 24

fun main (args: Array<String>) {
    val example = true
    val blueprints = loadBlueprints(example)

    // part1
    val results = blueprints.map { blueprint ->
        val factories = mutableListOf (Factory (blueprint))
        repeat (TIMELIMIT) {
            val addl = mutableListOf<Factory> ()
            factories.forEach {
                it.tick (addl)
            }
            factories.addAll (addl)
        }

        val maxGeodes = 0
        Pair (blueprint.index, maxGeodes)
    }
    results.forEach { (index, geodes) ->
        println("blueprint $index: $geodes geodes")
    }
    val quality = results.fold (0) { acc, (index, geodes) -> acc + index * geodes }
    println ("part1=$quality")


    // part2
    return
}

// EOF