package frc.team6502.kyberlib.motorcontrol

import edu.wpi.first.wpilibj.Notifier
import frc.team6502.kyberlib.CANId
import frc.team6502.kyberlib.CANKey
import frc.team6502.kyberlib.CANRegistry
import frc.team6502.kyberlib.math.units.Angle
import frc.team6502.kyberlib.math.units.AngularVelocity
import frc.team6502.kyberlib.math.units.Length
import frc.team6502.kyberlib.math.units.LinearVelocity

typealias GearRatio = Double
typealias BrakeMode = Boolean

const val FOLLOW_PERIOD = 0.02

enum class EncoderType {
    NONE,
    NEO_HALL,
    QUADRATURE
}

enum class MotorType {
    BRUSHLESS,
    BRUSHED
}

data class KEncoderConfig(val cpr: Int, val type: EncoderType, val reversed: Boolean = false)

abstract class KMotorController(val canId: CANId, val motorType: MotorType, apply: KMotorController.() -> Unit): KBasicMotorController() {

    init {
        this.apply(apply)
    }

    // Status of various configurable properties
    private var linearConfigured = false

    protected val closedLoopConfigured
        get() = (encoderConfig.type != EncoderType.NONE && encoderConfig.cpr > 0) &&
                (kP != 0.0 || kI != 0.0 || kD != 0.0)

    var feedForward: KFeedForward? = null

    var encoderConfig: KEncoderConfig = KEncoderConfig(0, EncoderType.NONE)
    set(value) {
        if(configureEncoder(value)) {
            field = value
        } else {
            System.err.println("Invalid encoder configuration")
        }
    }

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

    internal abstract fun configureEncoder(config: KEncoderConfig): Boolean
}