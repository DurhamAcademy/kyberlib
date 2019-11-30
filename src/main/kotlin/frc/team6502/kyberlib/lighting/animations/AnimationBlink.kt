package frc.team6502.kyberlib.lighting.animations

import java.awt.Color
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sin

class AnimationBlink(val color: Color, val ticksPerCycle: Double) : LEDAnimation() {

    override fun getBuffer(ticks: Int, length: Int): List<Color> {
        val brightness = ((sin(2 * PI * (ticks % ticksPerCycle) / ticksPerCycle) + 1) / 2).roundToInt()
        return Array(length) {
            Color((color.red * brightness), (color.green * brightness), (color.blue * brightness))
        }.toMutableList()
    }

}