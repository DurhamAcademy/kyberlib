package frc.team6502.kyberlib.motorcontrol

interface KFeedForward {
    fun percentAt(value: Double): Double
}

class KConstantFeedForward(private val percentage: Double) : KFeedForward {
    override fun percentAt(value: Double) = percentage
}

class KCharacterizedFeedForward(private val kV: Double, private val intercept: Double) : KFeedForward {
    override fun percentAt(value: Double): Double {
        return when {
            value > 0.0 -> (kV * value + intercept) / 12.0
            value < 0.0 -> (kV * value - intercept) / 12.0
            else -> 0.0
        }
    }
}