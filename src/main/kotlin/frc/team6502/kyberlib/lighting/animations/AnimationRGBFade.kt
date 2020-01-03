package frc.team6502.kyberlib.lighting.animations

import java.awt.Color

class AnimationRGBFade(val ticksPerCycle: Int) : LEDAnimation() {

    override fun getBuffer(ticks: Int, length: Int): List<Color> {
        return Array<Color>(length) {
            Color.getHSBColor(((ticks % ticksPerCycle).toFloat() / ticksPerCycle) % 1F, 1F, 1F)
        }.toMutableList()
    }
}
