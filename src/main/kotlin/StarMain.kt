import kotlin.system.measureTimeMillis

/**
 * Main for running center star method
 *
 * @author Yasuaki Yoshii
 *
 */
object StarMain {
    @JvmStatic
    fun main(args: Array<String>) {
        // dataset of sequence
        val setOfSeq = listOf<Sequence>(
            Sequence("copia", "ILDFHEKLLHPGIQKTTKLFGETYYFPNSQLLIQNIINECSICNLAK"),
            Sequence("MMULV", "LLDFLLHQLTHLSFSKMKALLERSHSPYYMLNRDRTLKNITETCKACAQVN"),
            Sequence("HTLV", "LQLSPAELHSFTHCGQTALTLQGATTTEASNILRSCHACRGGN"),
            Sequence("RSV", "YPLREAKDLHTALHIGPRALSKACNISMQQAREVVQTCPHCNSA"),
            Sequence("MMTV", "IHEATQAHTLHHLNAHTLRLLYKITREQARDIVKACKQCVVAT"),
            Sequence("SMRV", "LESAQESHALHHQNAAALRFQFHITREQAREIVKLCPNCPDWGS"))

        val star = StarMethod(setOfSeq, BLOSUM62(), -4)
        val time = measureTimeMillis {
            star.start()
        }
        println("execution time: $time[ms]")
        star.getResult()
        star.getDistanceMatrix()
        println("Successfully finished!")
    }
}