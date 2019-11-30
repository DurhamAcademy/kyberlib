package frc.team6502.kyberlib.lighting.animations

import java.awt.Color

class AnimationRGBWave(private val cycles: Int = 1, val ticksPerMovement: Int, val reversed: Boolean = false) : LEDAnimation() {

    fun constructInitialBuffer(length: Int): MutableList<Color> {

        return Array<Color>(length) {
            val hue = (it % (length / cycles)) / (length / cycles).toFloat()
            Color.getHSBColor(hue, 1F, 1F)
        }.asList().toMutableList()

    }

    override fun getBuffer(ticks: Int, length: Int): List<Color> {
        val b = constructInitialBuffer(length)

        for (i in 0 until (ticks / ticksPerMovement) % length) {
            b.add(0, b.removeAt(b.size - 1))
        }

        if (reversed) b.reverse()

        return b
    }

}