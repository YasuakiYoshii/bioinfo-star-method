import kotlin.system.measureTimeMillis

object StarMain {
    @JvmStatic
    fun main(args: Array<String>) {
        val setOfSeq = listOf<Sequence>(
            Sequence("copia", "ILDFHEKLLHPGIQKTTKLFGETYYFPNSQLLIQNIINECSICNLAK"),
            Sequence("MMULV", "LLDFLLHQLTHLSFSKMKALLERSHSPYYMLNRDRTLKNITETCKACAQVN"),
            Sequence("HTLV", "LQLSPAELHSFTHCGQTALTLQGATTTEASNILRSCHACRGGN"),
            Sequence("RSV", "YPLREAKDLHTALHIGPRALSKACNISMQQAREVVQTCPHCNSA"),
            Sequence("MMTV", "IHEATQAHTLHHLNAHTLRLLYKITREQARDIVKACKQCVVAT"),
            Sequence("SMRV", "LESAQESHALHHQNAAALRFQFHITREQAREIVKLCPNCPDWGS"))

        val star = StarMethod(setOfSeq, BLOSUM62(), 0)
        val time = measureTimeMillis {
            star.start()
        }
        println("実行時間: $time[ms]")
        star.getResult()
        star.getDistanceMatrix()
    }
}