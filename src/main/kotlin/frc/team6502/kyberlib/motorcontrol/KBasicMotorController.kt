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

    abstract var appliedOutput: Double
    abstract var percentOutput: Double
    var feedForward: Double = 0.0

    var followers: Array<KBasicMotorController> = arrayOf()

    var reversed: Boolean = false
        set(value) {
            field = value
            setReversed(value)
        }

    open fun update() {
        set(percentOutput * 12 + feedForward)
        updateFollowers()
    }

    /**
     * Enables or disables the motor controller's braking.
     */
    internal abstract fun setBrakeMode(brakeMode: BrakeMode)

    /**
     * Sets the reversal of a motor controller
     */
    internal abstract fun setReversed(reversed: Boolean)

    internal abstract fun set(value: Double)

    /**
     * Makes all followers follow the master
     */
    internal abstract fun updateFollowers()

    fun logError(text: String) {
        DriverStation.reportError("[$identifier] $text", false)
    }

    fun logDebug(text: String) {
        if (debug) println("[$identifier] $text")
    }
}