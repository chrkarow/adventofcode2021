import java.io.File

fun main() {
    val lanternfish = File("src/main/resources/day6.txt").readLines()[0].split(",").map { it.toInt() }

    simulate(lanternfish = lanternfish, days = 80)
    simulate(lanternfish = lanternfish, days = 256)
}


private fun simulate(lanternfish: List<Int>, days: Int) {
    val array = Array(9) { 0L }
    lanternfish.forEach { array[it]++ }

    for (day in 1..days) {
        val new = array[0]
        for (i in 1..array.lastIndex) {
            array[i - 1] = array[i]
        }
        array[8] = new
        array[6] += new
    }

    println(array.sum())
}
