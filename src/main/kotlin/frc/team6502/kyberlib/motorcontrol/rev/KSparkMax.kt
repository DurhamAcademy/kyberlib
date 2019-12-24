package frc.team6502.kyberlib.motorcontrol.rev

import com.revrobotics.CANEncoder
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel.MotorType
import frc.team6502.kyberlib.CANId
import frc.team6502.kyberlib.CANKey
import frc.team6502.kyberlib.CANRegistry
import frc.team6502.kyberlib.math.units.*
import frc.team6502.kyberlib.motorcontrol.BrakeMode
import frc.team6502.kyberlib.motorcontrol.EncoderType
import frc.team6502.kyberlib.motorcontrol.KEncoderConfig
import frc.team6502.kyberlib.motorcontrol.KMotorController
import frc.team6502.kyberlib.motorcontrol.MotorType.BRUSHED
import frc.team6502.kyberlib.motorcontrol.MotorType.BRUSHLESS

/**
 * Represents a REV Robotics Spark MAX motor controller.
 * [canId] is the controller's ID on the CAN bus
 * [motorType] is the type of motor being driven. WARNING: If set incorrectly this can seriously damage hardware. You've been warned.
 * [apply] is where motor setup can occur
 */
class KSparkMax(val canId: CANId, val motorType: frc.team6502.kyberlib.motorcontrol.MotorType, apply: KMotorController.() -> Unit) : KMotorController() {

    constructor(canKey: CANKey, motorType: frc.team6502.kyberlib.motorcontrol.MotorType, apply: KMotorController.() -> Unit) : this(CANRegistry[canKey]!!, motorType, apply)

    override val identifier: String = CANRegistry.filterValues { it == canId }.keys.firstOrNull() ?: "can$canId"

    private val _spark = CANSparkMax(canId, when (motorType) {
        BRUSHLESS -> MotorType.kBrushless
        BRUSHED   -> MotorType.kBrushed
    })

    private var _enc: CANEncoder? = null

    init {
        _spark.restoreFactoryDefaults()

        // running NEO with integrated encoder
        if (motorType == BRUSHLESS) {
            encoderConfig = KEncoderConfig(42, EncoderType.NEO_HALL)
        }

        this.apply(apply)
        notifier.startPeriodic(0.005)
    }

    override fun set(value: Double) {
        _spark.set(value / 12.0)
    }

    override fun setBrakeMode(brakeMode: BrakeMode) {
        _spark.idleMode = when (brakeMode) {
            true  -> CANSparkMax.IdleMode.kBrake
            false -> CANSparkMax.IdleMode.kCoast
        }
    }

    override fun setReversed(reversed: Boolean) {
        _spark.inverted = reversed
    }

    override fun configureEncoder(config: KEncoderConfig): Boolean {
        return when {
            config.type == EncoderType.NEO_HALL && motorType == BRUSHLESS -> {
                _enc = _spark.encoder
                _enc?.inverted = config.reversed
                true
            }
            config.type == EncoderType.QUADRATURE && motorType == BRUSHED -> {
                _enc = _spark.getEncoder(com.revrobotics.EncoderType.kQuadrature, config.cpr)
                _enc?.inverted = config.reversed
                true
            }
            else                                                          -> {
                false
            }
        }
    }

    override val appliedOutput: Double
        get() = _spark.appliedOutput

    override var position: Angle
        get() {
            if (!closedLoopConfigured) {
                logError("Cannot get position without a configured encoder")
                return 0.rotations
            }
            return _enc!!.position.rotations * (1 / gearRatio)
        }
        set(value) {
            positionSetpoint = value
        }

    override var linearPosition: Length
        get() {
            if (!linearConfigured) {
                logError("Cannot get linear position without a defined radius")
                return 0.feet
            }
            return (_enc!!.position.rotations * radius!!) * (1/gearRatio)
        }
        set(value) {
            linearPositionSetpoint = value
        }

    override var velocity: AngularVelocity
        get() {
            if (!closedLoopConfigured) {
                logError("Cannot set velocity without a configured encoder")
                return 0.rpm
            }
            return _enc!!.velocity.rpm * (1/gearRatio)
        }
        set(value) {
            velocitySetpoint = value
        }

    override var linearVelocity: LinearVelocity
        get() {
            if (!linearConfigured) {
                logError("Cannot get linear velocity without a defined radius")
                return 0.feetPerSecond
            }
            return _enc!!.velocity.rpm * radius!!
        }
        set(value) {
            linearVelocitySetpoint = value
        }

    override fun zeroPosition() {
        if (!encoderConfigured) {
            return logError("Cannot reset encoder position without configured encoder")
        }
        _enc?.position = 0.0
    }

}