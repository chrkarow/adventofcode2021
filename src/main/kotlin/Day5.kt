import java.io.File

fun main() {
    val coordinates = File("src/main/resources/day5.txt").readLines()
        .map { Pair(it.split(" -> ")[0], it.split(" -> ")[1]) }
        .map { Pair(Pair(it.first.split(",")[0].toInt(), it.first.split(",")[1].toInt()), Pair(it.second.split(",")[0].toInt(), it.second.split(",")[1].toInt())) }

    markVents(coordinates = coordinates) { it.first.first == it.second.first || it.first.second == it.second.second }
    markVents(coordinates = coordinates) { true }
}

private fun markVents(
    coordinates: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>,
    filterPredicate: (Pair<Pair<Int, Int>, Pair<Int, Int>>) -> Boolean
) {
    val field = Array(1000) { Array(1000) { 0 } }

    coordinates
        .filter(filterPredicate)
        .forEach { coordinatePair ->
            val x1 = coordinatePair.first.first
            val x2 = coordinatePair.second.first
            val xRange = if (x1 > x2) x1 downTo x2 else x1..x2

            val vertical = x1 == x2

            val y1 = coordinatePair.first.second
            val y2 = coordinatePair.second.second
            val yRange = if (y1 > y2) y1 downTo y2 else y1..y2
            val horizontal = y1 == y2

            if (horizontal) {
                xRange.forEach {
                    field[y1][it]++
                }
            } else if (vertical) {
                yRange.forEach {
                    field[it][x1]++
                }
            } else {
                yRange.zip(xRange).forEach {
                    field[it.first][it.second]++
                }
            }
        }

    val result = field.flatMap { line -> line.filter { it >= 2 } }.count()
    println(result)
}
