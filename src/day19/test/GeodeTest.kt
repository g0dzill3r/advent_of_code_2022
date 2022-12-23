package day19.test

import day19.Factory
import day19.Material
import day19.loadBlueprints

fun main (args: Array<String>) {
    val example = true
    val blueprints = loadBlueprints (example)
    val factory = Factory (blueprints [0])
    factory.robots.apply {
        clear ()
        put (Material.GEODE, 2)
    }
    var factories = mutableListOf (factory)

    repeat(24) {time ->
        println ("$time: factories ${factories.size}")
        val newFactories = mutableListOf<Factory> ()
        factories.forEach {
            it.tick (newFactories)
        }
        println ("$time: ${newFactories.size}")
    }

    factories.forEach {
        println (it)
    }

    val maxCracked = factories.fold (0) { acc, f -> Math.max (f.cracked, acc) }
    println (maxCracked)

    return
}
// EOF