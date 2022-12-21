package day16

fun main (args: Array<String>) {
    val example = true
    val volcano = loadVolcano (example)
    volcano.dump ()

    // part1
    val start = "AA"
    val timeLimit = 30
    val res = volcano.simulate (start, timeLimit)
    println (res)

    // part2

    return
}