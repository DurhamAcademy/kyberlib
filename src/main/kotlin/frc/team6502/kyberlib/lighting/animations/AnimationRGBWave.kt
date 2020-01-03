package frc.team6502.kyberlib.lighting.animations

import java.awt.Color

class AnimationRGBWave(private val cycles: Double = 1.0, val ticksPerMovement: Int = 10, val reversed: Boolean = false) : LEDAnimation() {

    fun constructInitialBuffer(length: Int): MutableList<Color> {

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

        for (i in 0 until (ticks / ticksPerMovement) % b.size) {
            b.add(0, b.removeAt(b.size - 1))
        }

        if (reversed) b.reverse()

        return b.take(length)
    }
}
