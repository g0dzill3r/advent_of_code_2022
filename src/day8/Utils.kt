package day8

fun <T> List<List<T>>.dump () {
    forEach {
        it.forEach {
            print ("${it ?: "-"} ")
        }
        println ();
    }
}

// EOF