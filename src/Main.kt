import java.util.*

fun printArr(arr: Array<Double>) {
    arr.forEach { value -> System.out.printf(Locale.ENGLISH, "%.5f ", value) }
    println()
}

fun printMtr(mtr: Array<Array<Double>>) {
    mtr.forEach { line ->
        run {
            line.forEach { value -> System.out.printf(Locale.ENGLISH, "%.5f ", value) }
            println()
        }
    }
}

fun calcF(x: Double, t: Double): Double {
    return 6.0 * t - 2.0
}

fun calcU0(x: Double): Double {
    return x * x
}

fun calcU1(x: Double): Double {
    return 0.0
}

fun calcMu0(t: Double): Double {
    return t * t * t
}

fun calcMu1(t: Double): Double {
    return 3.0 + t * t * t
}

fun calcSolution(spaceNodes: Array<Double>, timeNodes: Array<Double>, spaceStep: Double, timeStep: Double, beta: Double): Array<Array<Double>> {
    val result = Array(timeNodes.size, { Array(spaceNodes.size, { 0.0 }) })
    val spaceSplitNum = spaceNodes.size - 1

    for (i in 0 until spaceNodes.size) {
        result[0][i] = calcU0(spaceNodes[i])
    }

    result[1][0] = calcMu0(timeNodes[1])
    for (i in 1 until spaceSplitNum) {
        result[1][i] = timeStep * (calcU1(spaceNodes[1]) + timeStep * ((result[0][i + 1] - 2.0 * result[0][i] + result[0][i - 1]) / spaceStep +
                calcF(spaceNodes[i], timeNodes[0]))) + result[0][i]
    }
    result[1][spaceSplitNum] = (result[1][spaceSplitNum - 1] + spaceStep * calcMu1(timeNodes[1])) / (1.0 + beta * spaceStep)

    for (j in 2 until result.size) {
        result[j][0] = calcMu0(timeNodes[j])
        for (i in 1 until spaceSplitNum) {
            result[j][i] = timeStep * timeStep * ((result[j - 1][i + 1] - 2.0 * result[j - 1][i] + result[j - 1][i - 1]) / (spaceStep * spaceStep) +
                    calcF(spaceNodes[i], timeNodes[j])) + 2.0 * result[j - 1][i] - result[j - 2][i]
        }
        result[j][spaceSplitNum] = (2.0 * (result[j][spaceSplitNum - 1] / spaceStep + calcMu1(timeNodes[j])) / spaceStep -
                (result[j - 2][spaceSplitNum] - 2.0 * result[j - 1][spaceSplitNum]) / (timeStep * timeStep) + calcF(spaceNodes[spaceSplitNum], timeNodes[j])) /
                (1.0 / (timeStep * timeStep) + 2.0 * (beta + 1.0 / spaceStep) / spaceStep)
    }

    return result
}

fun main(args: Array<String>) {
    val spaceSplitNum = 10
    val timeSplitNum = 20
    val timeMax = 1.0
    val spaceStep = 1.0 / spaceSplitNum
    val timeStep = timeMax / timeSplitNum
    val spaceNodes = Array(spaceSplitNum + 1, { i: Int -> i * spaceStep })
    val timeNodes = Array(timeSplitNum + 1, { j: Int -> j * timeStep })
    val beta = 1.0

    println("Узлы по x")
    printArr(spaceNodes)
    println("Узлы по t")
    printArr(timeNodes)
    println("Результат")
    printMtr(calcSolution(spaceNodes, timeNodes, spaceStep, timeStep, beta))
}