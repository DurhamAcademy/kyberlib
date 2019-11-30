package frc.team6502.kyberlib.lighting.animations

import java.awt.Color
import kotlin.math.ceil

class AnimationRGBRain(private val cycles: Double = 1.0, private val dropLength: Int, val ticksPerMovement: Int, val reversed: Boolean = false) : LEDAnimation() {

    fun constructInitialBuffer(length: Int): MutableList<Color> {

        return Array<Color>(dropLength * ceil(length.toDouble() / dropLength).toInt()) {
            Color(255 * (it % dropLength) / (dropLength - 1), 255 * (it % dropLength) / (dropLength - 1), 255 * (it % dropLength) / (dropLength - 1))
        }.toMutableList()

    }

    fun constructRGBBuffer(length: Int): MutableList<Color> {
        return if (cycles >= 1) {
            Array<Color>(length) {
                val hue = (it % (length / cycles)) / (length / cycles)
                Color.getHSBColor(hue.toFloat(), 1F, 1F)
            }.asList().toMutableList()
        } else {
            Array<Color>((length / cycles).toInt()) {
                val hue = it / (length / cycles)
                Color.getHSBColor(hue.toFloat(), 1F, 1F)
            }.asList().toMutableList()
        }
    }

    override fun getBuffer(ticks: Int, length: Int): List<Color> {
        val b = constructInitialBuffer(length)
        val rgb = constructRGBBuffer(length)

        for (i in 0 until (ticks / ticksPerMovement) % dropLength) {
            b.add(0, b.removeAt(b.size - 1))
        }

        for (i in b.indices) {
            b[i] = Color((b[i].red * rgb[i].red / 255.0).toInt(), (b[i].green * rgb[i].green / 255.0).toInt(), (b[i].blue * rgb[i].blue / 255.0).toInt())
        }

        if (reversed) b.reverse()

        return b.take(length)
    }

}