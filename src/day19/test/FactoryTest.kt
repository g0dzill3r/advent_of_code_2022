package day19.test

import day19.Factory
import day19.loadBlueprints

fun main (args: Array<String>) {
    val example = true
    val blueprints = loadBlueprints(example)
    val factory = Factory (blueprints[0])
    var factories = mutableListOf (factory)

    repeat(23) {time ->
        println ("$time: factories ${factories.size}")
        val newFactories = mutableListOf<Factory> ()
        factories.forEach {
            it.tick (newFactories)
        }
        println ("$time: ${newFactories.size}")

        // We need to be smart and not add any new factories that are
        // noticeably behind and of the existing factories

//        newFactories.forEach {
//            if ()
//        }
        factories.addAll (newFactories)
        factories.sortWith { a, b ->
                a.robotScore.compareTo (b.robotScore)
        }
        if (factories.size > 100) {
            factories = factories.subList (factories.size - 100, factories.size)
        }
    }

    factories.forEach {
        println (it)
    }

    val maxCracked = factories.fold (0) { acc, f -> Math.max (f.cracked, acc) }
    println (maxCracked)

    return
}

// EOF