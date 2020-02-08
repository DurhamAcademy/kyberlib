package frc.team6502.kyberlib.math

class Interpolator(private val data: Map<Double, Double>) {

    fun calculate(x: Double): Double? {

        val nextHighest = getNext(x)
        val nextLowest = getPrevious(x)

        if(nextHighest == null || nextLowest == null) return null
        if(nextHighest.value == nextLowest.value) return nextLowest.value

        val alpha = (x - nextLowest.key) / (nextHighest.key - nextLowest.key)

        return nextLowest.value + alpha * (nextHighest.value - nextLowest.value)

    }

    private fun getNext(x: Double): Map.Entry<Double, Double>? {

        var nextHighest: Map.Entry<Double, Double>? = null

        for(k in data) {
            if(k.key >= x && ((nextHighest == null) || (k.key < nextHighest.key)))
                nextHighest = k
        }

        return nextHighest

    }

    private fun getPrevious(x: Double): Map.Entry<Double, Double>? {

        var nextLowest: Map.Entry<Double, Double>? = null

        for(k in data){
            if(k.key <= x && ((nextLowest == null) || (k.key > nextLowest.key))) {
                nextLowest = k
            }
        }

        return nextLowest

    }

}
