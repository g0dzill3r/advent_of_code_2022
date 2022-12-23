package day19

import kotlin.math.sign


data class Factory (val blueprint: Blueprint) {
    var time: Int = 0
        private set
    var producing = mutableMapOf<Material, Int> ()
    val robots = mutableMapOf<Material, Int> ().apply {
        Material.values ().forEach {
            val count = if (it == Material.ORE) 1 else 0
            put (it, count)
        }
    }
    val resources = Resources ()
    var cracked = 0

    /**
     * Make a copy of this factory by value.
     */
    fun clone (): Factory {
        val f = Factory (blueprint)
        f.time = time
        f.robots.clear ()
        f.robots.putAll (robots)
        f.resources.addAll (resources)
        f.cracked = cracked
        return f
    }

    /**
     *
     */
    fun tick (addl: MutableList<Factory>) {

        // Put the robots that we have to work

        robots.forEach { (material, count) ->
            when (material) {
                Material.CLAY, Material.ORE, Material.OBSIDIAN -> {
                    resources.increment (material, count)
                }
                Material.GEODE -> {
                    cracked += count
                }
            }
        }

        // Add any completed robots to the inventory

        producing.entries.forEach { (material, count) ->
            robots.add (material, count)
        }
        producing.entries.clear ()

        // Figure out what possible permutations of robots we're going to build

        val scenarios = blueprint.canAfford (resources)
        if (scenarios.isNotEmpty ()) {
            scenarios.forEachIndexed { i, scenario ->
                val clone = clone ()
                scenario.forEach {(robot, count) ->
                    clone.producing[robot] = count
                    clone.resources.spend (blueprint.robots[robot]!!.cost)
                }
                addl.add (clone)
            }
        }

        time ++
        return
    }

    private fun resourceScore (material: Material): Int {
        return resources.get (material)
    }
    val resourceScore: Score
        get () = Score (listOf(
            cracked,
            resourceScore (Material.OBSIDIAN),
            resourceScore (Material.CLAY),
            resourceScore (Material.ORE)
        ))

    private fun robotScore (material: Material): Int {
        return (robots[material] ?: 0) + (producing[material] ?: 0)
    }

    val robotScore: Score
        get () = Score (listOf (
            robotScore (Material.GEODE),
            robotScore (Material.OBSIDIAN),
            robotScore (Material.CLAY),
            robotScore (Material.ORE)
        ))

    fun compareTo (other: Factory): Int {
        val a = robotScore.compareTo (other.robotScore)
        val b = resourceScore.compareTo (other.resourceScore)
        return if (a == b) {
            a
        } else {
            0
        }
    }

    fun dump () = println (toString ())

    override fun toString (): String {
        return StringBuffer ().apply {
            append ("Factory (id=${blueprint.index}, time=${time}, producing=${producing ?: "NONE"})\n")
            Material.values ().forEach { material ->
                val resources = resources.get (material)
                val robots = robots[material] ?: 0
                append (" - $material: ${robots} robots, ${resources} resources \n")
            }
            append ("Max Affordable: ${blueprint.maxAffordable (resources)}\n")
            append ("Producing: ${producing.entries}\n")
            append ("Geodes: $cracked")
        }.toString ()
    }
    companion object {

    }
}


fun main (args: Array<String>) {
    val example = true
    val blueprints = loadBlueprints(example)

    val factories = blueprints.map { Factory (it) }
    factories.forEach {
        println(it)
    }
    return
}