import java.io.File

fun main() {
    val lines = File("src/main/resources/day1.txt").readLines().map { it.toInt() }

    part1(measurements = lines)
    part2(measurements = lines)
}

private fun part1(measurements: List<Int>) {

    val result = measurements.fold(Pair(0, Int.MAX_VALUE)) { pair, current ->
        if (current > pair.second) {
            Pair(pair.first + 1, current)
        } else {
            Pair(pair.first, current)
        }
    }.first

    println("Result: $result")
}

private fun part2(measurements: List<Int>) {
    val windowed = measurements.windowed(3) { it.sum() }
    part1(windowed)
}
