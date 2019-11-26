package frc.team6502.kyberlib.math.units

import kotlin.math.absoluteValue

val Number.feetPerSecond get() = LinearVelocity(toDouble())
val Number.metersPerSecond get() = LinearVelocity(toDouble() * Length.METER_TO_FEET)
val Number.mph get() = LinearVelocity(toDouble() * LinearVelocity.MPH_TO_FPS)

data class LinearVelocity(private val _feetPerSecond: Double) {
    companion object {
        const val MPH_TO_FPS = 1 / 0.681818
    }

    val feetPerSecond get() = _feetPerSecond
    val metersPerSecond get() = _feetPerSecond / Length.METER_TO_FEET
    val mph get() = _feetPerSecond / MPH_TO_FPS

    operator fun div(other: Length) = AngularVelocity(_feetPerSecond / other.feet)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LinearVelocity

        return (_feetPerSecond - other._feetPerSecond).absoluteValue < 0.01
    }

    override fun hashCode(): Int {
        return _feetPerSecond.hashCode()
    }
}