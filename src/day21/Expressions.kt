package day21

// NOT 9879574614298

fun Jobs.simplify (e: Expression.Calculation): Pair<Expression.Calculation, Boolean> {

    // We only solve expressions expressed as equalities

    if (e.operation != Operation.EQUALS) {
        throw IllegalArgumentException ("Can only solve equality expressions.")
    }

    // Make sure we're working with a number and an expression

    val l = eval (e.left)
    val r = eval (e.right)
    if (l is Expression.Number) {
        if (r is Expression.Calculation) {
            return simplify (Expression.Calculation (r, Operation.EQUALS, l))
        } else {
            throw IllegalArgumentException ("We're not Wolfram alpha; keep it simple.")
        }
    }

    // Exit if we've done all of the reducing that can be done

    if (l is Expression.Symbol) {
        return Pair(e, true)
    }

    // Deal with number on the left

    l as Expression.Calculation
    r as Expression.Number

    if (l.left is Expression.Number) {
        val result = when (l.operation) {
            Operation.PLUS -> {
                r.value - l.left.value
            }
            Operation.MINUS -> {
                l.left.value - r.value
            }
            Operation.DIVIDE -> {
                l.left.value / r.value
            }
            Operation.MULTIPLY -> {
                r.value / l.left.value
            }
            else -> throw Exception ()
        }
        val simpler = Expression.Calculation (l.right, Operation.EQUALS, Expression.Number (result))
        return Pair (simpler, false)
    }

    // Deal with number on the right

    if (l.right is Expression.Number) {
        val result = when (l.operation) {
            Operation.PLUS -> {
                r.value - l.right.value
            }
            Operation.MINUS -> {
                r.value + l.right.value
            }
            Operation.DIVIDE -> {
                r.value * l.right.value
            }
            Operation.MULTIPLY -> {
                r.value / l.right.value
            }
            else -> throw Exception ()
        }
        val simpler = Expression.Calculation (l.left, Operation.EQUALS, Expression.Number (result))
        return Pair (simpler, false)
    }

    return Pair (e, true)
}

fun main (args: Array<String>) {
    val example = false
    val jobs = loadJobs(example)

    // calculate root
    val root = jobs.eval ("root")
    println (root)

    // uncertain humn
    val expr = jobs.map["root"] as Expression.Calculation
    expr.left as Expression.Symbol
    expr.right as Expression.Symbol
    println (jobs.evalInputs (expr.left.symbol))
    println (jobs.evalInputs (expr.right.symbol))

    println (jobs.eval (expr.left))
    println (jobs.eval (expr.right))

    // remove humn
    jobs.map.remove ("humn")
    val left = jobs.eval (expr.left)
    val right = jobs.eval (expr.right)

    // solution
    var solve = Expression.Calculation (left, Operation.EQUALS, right)
    var solved = false
    while (! solved) {
        val result = jobs.simplify (solve)
        println ("\n")
        solve = result.first
        solved = result.second
    }
    println (solve)
    return
}

// EOF
