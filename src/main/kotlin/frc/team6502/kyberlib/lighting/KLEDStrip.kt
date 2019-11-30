package frc.team6502.kyberlib.lighting

import edu.wpi.first.wpilibj.AddressableLED
import edu.wpi.first.wpilibj.AddressableLEDBuffer
import java.awt.Color

class KLEDStrip(port: Int, private val length: Int) {

    companion object {
        private var singleton: KLEDStrip? = null
    }

    private val addressableLED = AddressableLED(port)
    private val regions = arrayListOf<KLEDRegion>()

    private var ticks = 0

    init {
        require(singleton == null) { "Only one LED strip may be used at once" }
        addressableLED.setLength(length)
        addressableLED.start()
    }

    operator fun plusAssign(other: KLEDRegion) {
        require(other.end > other.start)
        require(other.start >= 0)
        require(other.end <= length)

        regions.add(other)
    }

    fun update() {
        val mutableBuffer = Array<Color>(length) { Color.BLACK }
        val buffer = AddressableLEDBuffer(length)

        for (region in regions) {
            val b = region.getBuffer(ticks) ?: continue
            for (i in b.indices) {
                if (b[i].alpha < 1 && region.enableTransparency) {
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

        for (i in mutableBuffer.indices) {
            buffer.setLED(i, mutableBuffer[i].red, mutableBuffer[i].green, mutableBuffer[i].blue)
        }

        addressableLED.setData(buffer)

        ticks++
    }

}