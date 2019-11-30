package frc.team6502.kyberlib.lighting.animations

import edu.wpi.first.wpilibj.AddressableLEDBuffer
import java.awt.Color

class AnimationRGBFade(val ticksPerCycle: Int) : LEDAnimation() {


    override fun getBuffer(ticks: Int, length: Int): AddressableLEDBuffer {
        val buffer = AddressableLEDBuffer(length)
        val color = Color.getHSBColor(((ticks % ticksPerCycle).toFloat() / ticksPerCycle) % 1F, 1F, 0.5F)

        for (i in 0 until buffer.length) {
            buffer.setLED(i, color.red, color.green, color.blue)
        }

        return buffer
    }

}