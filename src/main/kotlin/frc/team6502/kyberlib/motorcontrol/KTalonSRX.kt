package frc.team6502.kyberlib.motorcontrol

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX

class KTalonSRX(val canId: Int) : KMotorController {

    private val _talon = TalonSRX(canId)

    override var percentOutput: Double
        get() = _talon.motorOutputPercent
        set(value) {
            _talon.set(ControlMode.PercentOutput, value)
        }
}