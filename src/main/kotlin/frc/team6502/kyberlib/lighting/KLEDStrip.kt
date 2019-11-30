package frc.team6502.kyberlib.lighting

import edu.wpi.first.wpilibj.AddressableLED
import frc.team6502.kyberlib.lighting.animations.LEDAnimation

class KLEDStrip(port: Int, private val length: Int, var animation: LEDAnimation) {

    private var ticks = 0
    private val addressableLED = AddressableLED(port)

    init {
        addressableLED.setLength(length)
        addressableLED.start()
    }

    fun update() {
        val buffer = animation.getBuffer(ticks, length)
        addressableLED.setData(buffer)
        ticks++
    }

}