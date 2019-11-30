package frc.team6502.kyberlib.lighting

import frc.team6502.kyberlib.lighting.animations.LEDAnimation
import java.awt.Color

class KLEDRegion(val animation: LEDAnimation, val start: Int, val end: Int, val enableTransparency: Boolean = false, val condition: () -> Boolean = { true }) {

    fun getBuffer(ticks: Int): List<Color>? {
        if (!condition()) return null
        val buf = animation.getBuffer(ticks, end - start).toMutableList()
        if (!enableTransparency) {
            // if transparency isn't enabled, premultiply the alpha
            for (i in buf.indices) {
                buf[i] = Color((buf[i].red / 255F) * (buf[i].alpha / 255F), (buf[i].green / 255F) * (buf[i].alpha / 255F), (buf[i].blue / 255F) * (buf[i].alpha / 255F))
            }
        }

        return buf
    }

}