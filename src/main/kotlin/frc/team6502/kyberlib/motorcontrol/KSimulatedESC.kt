package frc.team6502.kyberlib.motorcontrol

import frc.team6502.kyberlib.math.invertIf
import frc.team6502.kyberlib.math.units.*

class KSimulatedESC(apply: KSimulatedESC.() -> Unit = {}): KMotorController() {

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

    override fun updateFollowers() {
        for (follower in followers) {
            follower.percentOutput = this.appliedOutput.invertIf { follower.reversed != this.reversed }
            follower.update()
        }
    }

    override fun configureEncoder(config: KEncoderConfig): Boolean {
        return true
    }

    override var identifier: String = "TEST"

    override fun setBrakeMode(brakeMode: BrakeMode) {

    }

    override fun setReversed(reversed: Boolean) {

    }

    override fun set(value: Double) {
        appliedOutput = value / 12.0
    }

}