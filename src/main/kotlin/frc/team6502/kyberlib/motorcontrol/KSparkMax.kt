package frc.team6502.kyberlib.motorcontrol

import com.revrobotics.CANEncoder
import com.revrobotics.CANPIDController
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel.MotorType
import com.revrobotics.ControlType
import frc.team6502.kyberlib.CANId
import frc.team6502.kyberlib.math.invertIf
import frc.team6502.kyberlib.math.units.*
import frc.team6502.kyberlib.motorcontrol.MotorType.BRUSHED
import frc.team6502.kyberlib.motorcontrol.MotorType.BRUSHLESS

class KSparkMax(canId: CANId, motorType: frc.team6502.kyberlib.motorcontrol.MotorType, apply: KMotorController.() -> Unit) : KMotorController(canId, motorType, apply) {

    override fun updatePIDGains(kP: Double, kI: Double, kD: Double) {
        _pid.p = kP
        _pid.i = kI
        _pid.d = kD
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

    private val _spark = CANSparkMax(canId, when (motorType) {
        BRUSHLESS -> MotorType.kBrushless
        BRUSHED -> MotorType.kBrushed
    })
    private var _enc: CANEncoder? = null
    private val _pid = _spark.pidController

    init {
        _spark.restoreFactoryDefaults()

        if (motorType == BRUSHLESS) {
            encoderConfig = KEncoderConfig(42, EncoderType.NEO_HALL)
        }
    }

    override var percentOutput: Double
        get() = _spark.appliedOutput
        set(value) {
            _spark.set(value)
        }

    override var positionSetpoint = 0.radians
        set(value) {
            if (feedForward != null) {
                _pid.setReference(value.rotations, ControlType.kPosition, 0, feedForward?.voltageAt(value.radians, position.radians)
                        ?: 0.0, CANPIDController.ArbFFUnits.kVoltage)
            } else {
                _pid.setReference(value.rotations, ControlType.kPosition)
            }
            field = value
        }

    override var position: Angle
        get() = _enc!!.position.rotations
        set(value) {
            positionSetpoint = value * gearRatio
        }

    override var linearPositionSetpoint = 0.feet
        set(value) {
            requireNotNull(radius) { "Cannot set motor controller linearPosition without a defined radius" }
            positionSetpoint = (value * gearRatio) / radius!!
            field = value
        }

    override var linearPosition: Length
        get() {
            requireNotNull(radius) { "Cannot get motor controller linearPosition without a defined radius" }
            return _enc!!.position.rotations.times(radius!!) * (1 / gearRatio)
        }
        set(value) {
            linearPositionSetpoint = value
        }

    override var velocitySetpoint: AngularVelocity = 0.rpm
        set(value) {
            _pid.setReference(value.rpm * gearRatio, ControlType.kVelocity)
            field = value
        }

    override var velocity: AngularVelocity
        get() = _enc!!.velocity.rpm * (1 / gearRatio)
        set(value) {
            velocitySetpoint = value
        }

    override var linearVelocitySetpoint: LinearVelocity = 0.feetPerSecond
        set(value) {
            requireNotNull(radius) { "Cannot set motor controller linearVelocity without a defined radius" }
            velocitySetpoint = value / radius!!
            field = value
        }
    override var linearVelocity: LinearVelocity
        get() {
            requireNotNull(radius) { "Cannot get motor controller linearVelocity without a defined radius" }
            return _enc!!.velocity.rpm * radius!!
        }
        set(value) {
            linearVelocitySetpoint = value
        }

}