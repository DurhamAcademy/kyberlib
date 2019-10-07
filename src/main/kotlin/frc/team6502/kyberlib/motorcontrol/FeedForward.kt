package frc.team6502.kyberlib.motorcontrol

interface KFeedForward {
    fun voltageAt(setpoint: Double, current: Double): Double
}

/**
 * A feedforward which always applies a constant voltage
 */
class KConstantFeedForward(private val voltage: Double) : KFeedForward {
    override fun voltageAt(setpoint: Double, current: Double) = voltage
}

/**
 * A feedforward which uses an arbitrary user-defined lambda to process inputs.
 * Units are radians, feet, and per second in the case of velocity
 */
class KCustomFeedForward(private val f: (setpoint: Double, current: Double) -> Double): KFeedForward{
    override fun voltageAt(setpoint: Double, current: Double) = f(setpoint, current)
}

/**
 * A feedforward which applies velocity characterization for a mechanism like a drivetrain
 * It follows the formula V = kV * setpoint(ft/s or rad/s) +/- intercept
 */
class KCharacterizedFeedForward(private val kV: Double, private val intercept: Double) : KFeedForward {
    override fun voltageAt(setpoint: Double, current: Double): Double {
        return when {
            setpoint > 0.0 -> (kV * setpoint + intercept)
            setpoint < 0.0 -> (kV * setpoint - intercept)
            else -> 0.0
        }
    }
}