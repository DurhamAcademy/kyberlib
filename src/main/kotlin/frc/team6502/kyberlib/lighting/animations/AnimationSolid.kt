package frc.team6502.kyberlib.lighting.animations

import edu.wpi.first.wpilibj.AddressableLEDBuffer
import java.awt.Color

class AnimationSolid(val color: Color) : LEDAnimation() {


    override fun getBuffer(ticks: Int, length: Int): AddressableLEDBuffer {
        val buffer = AddressableLEDBuffer(length)

        for (i in 0 until buffer.length) {
            buffer.setLED(i, color.red, color.green, color.blue)
        }

        return buffer
    }

}