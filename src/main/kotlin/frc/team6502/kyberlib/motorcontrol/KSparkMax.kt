package frc.team6502.kyberlib.motorcontrol

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel.MotorType
import com.revrobotics.ControlType

class KSparkMax(canId: Int, type: MotorType) : KMotorController {



    private val _spark = CANSparkMax(canId, type)
    private val _pid = _spark.pidController

    init {
        _spark.restoreFactoryDefaults()
    }

    override var percentOutput: Double
        get() = _spark.appliedOutput
        set(value) {
            _spark.set(value)
        }

}