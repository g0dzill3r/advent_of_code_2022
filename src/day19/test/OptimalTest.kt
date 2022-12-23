package day19.test

import day19.*

fun main (args: Array<String>) {
    val example = true
    val blueprints = loadBlueprints(example)
    val blueprint = blueprints [0]
    val factory = Factory (blueprint)
    factory.resources.increment (Material.ORE, 24)



    val maxAffordable = factory.blueprint.maxAffordable (factory.resources)
    println ("maxAffordable(${permutations (maxAffordable).size}): $maxAffordable")
    val canAfford = factory.blueprint.canAfford (factory.resources)
    println ("canAfford(${canAfford.size})=$canAfford")

    canAfford.forEachIndexed { i, it ->
        println ("$i: $it - ${factory.blueprint.isOptimal (it, factory.resources)}")
    }
    return
}

// EOF