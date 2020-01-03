package frc.team6502.kyberlib.lighting.animations

import java.awt.Color
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sin

class AnimationBlink(val color: Color, val ticksPerCycle: Int) : LEDAnimation() {

    override fun getBuffer(ticks: Int, length: Int): List<Color> {
        val brightness = ((sin(2 * PI * (ticks % ticksPerCycle) / ticksPerCycle.toDouble()) + 1) / 2).roundToInt()
        return Array(length) {
            Color(color.red / 255F, color.green / 255F, color.blue / 255F, brightness.toFloat())
        }.toMutableList()
    }
}
