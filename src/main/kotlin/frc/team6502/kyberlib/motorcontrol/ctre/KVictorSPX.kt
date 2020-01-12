package frc.team6502.kyberlib.motorcontrol.ctre

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.VictorSPX
import frc.team6502.kyberlib.motorcontrol.*

class KVictorSPX(val canId: CANId, apply: KBasicMotorController.() -> Unit) : KBasicMotorController() {

    private val _victor = VictorSPX(canId)

    constructor(canKey: CANKey, apply: KBasicMotorController.() -> Unit) : this(CANRegistry[canKey]!!, apply)

    init {
        this.apply(apply)
    }

    override val identifier = CANRegistry.filterValues { it == canId }.keys.firstOrNull() ?: "can$canId"

    override fun followTarget(kmc: KBasicMotorController) {
        if (kmc is KVictorSPX) {
            _victor.follow(kmc._victor)
        } else {
            if (kmc.followers.size == 0) kmc.notifier.startPeriodic(0.005)
            kmc.followers.add(this)
        }
    }

    override fun writeBrakeMode(brakeMode: BrakeMode) {
        _victor.setNeutralMode(
            when (brakeMode) {
                true -> NeutralMode.Brake
                false -> NeutralMode.Coast
            }
        )
    }

    override fun writeReversed(reversed: Boolean) {
        _victor.inverted = reversed
    }

    override fun writePercent(value: Double) {
        _victor.set(ControlMode.PercentOutput, value)
    }

    override fun readPercent() = _victor.motorOutputPercent
}
