package frc.team6502.kyberlib.lighting.animations

import edu.wpi.first.wpilibj.AddressableLEDBuffer
import java.awt.Color
import kotlin.math.ceil

class AnimationRain(private val color: Color, private val dropLength: Int, val ticksPerMovement: Int, val reversed: Boolean = false) : LEDAnimation() {

    fun constructInitialBuffer(length: Int): MutableList<Color> {

        return Array<Color>(dropLength * ceil(length.toDouble() / dropLength).toInt()) {
            Color(color.red * (it % dropLength) / (dropLength - 1), color.green * (it % dropLength) / (dropLength - 1), color.blue * (it % dropLength) / (dropLength - 1))
        }.asList().toMutableList()

    }

    override fun getBuffer(ticks: Int, length: Int): AddressableLEDBuffer {
        val b = constructInitialBuffer(length)

        for (i in 0 until (ticks / ticksPerMovement) % dropLength) {
            b.add(0, b.removeAt(b.size - 1))
        }

        if (reversed) b.reverse()

        return bufferFromList(length, b)
    }

}