import java.io.File

fun main() {
    val lines = File("src/main/resources/day13.txt").readLines()
    val emptyLineIndex = lines.indexOf("")

    val coordinates = lines
        .take(emptyLineIndex)
        .map { it.split(",") }
        .map { it[0].toInt() to it[1].toInt() }
        .toSet()

    val foldingInstructions = lines
        .drop(emptyLineIndex + 1)
        .map { it.replaceFirst("fold along ", "") }
        .map { it.split("=") }
        .map { it[0] to it[1].toInt() }

    part1(
        coordinates = coordinates,
        foldingInstructions = foldingInstructions
    )

    part2(
        coordinates = coordinates,
        foldingInstructions = foldingInstructions
    )
}

private fun part1(coordinates: Set<Pair<Int, Int>>, foldingInstructions: List<Pair<String, Int>>) {
    val result = executeFolds(coordinates, foldingInstructions.take(1)).count()
    println(result)
}

private fun part2(coordinates: Set<Pair<Int, Int>>, foldingInstructions: List<Pair<String, Int>>) {
    val result = executeFolds(coordinates, foldingInstructions)
    visualize(coordinates = result)
}

private fun visualize(coordinates: Set<Pair<Int, Int>>) {
    val display = Array(10) { Array(50) { " " } }
    coordinates.forEach {
        display[it.second][it.first] = "#"
    }

    display.forEach { line ->
        println(line.joinToString(separator = ""))
    }
}

private fun executeFolds(
    coordinates: Set<Pair<Int, Int>>,
    foldingInstructions: List<Pair<String, Int>>
) =
    foldingInstructions.fold(coordinates) { coord, instruction ->
        if (instruction.first == "x") {
            foldLeft(
                coordinates = coord,
                col = instruction.second
            )
        } else {
            foldUp(
                coordinates = coord,
                line = instruction.second
            )
        }
    }


private fun foldUp(
    coordinates: Set<Pair<Int, Int>>,
    line: Int
) =
    coordinates
        .map { if (it.second <= line) it else it.first to line - (it.second - line) }
        .toSet()


private fun foldLeft(
    coordinates: Set<Pair<Int, Int>>,
    col: Int
) =
    coordinates
        .map { if (it.first <= col) it else col - (it.first - col) to it.second }
        .toSet()

