package day9

import readInput
import kotlin.math.sign


class Visits {
    val visits = mutableMapOf<String, Int> ()

    fun visit (point: Point) {
        visits.increment (point.toString ())
        return
    }

    fun dump () {
        println (visits)
    }
}

fun main (args: Array<String>) {
    val example = false
    val input = readInput (9, example)
    val steps = Step.parseSteps (input)

    // part1
    block {
        val start = Point ()
        val head = start.clone ()
        val tail = start.clone ()
        val visits = Visits ()
        visits.visit (tail)

        steps.forEach { step ->
            step.count.repeat {
                head.move (step.direction)
                if (tail.follow (head)) {
                    visits.visit (tail)
                }
            }
        }
        println (visits.visits.keys.count())
    }

    // part2
    block {
        val start = Point ()
        val knotCount = 10
        val knots = mutableListOf<Point> ().apply {
            knotCount.repeat {
                add (start.clone ())
            }
        }

        val visits = Visits ()
        visits.visit (knots.last ())

        steps.forEach { step ->
            step.count.repeat {
                knots.first ().move (step.direction)
                for (i in 1 until knots.size) {
                    if (knots[i].follow (knots[i-1])) {
                        if (i == knots.size - 1) {
                            visits.visit (knots[i])
                        }
                    }
                }
            }
        }
        println (visits.visits.keys.count())
    }
    return
}

// EOF