package frc.team6502.kyberlib.motorcontrol

import com.revrobotics.*
import com.revrobotics.CANSparkMaxLowLevel.MotorType
import frc.team6502.kyberlib.math.units.*

enum class SoftwarePIDMode {
    DISABLED,
    POSITION,
    VELOCITY
}

class KSparkMax(val canId: Int, val mtype: MotorType) : KMotorController {

    var radius: Length? = null

    private val _spark = CANSparkMax(canId, mtype)
    private var _enc: CANEncoder? = null
    private val _pid = _spark.pidController


    fun configEncoder(type: EncoderType, cpr: Int, reverse: Boolean) {
        if(mtype == MotorType.kBrushless) {
            _enc = _spark.encoder
        } else {
            _enc = _spark.getEncoder(type, cpr)
            if (reverse) _enc?.inverted = true
        }
    }

    init {
        _spark.restoreFactoryDefaults()
        if (mtype == MotorType.kBrushless) {
            configEncoder(EncoderType.kHallSensor, 0, false)
        }
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
            positionSetpoint = value
        }

    override var linearPositionSetpoint = 0.feet
        set(value) {
            requireNotNull(radius) { "Cannot set motor controller linearPosition without a defined radius" }
            positionSetpoint = value / radius!!
            field = value
        }

    override var linearPosition: Length
        get() {
            requireNotNull(radius) { "Cannot get motor controller linearPosition without a defined radius" }
            return _enc!!.position.rotations.times(radius!!)
        }
        set(value) {
            linearPositionSetpoint = value
        }

    override var velocitySetpoint: AngularVelocity = 0.rpm
        set(value) {
            _pid.setReference(value.rpm, ControlType.kVelocity)
            field = value
        }

    override var velocity: AngularVelocity
        get() = _enc!!.velocity.rpm
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

class KSparkMaxBuilder(id: Int, type: MotorType) {

    val s = KSparkMax(id, type)

    /**
     * Configures an external encoder. NEO internal encoders are automatically assumed if the motor type is brushless.
     * [sensorType] determines if the sensor is hall, quadrature, or analog, and [cpr] is the sensor's counts per revolution.
     */
    fun encoder(sensorType: EncoderType, cpr: Int, reverse: Boolean) {
        require(s.mtype != MotorType.kBrushless) { "Brushless motors must use their integrated encoder" }
        s.configEncoder(sensorType, cpr, reverse)
    }
    /**
     * Sets [kP], [kI], and [kD] gains for the motor controller
     */
    fun pid(kP: Double, kI: Double, kD: Double) {
        s.kP = kP
        s.kI = kI
        s.kD = kD
    }

    /**
     * Sets the output radius for the motor. Used to set distance instead of position
     */
    fun radius(r: Length) {
        s.radius = r
    }

    fun invert(i: Boolean) {
        s.inverted = i
    }

    fun brake(){
        s.brake = true
    }

    fun ff(feedForward: KFeedForward){
        s.feedForward = feedForward
    }

    fun build(): KSparkMax {
        return s
    }

}

fun sparkmax(id: Int, type: MotorType, init: KSparkMaxBuilder.() -> Unit): KSparkMax {
    return KSparkMaxBuilder(id, type).apply(init).build()
}

