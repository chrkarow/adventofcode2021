import java.io.File

data class Coordinates(val x: Int, val y: Int) {
    fun getAdjacent() = listOf(
        Coordinates(x = x, y = y - 1),
        Coordinates(x = x + 1, y = y - 1),
        Coordinates(x = x + 1, y = y),
        Coordinates(x = x + 1, y = y + 1),
        Coordinates(x = x, y = y + 1),
        Coordinates(x = x - 1, y = y + 1),
        Coordinates(x = x - 1, y = y),
        Coordinates(x = x - 1, y = y - 1),
    )
}

fun main() {
    val octopusses = File("src/main/resources/day11.txt").readLines()
        .map { line -> line.toCharArray().map(Char::digitToInt).toIntArray() }

    part1(octopusses = octopusses.map { line -> line.clone() })
    part2(octopusses = octopusses.map { line -> line.clone() })
}

private fun part1(octopusses: List<IntArray>) {
    val result = (1..100).fold(0) { acc, _ -> acc + step(octopusses) }
    println(result)
}

private fun part2(octopusses: List<IntArray>) {
    var result = 1
    while (step(octopusses) != 100) {
        result++
    }
    println(result)
}

private fun step(octopusses: List<IntArray>): Int {
    var result = 0
    octopusses.forEachIndexed { y, line ->
        line.forEachIndexed { x, _ ->
            increase(Coordinates(x = x, y = y), octopusses)
        }
    }
    octopusses.forEachIndexed { y, line ->
        line.forEachIndexed { x, _ ->
            if (octopusses[y][x] > 9) {
                result++
                octopusses[y][x] = 0
            }
        }
    }
    return result
}

private fun increase(coordinates: Coordinates, octopusses: List<IntArray>) {
    octopusses.get(coordinates)?.also {
        if (it > 9) return

        val newValue = it + 1

        octopusses[coordinates.y][coordinates.x] = newValue

        if (newValue > 9) {
            coordinates.getAdjacent().forEach { coord ->
                increase(coord, octopusses)
            }
        }
    }
}

private fun List<IntArray>.get(coordinates: Coordinates): Int? {
    return try {
        this[coordinates.y][coordinates.x]
    } catch (e: IndexOutOfBoundsException) {
        null
    }
}
