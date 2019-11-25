package frc.team6502.kyberlib.math

val Double.rtf: Double get() = Math.round(this*10000)/10000.0

// conditional inversion
fun Double.invertIf(condition: () -> Boolean) = this * if(condition()) -1.0 else 1.0
fun Int.invertIf(condition: () -> Boolean) = this * if(condition()) -1 else 1