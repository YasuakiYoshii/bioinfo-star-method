import kotlin.math.max

/**
 * Dynamic programming for pairwise alignment
 *
 * @author Yasuaki Yoshii
 *
 * @param a sequence a
 * @param b sequence b
 * @param distanceTable similarity score matrix or distance matrix
 * @param penalty value of penalty for gap
 */
class DPSearch(val a: Sequence, val b: Sequence, val distanceTable: BLOSUM62, val penalty: Int = 0) {

    /** Main matrix */
    private val matrix = Array(a.length + 1) { arrayOfNulls<Int>(b.length + 1) }

    /** Score matrix */
    private val score = Array(a.length) { arrayOfNulls<Int>(b.length) }

    /** Record matrix for trace back */
    private val forwardRecord = Array(a.length + 1) { arrayOfNulls<Int>(b.length + 1) }

    /** Alignment result of sequence a with gap */
    private val aResult = convertStringToCharList(a.sequence)

    /** Index of sequence a for trace back */
    private var aIndex = a.length - 1

    /** Alignment result of sequence b with gap */
    private val bResult = convertStringToCharList(b.sequence)

    /** Index of sequence b for trace back */
    private var bIndex = b.length - 1

    /** character for gap */
    private val gap: Char = '-'

    private fun convertStringToCharList(str: String): ArrayList<Char> {
        val list = arrayListOf<Char>()
        for (i in str.indices) {
            list.add(i, str[i])
        }
        return list
    }

    /** execute DP */
    fun find(): Int {
        initialize() // 1. initialize
        fillMatrix() // 2. fill matrix
        traceBack(a.length, b.length) // 3. trace back
        return matrix[a.length][b.length]!!
    }

    /** initialization step */
    private fun initialize() {
        for (row in 0 until a.length + 1) {
            matrix[row][0] = (row + 1) * penalty
            forwardRecord[row][0] = -1
        }
        for (col in 0 until b.length + 1) {
            matrix[0][col] = (col + 1) * penalty
            forwardRecord[0][col] = -1
        }
        // Fill score matrix
        for (row in 0 until a.length) {
            for (col in 0 until b.length) {
                val aSeq = a.sequence[row]
                val bSeq = b.sequence[col]
                score[row][col] = distanceTable.getScore(aSeq, bSeq)
            }
        }
    }

    /** Matrix Fill Step */
    private fun fillMatrix() {
        for (row in 1 until a.length + 1) {
            for (col in 1 until b.length + 1) {
//                println("row=$row, col=$col")
                matrix[row][col] = selectMax(row, col)
            }
        }
    }

    /** Select max of three */
    private fun selectMax(row: Int, col: Int): Int {
        val gapB = penalty + matrix[row-1][col]!! // sequence b has gap
        val gapA = penalty + matrix[row][col-1]!! // sequence a has gap
        val match = matrix[row-1][col-1]!! + score[row-1][col-1]!! // match
        val max = max(max(gapB, gapA), match)
        if (max == match) {
            // insert nothing
            forwardRecord[row][col] = 2
        } else {
            if (gapA == gapB) {
                if (a.length - row > b.length - col) {
                    // insert gap to a
                    forwardRecord[row][col] = 0
                } else {
                    // insert gap to b
                    forwardRecord[row][col] = 1
                }
            } else {
                if (max == gapA) {
                    // insert gap to a
                    forwardRecord[row][col] = 0
                } else {
                    // insert gap to b
                    forwardRecord[row][col] = 1
                }
            }
        }
        return max
    }

    /** Trace Back Step */
    private fun traceBack(row: Int, col: Int) {
        if (row == 0 && col == 0) return
        when (forwardRecord[row][col]) {
            0 -> { // gapA
                aResult.add(aIndex+1, gap)
                bIndex--
                traceBack(row, col-1)
            }
            1 -> { // gapB
                bResult.add(bIndex+1, gap)
                aIndex--
                traceBack(row-1, col)
            }
            2 -> { // match or mismatch
                aIndex--
                bIndex--
                traceBack(row-1, col-1)
            }
            -1 -> { // row == 0 or col == 0
                if (col != 0) {
                    aResult.add(0, gap)
                    bIndex--
                    traceBack(row, col-1)
                } else if (row != 0) {
                    bResult.add(0, gap)
                    aIndex--
                    traceBack(row-1, col)
                }
            }
        }
    }

    /** Get Alignment results */
    fun getPairwiseAlignment(): PairwiseAlignment {
        return PairwiseAlignment(aResult, bResult)
    }
}
