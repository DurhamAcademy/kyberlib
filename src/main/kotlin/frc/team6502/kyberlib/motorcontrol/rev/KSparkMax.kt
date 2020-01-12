package frc.team6502.kyberlib.motorcontrol.rev

import com.revrobotics.CANEncoder
import com.revrobotics.CANPIDController
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel.MotorType
import com.revrobotics.ControlType
import edu.wpi.first.wpilibj.RobotController
import frc.team6502.kyberlib.math.units.extensions.Angle
import frc.team6502.kyberlib.math.units.extensions.AngularVelocity
import frc.team6502.kyberlib.math.units.extensions.rotations
import frc.team6502.kyberlib.math.units.extensions.rpm
import frc.team6502.kyberlib.motorcontrol.*
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
        BRUSHED -> MotorType.kBrushed
    })

    private var _enc: CANEncoder? = null
    private val _pid = _spark.pidController

    init {
        _spark.restoreFactoryDefaults()

        // running NEO with integrated encoder
        if (motorType == BRUSHLESS) {
            encoderConfig = KEncoderConfig(42, EncoderType.NEO_HALL)
        }

        this.apply(apply)
    }

    override fun configureEncoder(config: KEncoderConfig): Boolean {
        return when {
            config.type == EncoderType.NEO_HALL && motorType == BRUSHLESS -> {
                _enc = _spark.encoder
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

    override fun writeBrakeMode(brakeMode: BrakeMode) {
        _spark.idleMode = when (brakeMode) {
            true -> CANSparkMax.IdleMode.kBrake
            false -> CANSparkMax.IdleMode.kCoast
        }
    }

    override fun writePercent(value: Double) {
        _spark.set(value + (feedForward?.asDouble?.div(RobotController.getBatteryVoltage()) ?: 0.0))
    }

    override fun readPercent(): Double = _spark.appliedOutput

    override fun writeReversed(reversed: Boolean) {
        _spark.inverted = reversed
    }

    override fun writePid(p: Double, i: Double, d: Double) {
        _pid.p = p
        _pid.i = i
        _pid.d = d
    }

    override fun writePosition(position: Angle) {
        _pid.setReference(position.rotations, ControlType.kPosition, 0, feedForward?.asDouble ?: 0.0, CANPIDController.ArbFFUnits.kVoltage)
    }

    override fun writeVelocity(vel: AngularVelocity) {
        _pid.setReference(vel.rpm, ControlType.kVelocity, 0, feedForward?.asDouble ?: 0.0, CANPIDController.ArbFFUnits.kVoltage)
    }

    override fun readPosition() = _enc!!.position.rotations

    override fun readVelocity() = _enc!!.velocity.rpm

    override fun followTarget(kmc: KBasicMotorController) {
        if (kmc is KSparkMax) {
            _spark.follow(kmc._spark, reversed)
        } else {
            kmc.followers.add(this)
            kmc.notifier.startPeriodic(0.005)
        }
    }

    override fun zeroPosition() {
        if (!encoderConfigured) {
            return logError("Cannot reset encoder position without configured encoder")
        }
        _enc?.position = 0.0
    }
}
