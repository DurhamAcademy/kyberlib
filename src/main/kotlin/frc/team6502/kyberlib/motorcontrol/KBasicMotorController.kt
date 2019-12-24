package frc.team6502.kyberlib.motorcontrol

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.Notifier
import frc.team6502.kyberlib.math.invertIf

abstract class KBasicMotorController {

    var brakeMode: BrakeMode = false
        set(value) {
            field = value
            setBrakeMode(value)
        }


    val notifier = Notifier { update() }

    var debug = false

    abstract val identifier: String

    abstract var appliedOutput: Double
    abstract var percentOutput: Double
    var feedForward: Double = 0.0

    var isFollower = false
        private set
    var followers: Array<KBasicMotorController> = arrayOf()
        private set

    operator fun plusAssign(kbmc: KBasicMotorController) {
        kbmc.isFollower = true
        kbmc.notifier.stop()
        followers += kbmc
    }

    var reversed: Boolean = false
        set(value) {
            field = value
            setReversed(value)
        }

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
     * Enables or disables the motor controller's braking.
     */
    internal abstract fun setBrakeMode(brakeMode: BrakeMode)

    /**
     * Sets the reversal of a motor controller
     */
    internal abstract fun setReversed(reversed: Boolean)

    internal abstract fun set(value: Double)

    fun logError(text: String) {
        DriverStation.reportError("[$identifier] $text", false)
    }

    fun logDebug(text: String) {
        if (debug) println("[$identifier] $text")
    }
}