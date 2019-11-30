package frc.team6502.kyberlib.lighting.animations

import edu.wpi.first.wpilibj.AddressableLEDBuffer
import java.awt.Color

abstract class LEDAnimation {
    abstract fun getBuffer(ticks: Int, length: Int): AddressableLEDBuffer
    protected fun bufferFromList(length: Int, list: List<Color>): AddressableLEDBuffer {
        val b = AddressableLEDBuffer(length)
        for (i in 0 until length) {
            b.setLED(i, list[i].red, list[i].green, list[i].blue)
        }
        return b
    }
}