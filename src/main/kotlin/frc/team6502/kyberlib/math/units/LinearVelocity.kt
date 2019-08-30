package frc.team6502.kyberlib.math.units

import kotlin.math.absoluteValue

val Number.feetPerSecond get() = LinearVelocity(toDouble())

class LinearVelocity(private val _feetPerSecond: Double) {

    val feetPerSecond get() = _feetPerSecond

    operator fun div(other: Length) = AngularVelocity(_feetPerSecond / other.feet)


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LinearVelocity

        if ((_feetPerSecond - other._feetPerSecond).absoluteValue < 0.00001) return false

        return true
    }
}