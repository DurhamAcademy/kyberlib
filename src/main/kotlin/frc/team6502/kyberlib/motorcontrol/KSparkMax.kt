package frc.team6502.kyberlib.motorcontrol

import com.revrobotics.CANEncoder
import com.revrobotics.CANPIDController
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel.MotorType
import com.revrobotics.ControlType
import frc.team6502.kyberlib.CANId
import frc.team6502.kyberlib.CANRegistry
import frc.team6502.kyberlib.math.invertIf
import frc.team6502.kyberlib.math.units.*
import frc.team6502.kyberlib.motorcontrol.MotorType.*

class KSparkMax(val canId: CANId, val motorType: frc.team6502.kyberlib.motorcontrol.MotorType, apply: KMotorController.() -> Unit) : KMotorController() {

    override var identifier: String = CANRegistry.filterValues { it == canId }.keys.firstOrNull() ?: "can$canId"

    private val _spark = CANSparkMax(canId, when (motorType) {
        BRUSHLESS -> MotorType.kBrushless
        BRUSHED -> MotorType.kBrushed
    })

    private var _enc: CANEncoder? = null

    init {
        _spark.restoreFactoryDefaults()

        // running NEO with integrated encoder
        if (motorType == BRUSHLESS) {
            encoderConfig = KEncoderConfig(42, EncoderType.NEO_HALL)
        }
    }

    override fun set(value: Double) {
        _spark.set(value / 12.0)
    }

    override fun updateFollowers() {
        for (follower in followers) {
            follower.percentOutput = _spark.appliedOutput.invertIf { follower.reversed != this.reversed }
        }
    }

    override fun setBrakeMode(brakeMode: BrakeMode) {
        _spark.idleMode = when (brakeMode) {
            true -> CANSparkMax.IdleMode.kBrake
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
            else -> {
                false
            }
        }
    }

    override var appliedOutput: Double
        get() = _spark.appliedOutput
        set(value) {
            percentOutput = value
        }

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
        _enc?.position = 0.0
    }

}