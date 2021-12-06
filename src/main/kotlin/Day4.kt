import java.io.File

fun main() {
    val lines = File("src/main/resources/day4.txt").readLines()

    val numbers = lines[0].split(",").map { it.toInt() }
    val fields = lines.drop(2)
        .fold(mutableListOf<MutableList<String>>(mutableListOf())) { acc, line ->
            if (line.isBlank()) acc.add(mutableListOf()) else acc.last().add(line)
            acc
        }.map { BingoField.of(it) }


    part1(
        numbers = numbers,
        fields = fields.map { it.copy() }
    )

    part2(
        numbers = numbers,
        fields = fields.map { it.copy() }
    )

}

private fun part1(numbers: List<Int>, fields: List<BingoField>) {
    var i = 0
    while (fields.firstOrNull { it.hasWon() } == null) {
        fields.forEach {
            it.markNumber(numbers[i])
        }
        i++
    }

    val result = fields.firstOrNull { it.hasWon() }?.getResult()?.times(numbers[i - 1])
    println(result)
}

private fun part2(numbers: List<Int>, fields: List<BingoField>) {
    val winningHistory = mutableListOf<Pair<BingoField, Int>>()
    val workingArea = fields.toMutableList()

    numbers.forEach { number ->
        workingArea.forEach { it.markNumber(number) }

        // It could be multiple fields which are winning at the same time
        workingArea.filter { it.hasWon() }.forEach {
            winningHistory.add(Pair(it, number))
            workingArea.remove(it)
        }
    }

    val lastWinningField = winningHistory.last().first
    val lastWinningNumber = winningHistory.last().second

    println(lastWinningNumber * lastWinningField.getResult())
}

class BingoField(private var field: List<List<Pair<Int, Boolean>>>) {

    companion object {
        fun of(lines: List<String>): BingoField {
            return BingoField(lines
                .map { line ->
                    line
                        .trim()
                        .replace("  ", " ")
                        .split(" ")
                        .map {
                            Pair(it.toInt(), false)
                        }
                })
        }
    }

    fun markNumber(number: Int) {
        field = field.map { line ->
            line.map { if (it.first == number) Pair(it.first, true) else it }
        }
    }

    fun hasWon(): Boolean {
        if (field.firstOrNull { line -> line.map { it.second }.reduce(Boolean::and) } != null) {
            return true
        }
        field[0].indices.forEach { index ->
            if (field.map { it[index].second }.reduce(Boolean::and)) return true
        }
        return false
    }

    fun getResult(): Int {
        return field.flatMap { line -> line.filter { !it.second }.map { it.first } }.sum()
    }

    fun copy(): BingoField {
        return BingoField(
            field = this.field.map { line -> line.map { Pair(it.first, it.second) } }
        )
    }
}
