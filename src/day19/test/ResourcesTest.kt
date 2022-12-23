package day19.test

import day19.Material
import day19.Resources

fun main (args: Array<String>) {
    val purse= listOf (
        Pair (Material.CLAY, 1),
        Pair (Material.OBSIDIAN, 12),
        Pair (Material.ORE, 4),
    )
    val r = Resources ()
    val test = {
        println (r)
        println ("CAN AFFORD ${r.canAfford(purse)}")
    }

    r.set (Material.CLAY, 1)
    test ()

    r.set (Material.OBSIDIAN, 10)
    test ()

    r.set (Material.ORE, 4)
    test ()

    r.increment(Material.OBSIDIAN, 5)
    test ()


    return
}

// EOF