package day16

import readInput
import java.util.regex.Pattern

data class ValveRec (val name: String, val flowRate: Int, val to: List<String>)

enum class ScenarioState { OPEN, MOVE }
data class Valve (val name: String, val flowRate: Int, val to: MutableList<Valve> = mutableListOf ()) {
    override fun toString (): String {
        return "${name}[${flowRate}] -> ${to.map { it.name }}"
    }
}

class Volcano (config: List<ValveRec>) {
    val valves: MutableMap<String, Valve> = mutableMapOf ()

    fun hasValve (name: String): Boolean = valves[name] != null
    fun valve (name: String): Valve? = valves[name]
    init {
        config.forEach { rec ->
            valves[rec.name] = Valve (rec.name, rec.flowRate)
        }
        config.forEach { rec ->
            val valve = valves[rec.name] as Valve
            rec.to.forEach {
                valve.to.add (valves[it] as Valve)
            }
        }
    }

    fun dump () {
        println (toString ())
        return
    }

    override fun toString() : String {
        val buf = StringBuffer ()
        valves.values.forEach { valve ->
            buf.append ("${valve}\n")
        }
        return buf.toString ()
    }

    data class Scenario (var current: Valve, var state: ScenarioState = ScenarioState.OPEN, val visited: MutableList<Valve> = mutableListOf (current), val open: MutableList<Valve> = mutableListOf (), var released: Int = 0): Cloneable {
        override fun clone (): Scenario {
            val visited = mutableListOf<Valve> ().apply { addAll (visited) }
            val open = mutableListOf<Valve> ().apply { addAll (open) }
            return Scenario (current, state, visited, open, released)
        }
        fun move (valve: Valve) {
            visited.add (valve)
            current = valve
            state = ScenarioState.OPEN
            return
        }
        fun open (valve: Valve) {
            open.add (valve)
            state = ScenarioState.MOVE
            return
        }

        override fun toString(): String = "Scenario(current=${current.name}, state=${open}, visited=${visited.map {it.name}}, open=${open.map {it.name}}, release=${released}"

        fun replicate (count: Int): List<Scenario> {
            val list = mutableListOf<Scenario> ()
            for (i in 0 until count) {
                if (i == 0) {
                    list.add (this)
                } else {
                    list.add (this.clone ())
                }
            }
            return list
        }

        fun tick (valves: Int, scenarios: MutableList<Scenario>): List<Scenario>? {

            // Tally up the gas released by the presently open valves

            released += open.fold (0) { acc, valve -> acc + valve.flowRate }

            // See if we should open the current valve or attempt to move to an alternate valve

            if (state == ScenarioState.OPEN && current.flowRate > 0) {
                open (current)
            } else if (visited.size < valves){
                val options = mutableListOf<Valve> ().apply {
                    addAll (current.to)
                }
//                options.removeAll (visited)
//                options.removeAll { it.flowRate == 0 }
                if (options.isNotEmpty ()) {
                    val replicas = replicate (options.size)
                    for (i in options.indices) {
                        replicas[i].move (options[i])
                    }
                    replicas.subList (1, replicas.size)
                    return replicas
                }
            }

            return null
        }
    }

    /**
     * Used to optimize the release of pressure
     */
    fun simulate (name: String, timeLimit: Int): List<Scenario> {
        val scenarios = mutableListOf<Scenario> ().apply {
            val valve = valves[name] as Valve
            add (Scenario (valve))
        }
        println (scenarios)
        repeat (timeLimit) {
            println ("==== ${it} ====")
            val additions = mutableListOf<Scenario> ()
            scenarios.forEachIndexed { i, scenario ->
                val additional = scenario.tick (valves.size, scenarios)
                if (additional != null) {
                    additions.addAll (additional)
                }
                println ("$i: $scenario")
            }
            scenarios.addAll (additions)
            Thread.sleep (1000L)
        }
        return scenarios
    }

    /**
     * The simulation logic for part1
     */
    fun simulate1 (name: String, timeLimit: Int): Int {
        var current = valves[name] as Valve
        var state = ScenarioState.OPEN
        val open = mutableListOf<Valve> ()
        val visited = mutableListOf<Valve> (current)
        var released = 0

        repeat (timeLimit) {
            println ("${it+1}: current=${current.name}, state=${state}, visited=${visited.map { it.name }}, open=${open.map { it.name }}, released=$released")
            released += open.fold (0) { acc, valve ->
                println ("  RELEASE ${valve.flowRate} from ${valve.name}")
                acc + valve.flowRate
            }
            if (state == ScenarioState.OPEN && current.flowRate > 0) {
                println ("  OPEN ${current.name}")
                open.add (current)
                state = ScenarioState.MOVE
            } else {
                val options = mutableListOf<Valve> ().apply {
                    addAll (current.to)
                    removeAll (visited)
                    removeAll {
                        it.flowRate == 0
                    }
                }
                if (options.isNotEmpty()) {
                    val target = options[0]
                    println ("  MOVE ${target.name}")
                    current = target
                    visited.add (target)
                    state = ScenarioState.OPEN
                }
            }
            println ("  RELEASED=$released")
        }

        return released
    }

    companion object {
        private val REGEX = "^Valve ([A-Z]{2}) has flow rate=(-?\\d+); tunnels? leads? to valves? ([A-Z, ]+)$"
        private val MATCHER = Pattern.compile (REGEX)
        fun parse (input: String): Volcano = parse (input.trim ().split ("\n"))
        fun parse (rows: List<String>): Volcano {
            val config = rows.map { row ->
                val matcher = MATCHER.matcher (row)
                if (! matcher.matches ()) {
                    throw IllegalArgumentException ("Malformed input: $row")
                }
                val from = matcher.group (1)
                val flowRate = matcher.group (2).toInt ()
                val to = matcher.group (3).split (", ")
                ValveRec (from, flowRate, to)
            }
            return Volcano (config)
        }
    }
}

fun loadVolcano (example: Boolean): Volcano {
    val input = readInput (16, example)
    return Volcano.parse (input)
}

fun main (args: Array<String>) {
    val example = true
    val volcano = loadVolcano (example)
    println (volcano)
    return
}

// EOF