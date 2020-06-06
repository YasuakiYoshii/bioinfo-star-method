import javax.management.QueryEval
import kotlin.math.max

/** Center Star Method */
class StarMethod(val setOfSeq: List<Sequence>, val score: BLOSUM62, val penalty: Int = 0) {

    private val distanceMatrix = Array(setOfSeq.size) { arrayOfNulls<Int>(setOfSeq.size) }

    private val sumOfDistance = arrayListOf<Int>()
    private var center: Int? = null

    private val centerPairwiseAlignment = arrayOfNulls<PairwiseAlignment>(setOfSeq.size)

    private val gap: Char = '-'

    private fun calcDistance(): Int {
        // calculate D[i,i]
        calcSameAlignmentDistance()
        for (i in setOfSeq.indices) {
            sumOfDistance.add(i, 0)
        }
        // calculate each pairwise alignment distance
        for (i in 1 until setOfSeq.size) {
            for (j in 0 until i) {
                val distance = DPSearch(setOfSeq[i], setOfSeq[j], score, penalty).find()
                distanceMatrix[i][j] = distance
                distanceMatrix[j][i] = distance
                sumOfDistance[i] = sumOfDistance[i].plus(distance)
                sumOfDistance[j] = sumOfDistance[j].plus(distance)
            }
        }
        // find center alignment
        center = sumOfDistance.indexOf(sumOfDistance.max())
        return center as Int
    }

    fun start() {
        calcDistance()
        for (i in setOfSeq.indices) {
            if (i == center) continue
            val search = DPSearch(setOfSeq[this.center!!], setOfSeq[i], score, penalty)
            search.find()
            val pair = search.getPairwiseAlignment()
            centerPairwiseAlignment[i] = pair
//            println("%5s: ${pair.alignment2.joinToString("")}".format(setOfSeq[i].name))
//            println("%5s: ${pair.alignment1.joinToString("")}".format(setOfSeq[center!!].name))
        }
        combinePairwise()
    }

    /** Combine based on center */
    private fun combinePairwise() {
        val centerSeq = setOfSeq[center!!].sequence
        var startIndex = 0
        for (i in 0 until setOfSeq[center!!].length) {
            // Get max index of centerSeq[i]
            var maxIndex = i
            for (j in setOfSeq.indices) {
                if (j == center) continue
                val current = centerPairwiseAlignment[j]?.alignment1?.joinToString("")?.indexOf(centerSeq[i], startIndex)
                if (maxIndex < current!!) maxIndex = current
            }
            // Fill gaps
            for (j in setOfSeq.indices) {
                val current = centerPairwiseAlignment[j]?.alignment1?.joinToString("")?.indexOf(centerSeq[i], startIndex)
                if (current != null) {
                    for (k in current until maxIndex) {
                        centerPairwiseAlignment[j]?.alignment1?.add(k, gap)
                        centerPairwiseAlignment[j]?.alignment2?.add(k, gap)
                    }
                }
            }
            startIndex = maxIndex + 1
            // Fill last gap
            var maxLength = 0
            if (i == setOfSeq[center!!].length - 1) {
                // Get max length of center
                for (j in setOfSeq.indices) {
                    if (j == center) continue
                    if (centerPairwiseAlignment[j]?.alignment1?.size!! > maxLength) {
                        maxLength = centerPairwiseAlignment[j]?.alignment1?.size!!
                    }
                }
                // Fill gap
                for (j in setOfSeq.indices) {
                    if (j == center) continue
                    if (centerPairwiseAlignment[j]?.alignment1?.size!! < maxLength) {
                        centerPairwiseAlignment[j]?.alignment1?.add(gap)
                        centerPairwiseAlignment[j]?.alignment2?.add(gap)
                    }
                }
            }
        }
    }

    fun getResult() {
        // Print result
        var first = true
        for (i in setOfSeq.indices) {
            if (i == center) continue
            if (first) {
                println("(center) %5s: ${centerPairwiseAlignment[i]?.alignment1?.joinToString("")}".format(setOfSeq[center!!].name))
                println("         %5s: ${centerPairwiseAlignment[i]?.alignment2?.joinToString("")}".format(setOfSeq[i].name))
                first = false
            } else {
                println("         %5s: ${centerPairwiseAlignment[i]?.alignment2?.joinToString("")}".format(setOfSeq[i].name))
            }
        }
    }

    private fun calcSameAlignmentDistance() {
        for (i in setOfSeq.indices) {
            val sequence = setOfSeq[i].sequence
            var distance = 0
            for (j in sequence.indices) {
                distance += score.getScore(sequence[j], sequence[j])
            }
            distanceMatrix[i][i] = distance
        }
    }

    fun getDistanceMatrix() {
        println("score matrix:")
        for (i in setOfSeq.indices) {
            for (j in setOfSeq.indices) {
                print("%3s ".format(distanceMatrix[i][j]))
            }
            println()
        }
    }
}