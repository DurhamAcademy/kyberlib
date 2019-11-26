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

class KSparkMax(canId: CANId, motorType: frc.team6502.kyberlib.motorcontrol.MotorType, apply: KMotorController.() -> Unit) : KMotorController(canId, motorType, apply) {

    override var identifier: String = CANRegistry.filterValues { it == canId }.keys.firstOrNull() ?: "can$canId"

    override fun set(value: Double?, ff: Double) {
        when (closedLoopMode) {
            ClosedLoopMode.VELOCITY -> _pid.setReference(value ?: velocitySetpoint.rpm, ControlType.kVelocity, 0, ff, CANPIDController.ArbFFUnits.kVoltage)
            ClosedLoopMode.POSITION -> _pid.setReference(value ?: positionSetpoint.rotations, ControlType.kPosition, 0, ff, CANPIDController.ArbFFUnits.kVoltage)
            ClosedLoopMode.NONE -> _spark.set((value ?: 0.0) + ff / 12.0)
        }
    }

    override fun updatePIDGains(kP: Double, kI: Double, kD: Double) {
        _pid.p = kP
        _pid.i = kI
        _pid.d = kD
    }

    override fun updateFollowers() {
        for (follower in followers) {
            if (follower is KSparkMax) {
                // use native follow if not already
                if (!follower._spark.isFollower) follower._spark.follow(_spark, follower.reversed != this.reversed)
            } else {
                // use software follow
                follower.percentOutput = _spark.appliedOutput.invertIf { follower.reversed != this.reversed }
            }
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

    private val _spark = CANSparkMax(canId, when (motorType) {
        BRUSHLESS -> MotorType.kBrushless
        BRUSHED -> MotorType.kBrushed
    })
    private var _enc: CANEncoder? = null
    private val _pid: CANPIDController by lazy { _spark.pidController }

    init {
        _spark.restoreFactoryDefaults()

        // running NEO with integrated encoder
        if (motorType == BRUSHLESS) {
            encoderConfig = KEncoderConfig(42, EncoderType.NEO_HALL)
        }
    }

    override var percentOutput: Double
        get() = _spark.appliedOutput
        set(value) {
            set(value, feedForward)
        }

    override var positionSetpoint = 0.radians
        set(value) {
            if (!closedLoopConfigured) {
                logError("Cannot set position without a configured encoder")
                return
            }
            closedLoopMode = ClosedLoopMode.POSITION
            set(value.rotations * gearRatio, feedForward)
            field = value
        }

    override var position: Angle
        get() {
            if (!closedLoopConfigured) {
                logError("Cannot get position without a configured encoder")
                return 0.rotations
            }
            return _enc!!.position.rotations
        }
        set(value) {
            positionSetpoint = value
        }

    override var linearPositionSetpoint = 0.feet
        set(value) {
            if (!linearConfigured) {
                logError("Cannot set linear position without a defined radius")
                return
            }
            positionSetpoint = value / radius!!
            field = value
        }

    override var linearPosition: Length
        get() {
            if (!linearConfigured) {
                logError("Cannot get linear position without a defined radius")
                return 0.feet
            }
            return _enc!!.position.rotations.times(radius!!) * (1 / gearRatio)
        }
        set(value) {
            linearPositionSetpoint = value
        }

    override var velocitySetpoint: AngularVelocity = 0.rpm
        set(value) {
            if (!closedLoopConfigured) {
                logError("Cannot set velocity without a configured encoder")
                return
            }
            field = value
            closedLoopMode = ClosedLoopMode.VELOCITY
            set(value.rpm, feedForward)
        }

    override var velocity: AngularVelocity
        get() {
            if (!closedLoopConfigured) {
                logError("Cannot set velocity without a configured encoder")
                return 0.rpm
            }
            return _enc!!.velocity.rpm * (1 / gearRatio)
        }
        set(value) {
            velocitySetpoint = value
        }

    override var linearVelocitySetpoint: LinearVelocity = 0.feetPerSecond
        set(value) {
            if (!linearConfigured) {
                logError("Cannot set linear velocity without a defined radius")
                return
            }
            velocitySetpoint = value / radius!!
            field = value
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

}