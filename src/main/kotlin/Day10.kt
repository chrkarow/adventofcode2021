import java.io.File

const val opening = "([{<"
const val closing = ")]}>"
val penaltyTable = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137
)

val autocompleteTable = mapOf(
    ')' to 1,
    ']' to 2,
    '}' to 3,
    '>' to 4
)

fun main() {
    val chunks = File("src/main/resources/day10.txt").readLines()
    part1(chunks = chunks)
    part2(chunks = chunks)
}

private fun part1(chunks: List<String>) {
    val result = chunks
        .mapNotNull { checkChunk(it) }
        .mapNotNull { penaltyTable[it] }
        .sum()

    println(result)
}

private fun part2(chunks: List<String>) {
    val scores = chunks
        .filter { checkChunk(it) == null }
        .map { createClosingSequence(it).fold(0L) { acc, char -> acc * 5 + autocompleteTable[char]!! } }
        .sorted()

    val result = scores[scores.size / 2]
    println(result)
}

private fun checkChunk(chunk: String): Char? =
    chunk.toCharArray()
        .fold(listOf<Char>()) { acc, char ->
            if (opening.contains(char)) {
                acc.plus(char)
            } else {
                val matchingBracket = acc.last()
                if (closing.indexOf(char) != opening.indexOf(matchingBracket)) {
                    return char
                }
                acc.take(acc.size - 1)
            }
        }.let { null }


private fun createClosingSequence(chunk: String) =
    chunk.toCharArray()
        .fold(listOf<Char>()) { acc, char ->
            if (opening.contains(char)) {
                acc.plus(char)
            } else {
                acc.take(acc.size - 1)
            }
        }
        .reversed()
        .map { closing[opening.indexOf(it)] }
        .joinToString(separator = "")

