package frc.team6502.kyberlib.motorcontrol

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.Notifier
import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj.RobotController
import frc.team6502.kyberlib.math.invertIf
import java.util.function.DoubleSupplier

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
            writeBrakeMode(value)
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
    val appliedOutput: Double
        get() = readPercent()

    /**
     * Sets the percentage of vBus that should be output to the motor (open-loop)
     */
    var percentOutput: Double = 0.0
        set(value) {
            field = value
            writePercent(value)
        }

    /**
     * Sets controller voltage directly
     */
    var voltage: Double = 0.0
        set(value) {
            field = value
            val vbus = if (RobotBase.isReal()) RobotController.getBatteryVoltage() else 12.0
            writePercent(value / vbus)
        }

    /**
     * Sets an additional arbitrary feedforward to be added to whatever the motor should normally output
     */
    var feedForward: DoubleSupplier? = null

    /**
     * True if this motor is following another.
     */
    var isFollower = false
        protected set

    operator fun plusAssign(kmc: KBasicMotorController) {
        kmc.follow(this)
    }

    internal val followers = arrayListOf<KBasicMotorController>()
    fun follow(kmc: KBasicMotorController) {
        isFollower = true
        followTarget(kmc)
    }

    protected abstract fun followTarget(kmc: KBasicMotorController)

    /**
     * Determines if the motor should run in the opposite direction
     */
    var reversed: Boolean = false
        set(value) {
            field = value
            writeReversed(value)
        }

    /**
     * Internal update function
     */
    fun update() {
        updateFollowers()
    }

    protected fun updateFollowers() {
        for (follower in followers) {
            // follower.percentOutput = appliedOutput.invertIf { follower.reversed }
            // follower.update()
        }
    }

    /**
     * Enables or disables the respective motor controller's braking.
     */
    protected abstract fun writeBrakeMode(brakeMode: BrakeMode)

    /**
     * Sets the internal reversal of the respective motor controller
     */
    protected abstract fun writeReversed(reversed: Boolean)

    /**
     * Sends a raw voltage to the respective motor controller
     */
    internal abstract fun writePercent(value: Double)

    protected abstract fun readPercent(): Double

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
