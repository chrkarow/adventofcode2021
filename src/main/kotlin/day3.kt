import java.io.File

fun main() {
    val binaries = File("src/main/resources/day3.txt").readLines()

    part1(binaries = binaries)
    part2(binaries = binaries)
}

private fun part1(binaries: List<String>) {

    val epsilonString = binaries[0]
        .mapIndexed { index, _ ->
            binaries.map { it[index] }.joinToString(separator = "")
        }.map { getMostCommon(it) }
        .joinToString(separator = "")

    val epsilonRate = epsilonString.toUShort(2)
    val gammaRate = epsilonRate.inv()
        .minus(61440.toUShort()) // As UShort is 16 Bit, we need to cut the leading 4 bits (1111000000000000 = 61440)
    println(epsilonRate * gammaRate)
}

private fun part2(binaries: List<String>) {

    val oxygenGenerator = calculatePart2Metrics(
        binaries = binaries,
        criteriaFunction = ::getMostCommon
    )

    val co2Scrubber = calculatePart2Metrics(
        binaries = binaries,
        criteriaFunction = ::getLeastCommon
    )

    println(oxygenGenerator * co2Scrubber)
}

private fun calculatePart2Metrics(
    binaries: List<String>,
    criteriaFunction: (String) -> Char
): UShort {
    var currentIndex = 0
    var workingCopy = binaries

    while (workingCopy.size > 1) {
        val criterion = criteriaFunction(workingCopy.map { it[currentIndex] }.joinToString(separator = ""))
        workingCopy = workingCopy.filter { it[currentIndex] == criterion }
        currentIndex++
    }
    return workingCopy[0].toUShort(2)
}

private fun getMostCommon(input: String): Char {
    val counts = count(input)
    return if (counts.first >= counts.second) '1' else '0'
}

private fun getLeastCommon(input: String): Char {
    val counts = count(input)
    return if (counts.first < counts.second) '1' else '0'
}

private fun count(input: String): Pair<Int, Int> {
    return input.fold(Pair(0, 0)) { acc, i ->
        if (i == '1') {
            Pair(acc.first + 1, acc.second)
        } else {
            Pair(acc.first, acc.second + 1)
        }
    }
}
