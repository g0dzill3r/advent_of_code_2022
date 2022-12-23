package day19.test

import day19.Material
import day19.Robots
import day19.loadBlueprints
import day19.permutations

fun main (args: Array<String>) {
    val example = true
    val blueprints = loadBlueprints(example)
    val blueprint = blueprints[0]
    val affordable = listOf<Robots> (
//        Material.ORE to 4,
        Robots (Material.CLAY, 2),
        Robots (Material.OBSIDIAN, 2),
//        Material.GEODE to 3
    )

    println ("Affordable: $affordable")

    val permutations = permutations (affordable)
    permutations.forEachIndexed { i, el ->
        println ("$i: $el")
    }

    return
}



// EOF