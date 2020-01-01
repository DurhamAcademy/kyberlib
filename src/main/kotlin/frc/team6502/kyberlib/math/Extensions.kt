package frc.team6502.kyberlib.math

import kotlin.math.absoluteValue
import kotlin.math.roundToInt

val Double.rtf: Double get() = (this * 10000).roundToInt() / 10000.0

// conditional inversion
fun Double.invertIf(condition: () -> Boolean) = this * if (condition()) -1.0 else 1.0

fun Int.invertIf(condition: () -> Boolean) = this * if (condition()) -1 else 1

infix fun Double.epsilonEquals(value: Double): Boolean {
    return (this - value).absoluteValue < 0.00001
}
