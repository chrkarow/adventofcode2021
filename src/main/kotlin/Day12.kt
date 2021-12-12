import java.io.File

const val lowercaseChars = "abcdefghijklmnopqrstuvwxyz"

data class Cave(val name: String) {
    val connectedTo: MutableSet<Cave> = mutableSetOf()

    fun connectTo(cave: Cave) {
        this.connectedTo.add(cave)
        cave.connectedTo.add(this)
    }

    fun isSmallCave(): Boolean =
        lowercaseChars.contains(name[0])

    fun isStart(): Boolean = name == "start"
    fun isEnd(): Boolean = name == "end"

}

fun main() {
    val caveSystem = mutableSetOf<Cave>()
    File("src/main/resources/day12.txt").readLines()
        .map { Pair(it.split("-")[0], it.split("-")[1]) }
        .forEach { connection ->
            val cave1 = caveSystem.firstOrNull { it.name == connection.first } ?: Cave(connection.first)
            val cave2 = caveSystem.firstOrNull { it.name == connection.second } ?: Cave(connection.second)
            cave1.connectTo(cave2)
            caveSystem.add(cave1)
            caveSystem.add(cave2)
        }


    val result1 = scanCaveSystem(
        start = caveSystem.first(Cave::isStart),
        caveSystem = caveSystem,
        part2 = false
    )
    println(result1.size)

    val result2 = scanCaveSystem(
        start = caveSystem.first(Cave::isStart),
        caveSystem = caveSystem,
        part2 = true
    )
    println(result2.size)
}

fun scanCaveSystem(
    start: Cave,
    caveSystem: Set<Cave>,
    currentWay: List<Cave> = emptyList(),
    part2: Boolean
): Set<List<Cave>> {

    val newWay = currentWay.plus(start)
    if (start.isEnd()) {
        return setOf(newWay)
    }

    val hasDuplicateSmallCave =
        !part2 || newWay.filter { it.isSmallCave() }.groupingBy { it }.eachCount().any { it.value > 1 }

    return start.connectedTo
        .filter {
            (!it.isStart() && (!it.isSmallCave() || !hasDuplicateSmallCave)) || !currentWay.contains(it)
        }
        .flatMap {
            scanCaveSystem(
                start = it,
                caveSystem = caveSystem,
                currentWay = newWay,
                part2 = part2
            )
        }
        .toSet()
}
