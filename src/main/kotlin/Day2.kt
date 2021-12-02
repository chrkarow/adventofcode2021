import java.io.File

fun main() {
    val directions = File("src/main/resources/day2.txt").readLines()
        .map { Pair(it.split(" ")[0], it.split(" ")[1].toInt()) }

    part1(directions = directions)
    part2(directions = directions)
}

private fun part1(directions: List<Pair<String, Int>>){
    var depth = 0
    var horizontal = 0

    directions.forEach {
        when(it.first){
            "down" -> depth += it.second
            "up" -> depth -= it.second
            "forward" -> horizontal += it.second
        }
    }

    println(depth * horizontal)
}

private fun part2(directions: List<Pair<String, Int>>){
    var depth = 0
    var horizontal = 0
    var aim = 0

    directions.forEach {
        when(it.first){
            "down" -> aim += it.second
            "up" -> aim -= it.second
            "forward" -> {
                horizontal += it.second
                depth += aim * it.second
            }
        }
    }

    println(depth * horizontal)
}
