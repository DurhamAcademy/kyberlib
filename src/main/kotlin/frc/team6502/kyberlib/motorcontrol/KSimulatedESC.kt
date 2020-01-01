package frc.team6502.kyberlib.motorcontrol

import frc.team6502.kyberlib.math.units.extensions.*
import kotlin.math.withSign

class KSimulatedESC(val name: String, apply: KSimulatedESC.() -> Unit = {}): KMotorController() {

    init {
        this.apply(apply)
    }

    override var appliedOutput: Double = 0.0

    override var position: Angle = 0.degrees

    override var linearPosition: Length = 0.meters

    override var velocity: AngularVelocity = 0.rpm

    override var linearVelocity: LinearVelocity = 0.metersPerSecond

    override fun zeroPosition() {
        position = 0.degrees
    }

    override fun configureEncoder(config: KEncoderConfig): Boolean {
        return true
    }

    override val identifier
        get() = name

    override fun setBrakeMode(brakeMode: BrakeMode) {

    }

    override fun setReversed(reversed: Boolean) {

    }

    override fun set(value: Double) {
        appliedOutput = (value / 12.0).withSign(value * if(reversed && !isFollower) -1 else 1)
    }

}