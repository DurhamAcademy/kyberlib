package frc.team6502.kyberlib.lighting.animations

import java.awt.Color

abstract class LEDAnimation {
    abstract fun getBuffer(ticks: Int, length: Int): List<Color>
}
