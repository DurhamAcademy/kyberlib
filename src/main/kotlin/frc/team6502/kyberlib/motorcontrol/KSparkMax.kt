package frc.team6502.kyberlib.motorcontrol

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel.MotorType
import com.revrobotics.ControlType
import com.revrobotics.SensorType
import edu.wpi.first.wpilibj.CounterBase
import edu.wpi.first.wpilibj.Encoder
import edu.wpi.first.wpilibj.Notifier
import edu.wpi.first.wpilibj.controller.PIDController
import frc.team6502.kyberlib.math.units.*

enum class SoftwarePIDMode {
    DISABLED,
    POSITION,
    VELOCITY
}

class KSparkMax(val canId: Int, val type: MotorType) : KMotorController {

    var radius: Length? = null

    private val _spark = CANSparkMax(canId, type)
    private var _enc = _spark.getEncoder(SensorType.kNoSensor, 0)
    private var _rioEncoder: Encoder? = null
    private val _pid = _spark.pidController

    // SOFTWARE PID
    private val _softwarePid = PIDController(0.0, 0.0, 0.0, 0.02)
    private var softwarePIDMode = SoftwarePIDMode.DISABLED
    private val _softwarePidNotifier = Notifier {
        if (_rioEncoder != null) {
            when (softwarePIDMode) {
                SoftwarePIDMode.POSITION -> _spark.set(_softwarePid.calculate(_rioEncoder!!.distance))
                SoftwarePIDMode.VELOCITY -> _spark.set(_softwarePid.calculate(_rioEncoder!!.rate))
                SoftwarePIDMode.DISABLED -> {
                }
            }
        }
    }

    fun configEncoder(type: SensorType, cpr: Int, reverse: Boolean) {
        _enc = _spark.getEncoder(type, cpr)
        _enc.inverted = reverse
    }

    fun configRioEncoder(a: Int, b: Int, reverse: Boolean, cpr: Int) {
        _rioEncoder = Encoder(a, b, reverse, CounterBase.EncodingType.k4X)
        _rioEncoder?.distancePerPulse = 1.0 / cpr
        _softwarePidNotifier.startPeriodic(0.02)
    }

    init {
        _spark.restoreFactoryDefaults()
        if (type == MotorType.kBrushless) {
            configEncoder(SensorType.kHallSensor, 0, false)
        }
    }

    var inverted: Boolean
        get() = _spark.inverted
        set(value) {
            _spark.inverted = value
        }

    override var kP: Double
        get() = _pid.p
        set(value) {
            _pid.p = value
            _softwarePid.p = value
        }

    override var kI: Double
        get() = _pid.i
        set(value) {
            _pid.i = value
            _softwarePid.i = value
        }

    override var kD: Double
        get() = _pid.d
        set(value) {
            _pid.d = value
            _softwarePid.d = value
        }

    override var percentOutput: Double
        get() = _spark.appliedOutput
        set(value) {
            softwarePIDMode = SoftwarePIDMode.DISABLED
            _spark.set(value)
        }

    override var positionSetpoint = 0.radians
        set(value) {
            softwarePIDMode = SoftwarePIDMode.POSITION
            if (_rioEncoder == null) _pid.setReference(value.rotations, ControlType.kPosition)
            field = value
        }

    override var position: Angle
        get() = _enc.position.rotations
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
            return _enc.position.rotations.times(radius!!)
        }
        set(value) {
            linearPositionSetpoint = value
        }

    override var velocitySetpoint: AngularVelocity = 0.rpm
        set(value) {
            softwarePIDMode = SoftwarePIDMode.VELOCITY
            if (_rioEncoder == null) _pid.setReference(value.rpm, ControlType.kVelocity)
            field = value
        }

    override var velocity: AngularVelocity
        get() = _enc.velocity.rpm
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
            return _enc.velocity.rpm * radius!!
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
    fun encoder(sensorType: SensorType, cpr: Int, reverse: Boolean) {
        require(s.type != MotorType.kBrushless) { "Brushless motors must use their integrated encoder" }
        s.configEncoder(sensorType, cpr, reverse)
    }

    /**
     * Configures an encoder connected to the roboRIO's DIO pins on [channelA] and [channelB].
     */
    fun encoder(channelA: Int, channelB: Int, reverse: Boolean, cpr: Int) {
        s.configRioEncoder(channelA, channelB, reverse, cpr)
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

    fun build(): KSparkMax {
        return s
    }

}

fun sparkmax(id: Int, type: MotorType, init: KSparkMaxBuilder.() -> Unit): KSparkMax {
    return KSparkMaxBuilder(id, type).apply(init).build()
}

