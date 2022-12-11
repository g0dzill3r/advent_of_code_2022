package day11





fun main (args: Array<String>) {
    val example = false

    // part1
    getMonkeys (example).let { monkeys ->
        repeat (20) {
            monkeys.playRound {
                val extra = it.mod (3.toBigInteger ())
                it.minus (extra).divide (3.toBigInteger ())
            }
        }
        val result = monkeys.monkeys.map { it.inspections }
            .sortedDescending()
            .subList(0, 2)
            .fold (1) {
                acc, v -> acc * v
            }
        println (result)
    }

    // part2
    getMonkeys (example).let { monkeys ->
        repeat (10_000) {
            monkeys.playRound {
                it
            }
        }
        monkeys.dump ()
        val result = monkeys.monkeys.map { it.inspections.toBigInteger() }
            .sortedDescending()
            .subList(0, 2)
            .fold (1.toBigInteger()) {
                    acc, v -> acc * v
            }
        println (result)
    }
    return
}

// EOF