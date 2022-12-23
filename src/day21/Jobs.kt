package day21

import readInput

data class Jobs (val map: MutableMap<String, Expression>) {
    fun evalInputs (symbol: String = ROOT, set: MutableSet<String> = mutableSetOf<String> ()): Set<String> {
        return evalInputs (map[symbol] as Expression, set)
    }

    fun evalInputs (expr: Expression, set: MutableSet<String>): Set<String> {
        when (expr) {
            is Expression.Number -> Unit
            is Expression.Symbol -> {
                set.add (expr.symbol)
                evalInputs (expr.symbol, set)
            }
            is Expression.Calculation -> {
                set.add ((expr.left as Expression.Symbol).symbol)
                set.add ((expr.right as Expression.Symbol).symbol)
                evalInputs (expr.left, set)
                evalInputs (expr.right, set)
            }
        }
        return set
    }

    val root: Expression.Calculation
        get () = map[ROOT] as Expression.Calculation
    fun set (symbol: String, value: Long) = map.put (symbol, Expression.Number (value))

    fun eval (symbol: String = ROOT): Expression {
        val expr = map[symbol]
        return expr?.let {
            eval (it)
        } ?: Expression.Symbol (symbol)
    }

    fun eval (expr: Expression): Expression {
        return when (expr) {
            is Expression.Number -> {
                expr
            }
            is Expression.Symbol -> {
                eval (expr.symbol)
            }
            is Expression.Calculation -> {
                val left = eval (expr.left)
                val right = eval (expr.right)
                if (left is Expression.Number && right is Expression.Number) {
                    Expression.Number (expr.invoke (left.value, right.value))
                } else {
                    Expression.Calculation (left, expr.operation, right)
                }
            }
        }
    }

    fun dump () = println (toString ())
    override fun toString(): String {
        return StringBuffer ().apply {
            map.forEach { (key, value) ->
                append ("$key: $value\n")
            }
        }.toString ()
    }

    companion object {
        val ROOT = "root"
    }
}

fun loadJobs (example: Boolean): Jobs {
    val input = readInput (21, example)
    val map = mutableMapOf<String, Expression> ()
    input.trim ().split ("\n").map { Expression.parse (it, map) }
    return Jobs (map)
}

fun main (args: Array<String>) {
    val example = true
    val jobs = loadJobs (example)
    jobs.dump ()

    val symbols = jobs.evalInputs("root")
    println (symbols)
    return
}

// EOF