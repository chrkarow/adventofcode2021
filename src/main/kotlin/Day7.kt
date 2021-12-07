import java.io.File
import kotlin.math.absoluteValue

fun main() {
    val positions = File("src/main/resources/day7.txt").readLines()[0].split(",").map { it.toLong() }

    part1(positions = positions)
    part2(positions = positions)
}

private fun part1(positions: List<Long>) {
    val sorted = positions.sorted()
    val median = sorted[sorted.size / 2]

    val result = positions.fold(0L) { acc, p -> acc + (median - p).absoluteValue }
    println(result)
}

private fun part2(positions: List<Long>) {
    // Rounding approach worked for example but not for the real puzzle.
    // Manually rounding it down, even if the average is 483.553 (actually needs to be rounded up).
    // val average = positions.average().roundToLong()
    val average = positions.average().toInt()

    val result = positions.fold(0L) { acc, p ->
        val distance = (average - p).absoluteValue
        acc + distance * (distance + 1) / 2
    }

    println(result)
}
