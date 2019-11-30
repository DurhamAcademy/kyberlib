package frc.team6502.kyberlib.lighting.animations

import java.awt.Color
import kotlin.math.ceil

class AnimationRGBRain(private val cycles: Double = 1.0, private val dropLength: Int, val ticksPerMovement: Int, val reversed: Boolean = false) : LEDAnimation() {

    fun constructInitialBuffer(length: Int): MutableList<Color> {

        return Array<Color>(dropLength * ceil(length.toDouble() / dropLength).toInt()) {
            val alpha = (it % dropLength) / (dropLength - 1).toFloat()
            Color(255F, 255F, 255F, alpha)
        }.toMutableList()

    }

    fun constructRGBBuffer(length: Int): MutableList<Color> {
        return if (cycles >= 1) {
            Array<Color>(length) {
                val hue = (it % (length / cycles)) / (length / cycles)
                Color.getHSBColor(1 - hue.toFloat(), 1F, 1F)
            }.asList().toMutableList()
        } else {
            Array<Color>((length / cycles).toInt()) {
                val hue = it / (length / cycles)
                Color.getHSBColor(1 - hue.toFloat(), 1F, 1F)
            }.asList().toMutableList()
        }
    }

    override fun getBuffer(ticks: Int, length: Int): List<Color> {
        val b = constructInitialBuffer(length)
        val rgb = constructRGBBuffer(length)

//        println(rgb.size)

        for (i in 0 until (ticks / ticksPerMovement) % (dropLength * rgb.size)) {
            b.add(0, b.removeAt(b.size - 1))
            rgb.add(0, rgb.removeAt(rgb.size - 1))
        }

        if (reversed) {
            b.reverse()
            rgb.reverse()
        }

        val trimmed = b.take(length).toMutableList()
        for (i in trimmed.indices) {
            trimmed[i] = Color((trimmed[i].red * rgb[i].red / 255.0).toInt(), (trimmed[i].green * rgb[i].green / 255.0).toInt(), (trimmed[i].blue * rgb[i].blue / 255.0).toInt(), trimmed[i].alpha)
        }

        return trimmed
    }

}