package frc.team6502.kyberlib.lighting

import frc.team6502.kyberlib.lighting.animations.LEDAnimation
import java.awt.Color

class KLEDRegion(val animation: LEDAnimation, val start: Int, val end: Int, val condition: () -> Boolean = { true }) {

    fun getBuffer(ticks: Int): List<Color>? {
        if (!condition()) return null
        return animation.getBuffer(ticks, end - start)
    }

}