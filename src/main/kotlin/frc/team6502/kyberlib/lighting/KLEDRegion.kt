package frc.team6502.kyberlib.lighting

import frc.team6502.kyberlib.lighting.animations.LEDAnimation
import java.awt.Color

class KLEDRegion(val animation: LEDAnimation, val start: Int, val end: Int, val enableTransparency: Boolean = false, val condition: () -> Boolean = { true }) {

    companion object {
        fun composite(length: Int, ticks: Int, regions: List<KLEDRegion>): Array<Color> {
            val mutableBuffer = Array<Color>(length) { Color.BLACK }

            for (region in regions) {
                val b = region.getBuffer(ticks) ?: continue
                for (i in b.indices) {
                    if (b[i].alpha < 255 && region.enableTransparency) {
                        mutableBuffer[region.start + i] = Color(
                                (b[i].red / 255F) * (b[i].alpha / 255F) + (mutableBuffer[region.start + i].red / 255F) * (1 - b[i].alpha / 255F),
                                (b[i].green / 255F) * (b[i].alpha / 255F) + (mutableBuffer[region.start + i].green / 255F) * (1 - b[i].alpha / 255F),
                                (b[i].blue / 255F) * (b[i].alpha / 255F) + (mutableBuffer[region.start + i].blue / 255F) * (1 - b[i].alpha / 255F)
                        )
                    } else {
                        mutableBuffer[region.start + i] = Color(b[i].red, b[i].green, b[i].blue)
                    }
                }
            }

            return mutableBuffer
        }
    }

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
