package frc.team6502.kyberlib.math.units

import frc.team6502.kyberlib.math.epsilonEquals
import kotlin.math.absoluteValue

/*
 * Unit system inspired by those of FalconLibrary and SaturnLibrary
 */

inline class KUnit<T>(val value: Double) : Comparable<KUnit<T>> {
    operator fun plus(other: KUnit<T>) = KUnit<T>(value + other.value)
    operator fun minus(other: KUnit<T>) = KUnit<T>(value - other.value)
    operator fun times(other: Double) = KUnit<T>(value * other)
    operator fun div(other: Double) = KUnit<T>(value / other)
    operator fun div(other: KUnit<T>) = value / other.value

    val absoluteValue get() = KUnit<T>(value.absoluteValue)
    override fun compareTo(other: KUnit<T>) = value.compareTo(other.value)

    infix fun epsilonEquals(other: KUnit<T>) = value epsilonEquals other.value
}

operator fun <T : KUnitKey, U : KUnitKey> KUnit<T>.times(other: KUnit<U>) = KUnit<Mul<T, U>>(value * other.value)
operator fun <T : KUnitKey, U : KUnitKey> KUnit<T>.div(other: KUnit<U>) = KUnit<Div<T, U>>(value * other.value)
