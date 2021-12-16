import java.io.File

fun main() {
    val packages = File("src/main/resources/day16.txt").readLines()[0]
        .let { line ->
            line
                .split("")
                .filter(String::isNotBlank)
                .joinToString(separator = "") { it.toInt(16).toString(2).padStart(4, '0') }
        }

    val parser = BitsParser(packageString = packages)
    val part2Result = parser.parsePackages()[0]

    println(parser.versionSum)
    println(part2Result)
}

class BitsParser(private val packageString: String) {

    private var parseIndex = 0
    var versionSum = 0

    fun parsePackages(
        packageCount: Int? = null,
        maxIndex: Int = Int.MAX_VALUE
    ): List<Long> {

        val result = mutableListOf<Long>()
        repeat(packageCount ?: Int.MAX_VALUE) {
            if (parseIndex >= maxIndex || !hasMore()) return result
            val version = parseInt(3)
            versionSum += version
            val typeId = parseInt(3)

            result.add(
                if (typeId == 4) {
                    parseLiteral()
                } else {
                    parseOperator(typeId = typeId)
                }
            )
        }
        return result
    }

    private fun parseLiteral(): Long {
        val sb = StringBuilder()
        while (parseInt(1) == 1) {
            sb.append(parseString(4))
        }
        sb.append(parseString(4))

        return sb.toString().toLong(2)
    }

    private fun parseOperator(typeId: Int): Long {
        val members = when (parseInt(1)) {
            0 -> parsePackages(maxIndex = parseInt(15) + parseIndex)
            1 -> parsePackages(packageCount = parseInt(11))
            else -> throw IllegalStateException("Kaputt")
        }

        return when (typeId) {
            0 -> members.sum()
            1 -> members.fold(1) { acc, i -> acc * i }
            2 -> members.minOrNull() ?: 0
            3 -> members.maxOrNull() ?: 0
            5 -> if (members[0] > members[1]) 1 else 0
            6 -> if (members[0] < members[1]) 1 else 0
            7 -> if (members[0] == members[1]) 1 else 0
            else -> throw IllegalStateException("Kaputt")
        }
    }

    private fun parseInt(size: Int): Int = parseString(size).toInt(2)

    private fun parseString(size: Int): String {
        val newParseIndex = parseIndex + size
        val result = packageString.substring(parseIndex, newParseIndex)
        parseIndex = newParseIndex
        return result
    }

    private fun hasMore() = parseIndex < packageString.lastIndex &&
            packageString.drop(parseIndex).replace("0", "").isNotEmpty()
}




