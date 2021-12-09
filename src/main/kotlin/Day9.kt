import java.io.File

data class Point(val x: Int, val y: Int) {
    fun neighborAbove() = Point(x = x, y = y - 1)
    fun neighborRight() = Point(x = x + 1, y = y)
    fun neighborBelow() = Point(x = x, y = y + 1)
    fun neighborLeft() = Point(x = x - 1, y = y)
}

fun main() {
    val heights = File("src/main/resources/day9.txt").readLines()
        .map { line -> line.split("").filter(String::isNotBlank).map { Pair(it.toInt(), false) }.toTypedArray() }

    part1(heights = heights)
    part2(heights = heights)
}

private fun part1(heights: List<Array<Pair<Int, Boolean>>>) {
    val result = getLowPoints(heights)
        .fold(0) { acc, p -> acc + heights.getHeightSafely(p) + 1 }
    println(result)
}

private fun part2(heights: List<Array<Pair<Int, Boolean>>>) {
    val result = getLowPoints(heights)
        .map {
            scanBasin(
                startPoint = it,
                map = heights,
                currentSize = 0
            )
        }.sortedDescending()
        .take(3)
        .reduce(Int::times)

    println(result)
}

private fun getLowPoints(heights: List<Array<Pair<Int, Boolean>>>) =
    heights.indices
        .flatMap { y -> heights[y].indices.map { x -> Point(x = x, y = y) } }
        .filter { isLowPoint(it, heights) }

private fun scanBasin(startPoint: Point, map: List<Array<Pair<Int, Boolean>>>, currentSize: Int): Int {
    if (map.getHeightSafely(point = startPoint) == 9 || map[startPoint.y][startPoint.x].second) return 0
    map[startPoint.y][startPoint.x] = Pair(map[startPoint.y][startPoint.x].first, true)

    val top = scanBasin(startPoint = startPoint.neighborAbove(), map = map, currentSize = currentSize)
    val right = scanBasin(startPoint = startPoint.neighborRight(), map = map, currentSize = currentSize)
    val bottom = scanBasin(startPoint = startPoint.neighborBelow(), map = map, currentSize = currentSize)
    val left = scanBasin(startPoint = startPoint.neighborLeft(), map = map, currentSize = currentSize)

    return 1 + top + right + bottom + left
}

private fun isLowPoint(point: Point, map: List<Array<Pair<Int, Boolean>>>): Boolean {
    val current = map.getHeightSafely(point = point)
    if (map.getHeightSafely(point = point.neighborAbove()) <= current) return false
    if (map.getHeightSafely(point = point.neighborRight()) <= current) return false
    if (map.getHeightSafely(point = point.neighborBelow()) <= current) return false
    if (map.getHeightSafely(point = point.neighborLeft()) <= current) return false
    return true
}

private fun List<Array<Pair<Int, Boolean>>>.getHeightSafely(point: Point): Int {
    return try {
        this[point.y][point.x].first
    } catch (e: IndexOutOfBoundsException) {
        9
    }
}
