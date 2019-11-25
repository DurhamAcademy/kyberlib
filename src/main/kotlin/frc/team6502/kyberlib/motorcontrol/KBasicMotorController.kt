package frc.team6502.kyberlib.motorcontrol

import frc.team6502.kyberlib.CANId

abstract class KBasicMotorController {

    var brakeMode: BrakeMode = false
        set(value) {
            field = value
            setBrakeMode(value)
        }

    abstract var percentOutput: Double

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
}