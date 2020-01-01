package frc.team6502.kyberlib.motorcontrol

import edu.wpi.first.wpilibj.controller.PIDController
import frc.team6502.kyberlib.math.units.extensions.*

typealias GearRatio = Double
typealias BrakeMode = Boolean

/**
 * Types of encoders that may be used
 */
enum class EncoderType {
    NONE, NEO_HALL, QUADRATURE
}

/**
 * Different methods of motor control
 */
enum class ClosedLoopMode {
    VELOCITY, POSITION, NONE
}

/**
 * Defines types of motors that can be used
 */
enum class MotorType {
    BRUSHLESS, BRUSHED
}

/**
 * Stores data about an encoder. [reversed] means the encoder reading goes + when the motor is applied - voltage.
 */
data class KEncoderConfig(val cpr: Int, val type: EncoderType, val reversed: Boolean = false)

/**
 * A more advanced motor control with feedback control.
 */
abstract class KMotorController: KBasicMotorController() {

    /**
     * The motor controller's PID controller. Runs rio-side.
     */
    protected val pidController = PIDController(0.0, 0.0, 0.0)

    /**
     * Does the motor controller have a rotational to linear motion conversion defined? (i.e. wheel radius)
     * Allows for linear setpoints to be used.
     */
    protected var linearConfigured = false

    /**
     * Does the motor controller have an encoder configured?
     * Allows for closed-loop control methods to be used
     */
    protected val encoderConfigured
        get() = (encoderConfig.type != EncoderType.NONE && encoderConfig.cpr > 0)

    /**
     * Does the motor have closed-loop gains set?
     * Allows for closed-loop control methods to be used
     */
    protected val closedLoopConfigured
        get() = encoderConfigured && (kP != 0.0 || kI != 0.0 || kD != 0.0)

    /**
     * The current closed loop mode (position, velocity, or none.)
     * Determines what inputs the PID controller takes
     */
    private var closedLoopMode = ClosedLoopMode.NONE

    /**
     * Settings relevant to the motor controller's encoder.
     */
    var encoderConfig: KEncoderConfig = KEncoderConfig(0, EncoderType.NONE)
        set(value) {
            if (configureEncoder(value)) {
                field = value
            } else {
                System.err.println("Invalid encoder configuration")
            }
        }

    /**
     * Defines the relationship between rotation and linear motion for the motor.
     */
    var radius: Length? = null
        set(value) {
            linearConfigured = true
            field = value
        }

    /**
     * Adds post-encoder gearing to allow for post-geared speeds to be set.
     */
    var gearRatio: GearRatio = 1.0

    /**
     * Proportional gain of the PID controller.
     */
    var kP: Double = 0.0
        set(value) {
            field = value
            pidController.p = value
        }

    /**
     * Integral gain of the PID controller.
     */
    var kI: Double = 0.0
        set(value) {
            field = value
            pidController.i = value
        }

    /**
     * Derivative gain of the PID controller.
     */
    var kD: Double = 0.0
        set(value) {
            field = value
            pidController.d = value
        }

    /**
     * The current position of the motor
     */
    abstract var position: Angle

    /**
     * The current linear position of the motor
     */
    abstract var linearPosition: Length

    /**
     * The current velocity of the motor
     */
    abstract var velocity: AngularVelocity

    /**
     * The current linear velocity of the motor
     */
    abstract var linearVelocity: LinearVelocity

    /**
     * Sets the percentage of vBus that should be output to the motor (open-loop)
     */
    override var percentOutput: Double = 0.0
        set(value) {
            closedLoopMode = ClosedLoopMode.NONE
            field = value
        }

    /**
     * Sets the angle to which the motor should go
     */
    var positionSetpoint: Angle = 0.rotations
        set(value) {
            if (!encoderConfigured) {
                logError("Cannot set position without a configured encoder")
            } else {
                closedLoopMode = ClosedLoopMode.POSITION
                field = value
            }
        }

    /**
     * Sets the linear position to which the motor should go
     */
    var linearPositionSetpoint: Length = 0.meters
        set(value) {
            if (!encoderConfigured || !linearConfigured) {
                logError("Cannot set linear position without a configured encoder and radius")
            } else {
                field = value
                positionSetpoint = value.toAngle(radius!!)
            }
        }

    /**
     * Sets the velocity to which the motor should go
     */
    var velocitySetpoint: AngularVelocity = 0.rpm
        set(value) {
            if (!encoderConfigured) {
                logError("Cannot set velocity without a configured encoder")
            } else {
                closedLoopMode = ClosedLoopMode.VELOCITY
                field = value
            }
        }

    /**
     * Sets the linear velocity to which the motor should go
     */
    var linearVelocitySetpoint: LinearVelocity = 0.metersPerSecond
        set(value) {
            if (!encoderConfigured || !linearConfigured) {
                logError("Cannot set linear velocity without a configured encoder and radius")
            } else {
                field = value
                velocitySetpoint = value.toAngularVelocity(radius!!)
            }
        }

    /**
     * Internal update method, computes feedback and sets it to motor controller & followers
     */
    override fun update() {

        // perform PID computations
        when (closedLoopMode) {
            ClosedLoopMode.POSITION -> {
                val output = pidController.calculate(position.rotations, positionSetpoint.rotations)
                set(output * 12.0 + feedForward)
            }
            ClosedLoopMode.VELOCITY -> {
                val output = pidController.calculate(velocity.rpm, velocitySetpoint.rpm)
                set(output * 12.0 + feedForward)
            }
            else                    -> {
                // don't use PID
                set(percentOutput * 12 + feedForward)
            }
        }

        updateFollowers()
    }


    /**
     * Resets the encoder's position to zero
     */
    abstract fun zeroPosition()

    /**
     * Configures the respective ESC encoder settings when a new encoder configuration is set
     */
    internal abstract fun configureEncoder(config: KEncoderConfig): Boolean
}