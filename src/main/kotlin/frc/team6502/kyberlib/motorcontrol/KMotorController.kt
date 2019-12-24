package frc.team6502.kyberlib.motorcontrol

import edu.wpi.first.wpilibj.controller.PIDController
import frc.team6502.kyberlib.math.units.*

typealias GearRatio = Double
typealias BrakeMode = Boolean

enum class EncoderType {
    NONE, NEO_HALL, QUADRATURE
}

enum class ClosedLoopMode {
    VELOCITY, POSITION, NONE
}

enum class MotorType {
    BRUSHLESS, BRUSHED
}

data class KEncoderConfig(val cpr: Int, val type: EncoderType, val reversed: Boolean = false)

abstract class KMotorController: KBasicMotorController() {

    protected val pidController = PIDController(0.0, 0.0, 0.0)

    // Status of various configurable properties
    protected var linearConfigured = false

    protected val encoderConfigured
        get() = (encoderConfig.type != EncoderType.NONE && encoderConfig.cpr > 0)

    protected val closedLoopConfigured
        get() = encoderConfigured && (kP != 0.0 || kI != 0.0 || kD != 0.0)

    private var closedLoopMode = ClosedLoopMode.NONE

    var encoderConfig: KEncoderConfig = KEncoderConfig(0, EncoderType.NONE)
        set(value) {
            if (configureEncoder(value)) {
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
            pidController.p = value
        }
    var kI: Double = 0.0
        set(value) {
            field = value
            pidController.i = value
        }
    var kD: Double = 0.0
        set(value) {
            field = value
            pidController.d = value
        }


    abstract var position: Angle
    abstract var linearPosition: Length
    abstract var velocity: AngularVelocity
    abstract var linearVelocity: LinearVelocity

    override var percentOutput: Double = 0.0
        set(value) {
            closedLoopMode = ClosedLoopMode.NONE
            field = value
        }

    var positionSetpoint: Angle = 0.rotations
        set(value) {
            if(!encoderConfigured){
               logError("Cannot set position without a configured encoder")
            } else {
                closedLoopMode = ClosedLoopMode.POSITION
                field = value
            }
        }

    var linearPositionSetpoint: Length = 0.meters
        set(value) {
            if(!encoderConfigured || !linearConfigured){
                logError("Cannot set linear position without a configured encoder and radius")
            } else {
                field = value
                positionSetpoint = value / radius!!
            }
        }

    var velocitySetpoint: AngularVelocity = 0.rpm
        set(value) {
            if(!encoderConfigured){
                logError("Cannot set velocity without a configured encoder")
            } else {
                closedLoopMode = ClosedLoopMode.VELOCITY
                field = value
            }
        }

    var linearVelocitySetpoint: LinearVelocity = 0.metersPerSecond
        set(value) {
            if(!encoderConfigured || !linearConfigured){
                logError("Cannot set linear velocity without a configured encoder and radius")
            } else {
                field = value
                velocitySetpoint = value / radius!!
            }
        }

    override fun update(){

        // perform PID computations
        when(closedLoopMode){
            ClosedLoopMode.POSITION -> {
                val output = pidController.calculate(position.rotations, positionSetpoint.rotations)
                set(output * 12.0 + feedForward)
            }
            ClosedLoopMode.VELOCITY -> {
                val output = pidController.calculate(velocity.rpm, velocitySetpoint.rpm)
                set(output * 12.0 + feedForward)
            }
            else -> {
                // don't use PID
                set(percentOutput * 12 + feedForward)
            }
        }

        updateFollowers()
    }

    abstract fun zeroPosition()

    internal abstract fun configureEncoder(config: KEncoderConfig): Boolean
}