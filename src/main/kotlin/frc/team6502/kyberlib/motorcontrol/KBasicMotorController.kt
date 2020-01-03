package frc.team6502.kyberlib.motorcontrol

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.Notifier
import frc.team6502.kyberlib.math.invertIf

/**
 * A basic motor controller.
 */
abstract class KBasicMotorController {

    /**
     * Controls how the motor will stop when set to 0. If true the motor will brake instead of coast.
     */
    var brakeMode: BrakeMode = false
        set(value) {
            field = value
            setBrakeMode(value)
        }

    val notifier = Notifier { update() }

    /**
     * If enabled, the motor controller will print additional information to stdout.
     */
    var debug = false

    /**
     * The prefix used by this motor for logging of errors and debug information.
     */
    abstract val identifier: String

    /**
     * What percent output is currently being applied?
     */
    abstract val appliedOutput: Double

    /**
     * Sets the percentage of vBus that should be output to the motor (open-loop)
     */
    abstract var percentOutput: Double

    /**
     * Sets an additional arbitrary feedforward to be added to whatever the motor should normally output
     */
    var feedForward: Double = 0.0

    /**
     * True if this motor is following another.
     */
    var isFollower = false
        internal set

    /**
     * Motors following this one
     */
    var followers: Array<KBasicMotorController> = arrayOf()
        private set

    operator fun plusAssign(kbmc: KBasicMotorController) {
        kbmc.isFollower = true
        kbmc.notifier.stop()
        followers += kbmc
    }

    /**
     * Determines if the motor should run in the opposite direction
     */
    var reversed: Boolean = false
        set(value) {
            field = value
            setReversed(value)
        }

    /**
     * Internal update function
     */
    open fun update() {
        set(percentOutput * 12 + feedForward)
        updateFollowers()
    }

    protected fun updateFollowers() {
        for (follower in followers) {
            follower.percentOutput = appliedOutput.invertIf { follower.reversed }
            follower.update()
        }
    }

    /**
     * Enables or disables the respective motor controller's braking.
     */
    internal abstract fun setBrakeMode(brakeMode: BrakeMode)

    /**
     * Sets the internal reversal of the respective motor controller
     */
    internal abstract fun setReversed(reversed: Boolean)

    /**
     * Sends a raw voltage to the respective motor controller
     */
    internal abstract fun set(value: Double)

    /**
     * Logs an error to the driver station window
     */
    fun logError(text: String) {
        DriverStation.reportError("[$identifier] $text", false)
    }

    /**
     * Logs debug information to the driver station window if debug=true
     */
    fun logDebug(text: String) {
        if (debug) println("[$identifier] $text")
    }
}
