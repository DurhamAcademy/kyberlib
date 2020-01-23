package frc.team6502.kyberlib.motorcontrol

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
abstract class KMotorController : KBasicMotorController() {

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

    var velocityMultipler = 1.0
        set(value) {
            field = value
            writeMultipler(velocityMultipler, positionMultipler)
        }

    var positionMultipler = 1.0
        set(value) {
            field = value
            writeMultipler(velocityMultipler, positionMultipler)
        }

    /**
     * Defines the relationship between rotation and linear motion for the motor.
     */
    var radius: Length? = null
        set(value) {
            linearConfigured = value != null
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
            writePid(kP, kI, kD)
        }

    /**
     * Integral gain of the PID controller.
     */
    var kI: Double = 0.0
        set(value) {
            field = value
            writePid(kP, kI, kD)
        }

    /**
     * Derivative gain of the PID controller.
     */
    var kD: Double = 0.0
        set(value) {
            field = value
            writePid(kP, kI, kD)
        }

    /**
     * Sets a current limit for the motor
     */
    var currentLimit: Int = 40
        set(value) {
            field = value
            writeCurrentLimit(value)
        }

    var position: Angle
        get() {
            if (!encoderConfigured) {
                logError("Cannot get position without a configured encoder")
                return 0.rotations
            }
            return readPosition() / gearRatio
        }
        set(value) {
            positionSetpoint = value
        }

    var linearPosition: Length
        get() {
            if (!linearConfigured) {
                logError("Cannot get linear position without a defined radius")
                return 0.feet
            }
            return position.toCircumference(radius!!)
        }
        set(value) {
            linearPositionSetpoint = value
        }

    var velocity: AngularVelocity
        get() {
            if (!encoderConfigured) {
                logError("Cannot get velocity without a configured encoder")
                return 0.rpm
            }
            return readVelocity() / gearRatio
        }
        set(value) {
            velocitySetpoint = value
        }

    var linearVelocity: LinearVelocity
        get() {
            if (!linearConfigured) {
                logError("Cannot get linear velocity without a defined radius")
                return 0.feetPerSecond
            }
            return velocity.toTangentialVelocity(radius!!)
        }
        set(value) {
            linearVelocitySetpoint = value
        }

    /**
     * Sets the angle to which the motor should go
     */
    var positionSetpoint: Angle = 0.rotations
        set(value) {
            if (!encoderConfigured) {
                logError("Cannot set position without a configured encoder")
            } else {
                field = value
                writePosition(value)
            }
        }

    /**
     * Sets the linear position to which the motor should go
     */
    var linearPositionSetpoint: Length = 0.meters
        set(value) {
            if (!encoderConfigured || !linearConfigured) {
                logError("Cannot set linear position without a defined radius")
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
                field = value
                writeVelocity(value)
            }
        }

    /**
     * Sets the linear velocity to which the motor should go
     */
    var linearVelocitySetpoint: LinearVelocity = 0.metersPerSecond
        set(value) {
            if (!encoderConfigured || !linearConfigured) {
                logError("Cannot set linear velocity without a defined radius")
            } else {
                field = value
                velocitySetpoint = value.toAngularVelocity(radius!!)
            }
        }

    protected abstract fun writePid(p: Double, i: Double, d: Double)

    protected abstract fun writeMultipler(mv: Double, mp: Double)

    protected abstract fun writePosition(position: Angle)

    protected abstract fun writeVelocity(vel: AngularVelocity)

    protected abstract fun readPosition(): Angle

    protected abstract fun readVelocity(): AngularVelocity

    protected abstract fun writeCurrentLimit(limit: Int)

    /**
     * Resets the encoder's position to zero
     */
    abstract fun zeroPosition()

    /**
     * Configures the respective ESC encoder settings when a new encoder configuration is set
     */
    internal abstract fun configureEncoder(config: KEncoderConfig): Boolean
}
