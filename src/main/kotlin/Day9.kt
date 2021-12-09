import java.io.File

fun main() {
    val heights = File("src/main/resources/day9.txt").readLines()
        .map { line -> line.split("").filter(String::isNotBlank).map { Pair(it.toInt(), false) }.toTypedArray() }

    part1(heights = heights)
    part2(heights = heights)
}

private fun part1(heights: List<Array<Pair<Int, Boolean>>>) {
    var result = 0
    for (y in heights.indices) {
        for (x in heights[y].indices) {
            if (isLowPoint(x, y, heights)) {
                result += 1 + heights[y][x].first
            }
        }
    }
    println(result)
}

private fun part2(heights: List<Array<Pair<Int, Boolean>>>) {
    val basinSizes = mutableListOf<Int>()
    for (y in heights.indices) {
        for (x in heights[y].indices) {
            val basinSize = scanBasin(startX = x, startY = y, map = heights, 0)
            if (basinSize != 0) basinSizes.add(basinSize)
        }
    }
    println(basinSizes.toList().sorted().takeLast(3).reduce(Int::times))
}

private fun scanBasin(startX: Int, startY: Int, map: List<Array<Pair<Int, Boolean>>>, currentSize: Int): Int {
    val current = map.gePairSafely(y = startY, x = startX)
    if (current.first == 9 || current.second) return 0

    map[startY][startX] = Pair(map[startY][startX].first, true)

    val top = scanBasin(startY = startY - 1, startX = startX, map = map, currentSize = currentSize + 1)
    val right = scanBasin(startY = startY, startX = startX + 1, map = map, currentSize = currentSize + 1)
    val bottom = scanBasin(startY = startY + 1, startX = startX, map = map, currentSize = currentSize + 1)
    val left = scanBasin(startY = startY, startX = startX - 1, map = map, currentSize = currentSize + 1)
    return 1 + top + right + bottom + left
}

private fun isLowPoint(startX: Int, startY: Int, map: List<Array<Pair<Int, Boolean>>>): Boolean {
    val current = map.gePairSafely(y = startY, x = startX).first
    if (map.gePairSafely(y = startY - 1, x = startX).first <= current) return false
    if (map.gePairSafely(y = startY, x = startX + 1).first  <= current) return false
    if (map.gePairSafely(y = startY + 1, x = startX).first  <= current) return false
    if (map.gePairSafely(y = startY, x = startX - 1).first  <= current) return false
    return true
}

private fun List<Array<Pair<Int, Boolean>>>.gePairSafely(y: Int, x: Int): Pair<Int, Boolean> {
    return try {
        val line = this[y]
        line[x]
    } catch (e: IndexOutOfBoundsException) {
        Pair(9, true)
    }
}
