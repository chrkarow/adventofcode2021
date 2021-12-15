import java.io.File
import java.util.*

data class RiskPoint(val y: Int, val x: Int) {
    fun getNeighbours() = listOf(
        RiskPoint(y = y - 1, x = x),
        RiskPoint(y = y, x = x + 1),
        RiskPoint(y = y + 1, x = x),
        RiskPoint(y = y, x = x - 1),
    )
}

data class RiskNode(
    val risk: Int,
    val coordinates: RiskPoint,
) {

    var predecessor: RiskNode? = null
    var distance: Int = Int.MAX_VALUE

    fun getUnvisitedNeighbors(
        risks: List<List<RiskNode>>,
        unvisitedNodes: Collection<RiskNode>
    ) =
        coordinates.getNeighbours()
            .mapNotNull { risks.getRiskSafely(it) }
            .filter { unvisitedNodes.contains(it) }

}

fun main() {
    val risks = File("src/main/resources/day15.txt").readLines()
        .mapIndexed { y, line ->
            line.split("").filter(String::isNotBlank).mapIndexed { x, value -> RiskNode(risk = value.toInt(), coordinates = RiskPoint(y = y, x = x)) }
        }

    part1(risks = risks.map { line -> line.map { it.copy() } })
    part2(risks = risks)
}

private fun part1(risks: List<List<RiskNode>>) {

    assesRisk(risks = risks)

    var predecessor: RiskNode? = risks.last().last()
    var costs = 0
    while (predecessor != null) {
        costs += predecessor.risk
        predecessor = predecessor.predecessor
    }

    println(costs)
}

private fun part2(risks: List<List<RiskNode>>) {
    // Blow up risk map 5 times
    val newRisk = (0 until (5 * risks.size)).map { line ->
        val y = line % risks.size
        val tileNumber = line / risks.size
        (0..4).map { x ->
            risks[y].map {
                it.copy(
                    risk = if (it.risk + x + tileNumber >= 10) it.risk + x + tileNumber - 9 else it.risk + x + tileNumber,
                    coordinates = RiskPoint(y = line, x = it.coordinates.x + x * risks[y].size)
                )
            }
        }.flatten()
    }

    part1(risks = newRisk)
}

/** Dijkstra Algorithm (Source: https://de.wikipedia.org/wiki/Dijkstra-Algorithmus) **/
private fun assesRisk(risks: List<List<RiskNode>>) {

    val unvisitedNodes = risks.flatten().toPriorityQueue(Comparator.comparingInt { it.distance })
    risks[0][0].distance = 0

    while (unvisitedNodes.isNotEmpty()) {

        val currentNode = unvisitedNodes.remove()
        currentNode.getUnvisitedNeighbors(risks = risks, unvisitedNodes = unvisitedNodes)
            .forEach {
                val newDistance = currentNode.distance + it.risk
                if (newDistance < it.distance) {
                    it.distance = newDistance
                    it.predecessor = currentNode
                    unvisitedNodes.remove(it)
                    unvisitedNodes.add(it)
                }
            }
    }
}

private fun List<List<RiskNode>>.getRiskSafely(point: RiskPoint): RiskNode? {
    return try {
        this[point.y][point.x]
    } catch (e: IndexOutOfBoundsException) {
        null
    }
}

private fun <T> List<T>.toPriorityQueue(comparator: Comparator<T>): PriorityQueue<T> {
    val queue = PriorityQueue(comparator)
    queue.addAll(this)
    return queue
}
