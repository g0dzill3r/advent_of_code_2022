package day21

fun <T, S> Map<T, S>.extract (vararg keys: T, notFound: (T) -> S? = { t -> null }): List<S?> {
    return keys.map {
        get (it) ?: notFound (it)
    }
}

fun main (args: Array<String>) {
    val map = mapOf (
        "name" to "Lee Crawford",
        "favoriteColor" to "green",
        "pronouns" to "thee/thou",
        "dog" to "Stevie",
        "car" to "Volkswagen"
    )

    val (name, dog, car) = map.extract ("name", "dog", "car", "Fred") {
        throw Exception ("Missing key: $it")
    }
    return
}
