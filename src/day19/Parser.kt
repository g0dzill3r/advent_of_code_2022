package day19

import readInput
import java.lang.IllegalArgumentException
import java.util.regex.Pattern


class Resources {
    private val data = mutableMapOf<Material, Int> ()
    fun get (material: Material): Int = data[material] ?: 0
    fun set (material: Material, count: Int) {
        data[material] = count
    }
    fun clear () {
        data.clear ()
    }
    fun increment (material: Material, count: Int): Int {
        return (get (material) + count).apply {
            set (material, this)
        }
    }
    fun decrement (material: Material, count: Int): Int {
        return (get (material) - count).apply {
            set (material, this)
        }
    }

    fun canAfford (pairs: List<Pair<Material, Int>>): Boolean {
        pairs.forEach { (material, count) ->
            if (count > get (material)) {
                return false
            }
        }
        return true
    }

    fun dump () = println (toString ())

    fun addAll (resources: Resources) {
        data.putAll (resources.data)
    }
    fun copy (): Resources {
        val other = Resources ()
        other.data.putAll (data)
        return other
    }
    fun spend (pairs: List<Cost>, count: Int = 1) {
        for ((material, cost) in pairs) {
            decrement (material, count * cost)
        }
    }
    override fun toString(): String = data.entries.toString ()
}

enum class Material (val encoded: String, val rank: Int) {
    ORE ("ore", 0),
    CLAY ("clay", 1),
    OBSIDIAN ("obsidian", 2),
    GEODE ("geode", 3);

    companion object {
        private val map = mutableMapOf<String, Material> ().apply {
            Material.values ().forEach {
                put (it.encoded, it)
            }
        }
        fun decode (encoded: String): Material = map [encoded] ?: throw IllegalArgumentException ("Invalid material: $encoded")
    }
}

data class Robots (val material: Material, val count: Int) {
    override fun toString(): String = "${count} $material"
}
data class Cost (val material: Material, val count: Int) {
    override fun toString(): String = "${count} $material"
}
data class Recipe (val robot: Material, val cost: List<Cost>) {
    fun dump () = println (toString ())

    fun maxAffordable (resources: Resources): Int {
        var count: Int? = null
        for (el in cost) {
            val canAfford = resources.get (el.material) / el.count
            if (count == null) {
                count = canAfford
            } else {
                count = Math.min (count, canAfford)
            }
        }
        return count as Int
    }

    override fun toString(): String = "${robot} = ${cost.joinToString (", ")}"
}
data class Blueprint (val index: Int, val recipes: List<Recipe>) {
    val robots = mutableMapOf<Material, Recipe> ().apply {
        recipes.forEach {
            put (it.robot, it)
        }
    }

    /**
     * Calculate the maximum number of each robot type that we could afford with the
     * given amount of resources.
     */

    fun maxAffordable (resources: Resources): List<Robots> {
        return recipes.map {
            Robots (it.robot, it.maxAffordable (resources))
        }.filter { it.count != 0 }
    }

    /**
     * Used to scrub the list of possible affordable permutations to remove
     * any of those that doesn't fully spend the budget since those are by definition
     * suboptimal strategies.
     */
    fun isOptimal (pairs: List<Robots>, resources: Resources): Boolean {

        // Calculate what resources we'll have left after building the specified robots

        val leftover = resources.copy ()
        for (pair in pairs) {
            val recipe = robots[pair.material] as Recipe
            val count = pair.count
            leftover.spend (recipe.cost, count)
        }

        // Now see if we can still afford to build any robots

        for (recipe in recipes) {
            if (recipe.maxAffordable (leftover) > 0) {
                return false
            }
        }
        return true
    }

    /**
     * Tests to see whether we can actually afford to purchase this many robots
     * with the specified resources.
     */
    private fun canAfford (pairs: List<Robots>, resources: Resources): Boolean {
        val copy = resources.copy ()
        for ((robot, count) in pairs) {
            val recipe = robots[robot] as Recipe
            recipe.cost.forEach { (material, cost) ->
                if (copy.decrement (material, count * cost) < 0) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * Figure out all the possible set of robots that could be provisioned using
     * the given amount of resources.
     */

    fun canAfford (resources: Resources): List<List<Robots>> {
        val affordable = maxAffordable (resources)
        val permutations = permutations (affordable)
            .filter { canAfford (it, resources) }
            .filter { isOptimal (it, resources) }

        return permutations
    }

    fun dump () = println (toString ())
    override fun toString(): String {
        val buf = StringBuffer ()
        buf.append ("Blueprint $index:\n")
        recipes.forEach {
            buf.append ("  ${it}\n")
        }
        return buf.toString ()
    }
    companion object {
        private val REGEX0 = "^Blueprint (\\d+):$"
        private val PATTERN0 = Pattern.compile (REGEX0)
        private val REGEX1 = "^\\s*(\\w+) robot costs (\\d+) (\\w+)(?: and (\\d++) (\\w+))?.$"
        private val PATTERN1 = Pattern.compile (REGEX1)
        fun parse (str: String): Blueprint {
            val strs = str.split (" Each ")

            val p0 = PATTERN0.matcher (strs[0])
            if (! p0.matches ()) {
                throw IllegalArgumentException ("No match: ${strs[0]}")
            }
            val index = p0.group (1).toInt ()
            val recipes = mutableListOf<Recipe> ()

            for (i in 1 until strs.size) {
                val p = PATTERN1.matcher (strs[i])
                if (! p.matches ()) {
                    throw IllegalArgumentException ("No match: ${strs[i]}")
                }
                val robot = p.group (1)
                val costs = mutableListOf<Cost> ().apply {
                    add (Cost (Material.decode (p.group (3)), p.group (2).toInt ()))
                    if (p.group (4) != null) {
                        add (Cost (Material.decode (p.group (5)), p.group (4).toInt ()))
                    }
                }
                recipes.add (Recipe (Material.decode (robot), costs))
            }

            return Blueprint (index, recipes)
        }
    }
}


fun loadBlueprints (example: Boolean): List<Blueprint> {
    val input = readInput (19, example)
    val blueprints = input.trim ().split ("\n").map {
        Blueprint.parse (it)
    }
    return blueprints
}

fun main (args: Array<String>) {
    val example = true
    val blueprints = loadBlueprints(example)
    blueprints.forEach {
        println (it)
    }
    return
}

// EOF