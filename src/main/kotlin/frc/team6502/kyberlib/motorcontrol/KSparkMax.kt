package frc.team6502.kyberlib.motorcontrol

import com.revrobotics.*
import com.revrobotics.CANSparkMaxLowLevel.MotorType
import frc.team6502.kyberlib.math.units.*

class KSparkMax(val canId: Int, val type: MotorType, apply: KSparkMax.() -> Unit) : KMotorController {



    private val _spark = CANSparkMax(canId, type)
    private var _enc: CANEncoder? = null
    private val _pid = _spark.pidController




    fun configEncoder(type: EncoderType, cpr: Int, reverse: Boolean) {
        if(this.type == MotorType.kBrushless) {
            _enc = _spark.encoder
        } else {
            _enc = _spark.getEncoder(type, cpr)
            if (reverse) _enc?.inverted = true
        }
    }

    init {
        _spark.restoreFactoryDefaults()
        if (type == MotorType.kBrushless) {
            configEncoder(EncoderType.kHallSensor, 0, false)
        }
        this.apply(apply)
    }

    var inverted: Boolean
        get() = _spark.inverted
        set(value) {
            _spark.inverted = value
        }

    override var feedForward: KFeedForward? = null

    override var kP: Double
        get() = _pid.p
        set(value) {
            _pid.p = value
        }

    override var kI: Double
        get() = _pid.i
        set(value) {
            _pid.i = value
        }

    override var kD: Double
        get() = _pid.d
        set(value) {
            _pid.d = value
        }

    override var brake: Boolean
        get() = _spark.idleMode == CANSparkMax.IdleMode.kBrake
        set(value) {
            _spark.idleMode = when(value) {
                true -> CANSparkMax.IdleMode.kBrake
                false -> CANSparkMax.IdleMode.kCoast
            }
        }

    override var percentOutput: Double
        get() = _spark.appliedOutput
        set(value) {
            _spark.set(value)
        }

    override var positionSetpoint = 0.radians
        set(value) {
            if(feedForward != null) {
                _pid.setReference(value.rotations, ControlType.kPosition, 0, feedForward?.voltageAt(value.radians, position.radians) ?: 0.0, CANPIDController.ArbFFUnits.kVoltage)
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
            return _enc!!.position.rotations.times(radius!!) * (1/gearRatio)
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
        get() = _enc!!.velocity.rpm / gearRatio
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