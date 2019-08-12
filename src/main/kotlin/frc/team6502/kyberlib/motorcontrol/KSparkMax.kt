package frc.team6502.kyberlib.motorcontrol

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel.MotorType
import com.revrobotics.ControlType
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.RobotBase
import frc.team6502.kyberlib.math.units.*

class KSparkMax(canId: Int, type: MotorType) : KMotorController {

    var radius: Length? = null

    private val _spark = CANSparkMax(canId, type)
    private val _pid = _spark.pidController
    private val _enc = _spark.encoder

    init {
        _spark.restoreFactoryDefaults()
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

    override var percentOutput: Double
        get() = _spark.appliedOutput
        set(value) {
            _spark.set(value)
        }

    override var rotationSetpoint = 0.radians
        set(value) {
            _pid.setReference(value.rotations, ControlType.kPosition)
            field = value
        }

    override var rotation: Angle
        get() = _enc.position.rotations
        set(value) {
            rotationSetpoint = value
        }

    override var positionSetpoint = 0.feet
        set(value) {
            if (radius == null) {
                DriverStation.reportWarning("Cannot set motor controller position without a defined radius", false)
            } else {
                _pid.setReference((value / radius!!).rotations, ControlType.kPosition)
                field = value
            }
        }

    override var position: Length
        get() {
            return if (radius == null) {
                DriverStation.reportWarning("Cannot get motor controller position without a defined radius", false)
                0.inches
            } else {
                _enc.position.rotations.times(radius!!)
            }
        }
        set(value) {
            positionSetpoint = value
        }

}

class KSparkMaxBuilder(id: Int, type: MotorType) {

    val s = KSparkMax(id, type)

    /**
     * Sets [kP], [kI], and [kD] gains for the motor controller
     */
    fun pid(kP: Double, kI: Double, kD: Double) {
        s.kP = kP
        s.kI = kI
        s.kD = kD
    }

    /**
     * Sets the output radius for the motor. Used to set distance instead of rotation
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

