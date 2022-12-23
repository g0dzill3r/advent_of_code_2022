package day19.test

import day19.Material
import day19.Resources
import day19.loadBlueprints

fun main (args: Array<String>) {
    val example = true
    val blueprints = loadBlueprints(example)
    val blueprint = blueprints[0]

    val resources = Resources ().apply {
        set(Material.ORE, 21)
        set(Material.CLAY, 7)
        set(Material.OBSIDIAN, 9)
    }

    println ("RESOURCES: $resources")
    blueprint.recipes.forEach {
        println ("$it -> AFFORD ${it.maxAffordable(resources)}")
    }
    println (blueprint.maxAffordable (resources))

    return
}

// EOF