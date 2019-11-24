package frc.team6502.kyberlib.motorcontrol

import edu.wpi.first.wpilibj.Notifier
import frc.team6502.kyberlib.math.units.Angle
import frc.team6502.kyberlib.math.units.AngularVelocity
import frc.team6502.kyberlib.math.units.Length
import frc.team6502.kyberlib.math.units.LinearVelocity

typealias GearRatio = Double
typealias BrakeMode = Boolean

const val FOLLOW_PERIOD = 0.02

enum class EncoderType {
    NONE,
    HALL,
    CTRE_MAG
}

data class KEncoderConfig(val cpr: Int, val type: EncoderType)

abstract class KMotorController: KBasicMotorController() {

    // Status of various configurable properties
    private var linearConfigured = false

    protected val closedLoopConfigured
        get() = (encoderConfig.type != EncoderType.NONE && encoderConfig.cpr > 0) &&
                (kP != 0.0 || kI != 0.0 || kD != 0.0)

    var feedForward: KFeedForward? = null

    var encoderConfig: KEncoderConfig = KEncoderConfig(0, EncoderType.NONE)

    var radius: Length? = null
        set(value) {
            linearConfigured = true
            field = value
        }

    var gearRatio: GearRatio = 1.0

    var kP: Double = 0.0
        set(value) {
            field = value
            updatePIDGains(kP, kI, kD)
        }
    var kI: Double = 0.0
        set(value) {
            field = value
            updatePIDGains(kP, kI, kD)
        }
    var kD: Double = 0.0
        set(value) {
            field = value
            updatePIDGains(kP, kI, kD)
        }
    
    abstract var positionSetpoint: Angle
    abstract var position: Angle
    abstract var linearPositionSetpoint: Length
    abstract var linearPosition: Length
    abstract var velocitySetpoint: AngularVelocity
    abstract var velocity: AngularVelocity
    abstract var linearVelocitySetpoint: LinearVelocity
    abstract var linearVelocity: LinearVelocity

    private val followerNotifier = Notifier {
        updateFollowers()
    }

    var followers: Array<KBasicMotorController> = arrayOf()
    set(value) {
        field = value
        if(value.isNotEmpty()) {
            followerNotifier.startPeriodic(FOLLOW_PERIOD)
        } else {
            followerNotifier.stop()
        }
    }

    /**
     * Updates the gains of the motor controller's PID controller.
     */
    internal abstract fun updatePIDGains(kP: Double, kI: Double, kD: Double)

    /**
     * Makes all followers follow the master
     */
    internal abstract fun updateFollowers()
}