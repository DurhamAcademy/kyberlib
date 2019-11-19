package frc.team6502.kyberlib.math

import edu.wpi.first.wpilibj.Timer

class Differentiator {

    private var lastFPGA = Timer.getFPGATimestamp()
    private var lastValue: Double? = null

    /**
     * Return the rate of change of value in units per second
     */
    fun calculate(value: Double): Double {
        val v = if(lastValue != null) (value - lastValue!!) / (Timer.getFPGATimestamp() - lastFPGA) else 0.0
        lastValue = value
        lastFPGA = Timer.getFPGATimestamp()
        return v
    }
}