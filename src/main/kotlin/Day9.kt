import java.io.File

data class Point(val x: Int, val y: Int) {
    fun neighborAbove() = Point(x = x, y = y - 1)
    fun neighborRight() = Point(x = x + 1, y = y)
    fun neighborBelow() = Point(x = x, y = y + 1)
    fun neighborLeft() = Point(x = x - 1, y = y)
}

fun main() {
    val heights = File("src/main/resources/day9.txt").readLines()
        .map { line -> line.split("").filter(String::isNotBlank).map(String::toInt) }

    part1(heights = heights)
    part2(heights = heights)
}

private fun part1(heights: List<List<Int>>) {
    val result = getLowPoints(heights)
        .fold(0) { acc, p -> acc + heights.getHeightSafely(p) + 1 }
    println(result)
}

private fun part2(heights: List<List<Int>>) {
    val result = getLowPoints(heights)
        .map {
            scanBasin(
                startPoint = it,
                map = heights,
            ).size
        }.sortedDescending()
        .take(3)
        .reduce(Int::times)

    println(result)
}

private fun getLowPoints(heights: List<List<Int>>) =
    heights.indices
        .flatMap { y -> heights[y].indices.map { x -> Point(x = x, y = y) } }
        .filter { isLowPoint(it, heights) }

private fun scanBasin(startPoint: Point, map: List<List<Int>>, currentBasin: Set<Point> = emptySet()): Set<Point> {
    if (map.getHeightSafely(point = startPoint) == 9 || currentBasin.contains(startPoint)) return currentBasin

    var nextBasin = currentBasin.plus(startPoint)

    nextBasin = scanBasin(startPoint = startPoint.neighborAbove(), map = map, currentBasin = nextBasin)
    nextBasin = scanBasin(startPoint = startPoint.neighborRight(), map = map, currentBasin = nextBasin)
    nextBasin = scanBasin(startPoint = startPoint.neighborBelow(), map = map, currentBasin = nextBasin)
    nextBasin = scanBasin(startPoint = startPoint.neighborLeft(), map = map, currentBasin = nextBasin)

    return nextBasin
}

private fun isLowPoint(point: Point, map: List<List<Int>>): Boolean {
    val current = map.getHeightSafely(point = point)
    if (map.getHeightSafely(point = point.neighborAbove()) <= current) return false
    if (map.getHeightSafely(point = point.neighborRight()) <= current) return false
    if (map.getHeightSafely(point = point.neighborBelow()) <= current) return false
    if (map.getHeightSafely(point = point.neighborLeft()) <= current) return false
    return true
}

private fun List<List<Int>>.getHeightSafely(point: Point): Int {
    return try {
        this[point.y][point.x]
    } catch (e: IndexOutOfBoundsException) {
        9
    }
}
