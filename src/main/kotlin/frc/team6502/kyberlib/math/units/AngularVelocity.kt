package frc.team6502.kyberlib.math.units

import kotlin.math.PI
import kotlin.math.absoluteValue

val Number.radiansPerSecond get() = AngularVelocity(toDouble())
val Number.degreesPerSecond get() = AngularVelocity(toDouble() * AngularVelocity.DEGREES_TO_RADIANS)
val Number.rpm get() = AngularVelocity(toDouble() * AngularVelocity.ROTATIONS_TO_RADIANS * AngularVelocity.SECOND_TO_MINUTE)
fun Number.encoderVelocity(cpr: Int) = AngularVelocity((toDouble() / (cpr * 4.0)) * AngularVelocity.ROTATIONS_TO_RADIANS * 10)

data class AngularVelocity(private val _radiansPerSecond: Double) {
    companion object {
        const val DEGREES_TO_RADIANS = PI / 180.0
        const val ROTATIONS_TO_RADIANS = PI * 2.0
        const val SECOND_TO_MINUTE = 1 / 60.0
    }

    operator fun times(other: Length) = LinearVelocity(other.feet * _radiansPerSecond)
    operator fun times(other: Double) = AngularVelocity(other * _radiansPerSecond)

    val radiansPerSecond get() = _radiansPerSecond
    val degreesPerSecond get() = _radiansPerSecond / DEGREES_TO_RADIANS
    val rpm get() = _radiansPerSecond / (ROTATIONS_TO_RADIANS * SECOND_TO_MINUTE)
    fun encoderVelocity(cpr: Int) = (_radiansPerSecond / (ROTATIONS_TO_RADIANS * 10)) * (cpr * 4.0)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AngularVelocity

        return (_radiansPerSecond - other._radiansPerSecond).absoluteValue < 0.01
    }

    override fun hashCode(): Int {
        return _radiansPerSecond.hashCode()
    }
}