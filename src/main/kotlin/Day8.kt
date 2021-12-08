import java.io.File

fun main() {
    val segments = File("src/main/resources/day8.txt").readLines()
        .map { Pair(it.split(" | ")[0].split(" "), it.split(" | ")[1].split(" ")) }

    part1(segments = segments)
    part2(segments = segments)
}

private fun part1(segments: List<Pair<List<String>, List<String>>>) {
    val result = segments.flatMap { it.second }.count { listOf(2, 4, 3, 7).contains(it.length) }
    println(result)
}

private fun part2(segments: List<Pair<List<String>, List<String>>>) {
    var sum = 0

    segments.forEach {
        val array = Array(7) { setOf('a', 'b', 'c', 'd', 'e', 'f', 'g') }

        it.first.forEach { signal ->
            val charSet = signal.toCharArray().toSet()
            when (signal.length) {
                2 -> {
                    array[0] = array[0].intersect(charSet)
                    array[1] = array[1].intersect(charSet)
                    array[2] = array[2].minus(charSet)
                    array[3] = array[3].minus(charSet)
                    array[4] = array[4].minus(charSet)
                    array[5] = array[5].minus(charSet)
                    array[6] = array[6].minus(charSet)
                }
                3 -> {
                    array[0] = array[0].intersect(charSet)
                    array[1] = array[1].intersect(charSet)
                    array[2] = array[2].minus(charSet)
                    array[3] = array[3].minus(charSet)
                    array[4] = array[4].minus(charSet)
                    array[5] = array[5].intersect(charSet)
                    array[6] = array[6].minus(charSet)

                }
                4 -> {
                    array[0] = array[0].intersect(charSet)
                    array[1] = array[1].intersect(charSet)
                    array[2] = array[2].minus(charSet)
                    array[3] = array[3].minus(charSet)
                    array[4] = array[4].intersect(charSet)
                    array[5] = array[5].minus(charSet)
                    array[6] = array[6].intersect(charSet)
                }
                5 -> {
                    array[2] = array[2].intersect(charSet)
                    array[5] = array[5].intersect(charSet)
                    array[6] = array[6].intersect(charSet)
                }
                6 -> {
                    array[1] = array[1].intersect(charSet)
                    array[2] = array[2].intersect(charSet)
                    array[4] = array[4].intersect(charSet)
                    array[5] = array[5].intersect(charSet)
                }
                7 -> {
                    array[0] = array[0].intersect(charSet)
                    array[1] = array[1].intersect(charSet)
                    array[2] = array[2].intersect(charSet)
                    array[3] = array[3].intersect(charSet)
                    array[4] = array[4].intersect(charSet)
                    array[5] = array[5].intersect(charSet)
                    array[6] = array[6].intersect(charSet)
                }
            }
        }

        val result = array.mapIndexed { i, set ->
            if (set.size > 1) {
                var x = set
                array.forEachIndexed { j, other ->
                    if (j != i) {
                        x = x.minus(other)
                    }
                }
                x
            } else set
        }

        val patterns = mapOf(
            setOf(result[0].first(), result[1].first()) to 1,
            setOf(result[5].first(), result[0].first(), result[6].first(), result[3].first(), result[2].first()) to 2,
            setOf(result[5].first(), result[0].first(), result[6].first(), result[1].first(), result[2].first()) to 3,
            setOf(result[0].first(), result[1].first(), result[6].first(), result[4].first()) to 4,
            setOf(result[1].first(), result[2].first(), result[6].first(), result[4].first(), result[5].first()) to 5,
            setOf(result[1].first(), result[2].first(), result[3].first(), result[4].first(), result[5].first(), result[6].first()) to 6,
            setOf(result[0].first(), result[1].first(), result[5].first()) to 7,
            setOf(result[0].first(), result[1].first(), result[2].first(), result[3].first(), result[4].first(), result[5].first(), result[6].first()) to 8,
            setOf(result[0].first(), result[1].first(), result[2].first(), result[4].first(), result[5].first(), result[6].first()) to 9,
            setOf(result[0].first(), result[1].first(), result[2].first(), result[3].first(), result[4].first(), result[5].first()) to 0,
        )

        sum += it.second.map { pattern -> patterns[pattern.toCharArray().toSet()] }.joinToString(separator = "").toInt()
    }

    println(sum)
}
