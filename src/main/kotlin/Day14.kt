import java.io.File

fun main() {
    val lines = File("src/main/resources/day14.txt").readLines()

    val template = lines[0]

    val rules = lines.drop(2)
        .map { it.split(" -> ") }
        .associate { it[0] to it[1] }

    part1(
        template = template,
        rules = rules
    )

    part2(
        template = template,
        rules = rules
    )
}

/** Naive implementation - this explodes very quickly **/
private fun part1(template: String, rules: Map<String, String>) {
    val result = (1..10).fold(template) { acc, _ ->
        acc
            .windowed(2)
            .fold("${acc.first()}") { buildUp, pair ->
                buildUp + rules[pair] + pair.last()
            }
    }

    val frequencies = result
        .groupingBy { it }
        .eachCount()
        .values
        .sortedDescending()

    println(frequencies.first() - frequencies.last())
}

/** Optimized implementation **/
private fun part2(template: String, rules: Map<String, String>) {

    val initialPairs = template.windowed(2).groupingBy { it }.eachCount().entries.associate { it.key to it.value.toLong() }
    val mapped = (1..40).fold(initialPairs) { currentStep, _ -> polymerize(pairs = currentStep, rules = rules) }

    val frequencies = mapped.entries
        .flatMap { listOf(it.key.first() to it.value, it.key.last() to it.value) }
        .groupingBy { it.first }
        .aggregate { _, acc: Long?, elem, first -> if (first) elem.second else acc!! + elem.second }
        // As the character pairs overlap, we have to divide the value by 2.
        // Only problem is the first and the last character of the string, which don't have an overlapping pair
        // so we have to add 1 to the value and can afterwards divide it by 2.
        .map {
            var newValue = it.value
            if (it.key == template.first()) newValue++
            if (it.key == template.last()) newValue++
            newValue / 2
        }.sortedDescending()

    println(frequencies.first() - frequencies.last())
}

private fun polymerize(pairs: Map<String, Long>, rules: Map<String, String>) =
    pairs
        .flatMap {
            rules[it.key]?.let { insert ->
                listOf(
                    "${it.key.first()}$insert" to it.value,
                    "$insert${it.key.last()}" to it.value
                )
            } ?: listOf(it.key to it.value)
        }.groupingBy { it.first }
        .aggregate { _, acc: Long?, elem, first -> if (first) elem.second else acc!! + elem.second }

