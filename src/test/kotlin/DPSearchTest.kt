object DPSearchTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val seq1 = Sequence("seq1", "ARND")
        val seq2 = Sequence("seq2", "AND")
        val search1 = DPSearch(seq1, seq2, BLOSUM62())
        val similarity1 = search1.find()
        println(similarity1)

        val seq3 = Sequence("seq3", "AARND")
        val seq4 = Sequence("seq4", "RND")
        val search2 = DPSearch(seq3, seq4, BLOSUM62())
        val similarity2 = search2.find()
        println(similarity2)

        val seq5 = Sequence("seq5", "AARENDNN")
        val seq6 = Sequence("seq6", "RND")
        val search3 = DPSearch(seq5, seq6, BLOSUM62())
        val similarity3 = search3.find()
        println(similarity3)
    }
}