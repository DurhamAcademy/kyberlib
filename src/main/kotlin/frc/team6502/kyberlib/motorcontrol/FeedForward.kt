package frc.team6502.kyberlib.motorcontrol

/*
interface KFeedForward {
    posSupplier: (kmc: KMotorController) -> Double = {kmc.position.radians}, velSupplier: (kmc: KMotorController) -> Double = {kmc.velocity.radiansPerSecond}
    fun calculate(kmc: KMotorController): Double
}

/**
 * A feedforward which always applies a constant voltage
 */
class KConstantFeedForward(private val ks: Double) : KFeedForward {
    override fun calculate(kmc: KMotorController, posSupplier: (kmc: KMotorController) -> Double, velSupplier: (kmc: KMotorController) -> Double): Double {
        return ks
    }
}

/**
 * A feedforward which has a gravity term dependent on angle.
 */
class KCosineFeedForward(val ks: Double, val kcos: Double, val kv: Double, val ka: Double): KFeedForward {
    private val diff = Differentiator()
    override fun calculate(kmc: KMotorController): Double {
        return ks * kmc.velocity.radiansPerSecond.sign + kcos * kmc.position.cos + kv * kmc.velocity.radiansPerSecond + ka * diff.calculate(kmc.velocity.radiansPerSecond);
    }
}

/**
 * A feedforward which applies velocity characterization for a mechanism like a drivetrain
 * It follows the formula V = kV * setpoint(ft/s or rad/s) +/- intercept
 */
class KSimpleFeedForward(val ks: Double, val kv: Double, val ka: Double) : KFeedForward {
    private val diff = Differentiator()
    override fun calculate(kmc: KMotorController, posSupplier: (kmc: KMotorController) -> Double, velSupplier: (kmc: KMotorController) -> Double): Double {
        val vel = velSupplier(kmc)
        return ks * vel.sign + kv * vel + ka * diff.calculate(vel);
    }
}
*/
