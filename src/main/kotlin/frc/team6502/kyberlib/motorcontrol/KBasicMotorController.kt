package frc.team6502.kyberlib.motorcontrol

import edu.wpi.first.wpilibj.DriverStation

abstract class KBasicMotorController {

    var brakeMode: BrakeMode = false
        set(value) {
            field = value
            setBrakeMode(value)
        }

    var debug = false
    abstract var identifier: String

    abstract var percentOutput: Double
    open var feedForward: Double = 0.0
        set(value) {
            set(percentOutput - (feedForward / 12.0), value)
            field = value
        }

    var reversed: Boolean = false
        set(value) {
            field = value
            setReversed(value)
        }

    /**
     * Enables or disables the motor controller's braking.
     */
    internal abstract fun setBrakeMode(brakeMode: BrakeMode)

    /**
     * Sets the reversal of a motor controller
     */
    internal abstract fun setReversed(reversed: Boolean)

    internal abstract fun set(value: Double?, ff: Double)

    fun logError(text: String) {
        DriverStation.reportError("[$identifier] $text", false)
    }

    fun logDebug(text: String) {
        if (debug) println("[$identifier] $text")
    }
}