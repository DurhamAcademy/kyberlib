package frc.team6502.kyberlib.lighting

import edu.wpi.first.wpilibj.AddressableLED
import edu.wpi.first.wpilibj.AddressableLEDBuffer

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
        require(other.start > 0)
        require(other.end <= length)

        regions.add(other)
    }

    fun update() {
        val buffer = AddressableLEDBuffer(length)

        for (region in regions) {
            val b = region.getBuffer(ticks) ?: continue
            for (i in b.indices) {
                buffer.setLED(region.start + i, b[i].red, b[i].green, b[i].blue)
            }
        }

        addressableLED.setData(buffer)

        ticks++
    }

}