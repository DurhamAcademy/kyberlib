package frc.team6502.kyberlib.math.units

import kotlin.math.PI

val Number.radiansPerSecond get() = AngularVelocity(toDouble())
val Number.degreesPerSecond get() = AngularVelocity(toDouble() * AngularVelocity.DEGREES_TO_RADIANS)
val Number.rpm get() = AngularVelocity(toDouble() * AngularVelocity.ROTATIONS_TO_RADIANS * AngularVelocity.MINUTE_TO_SECOND)
fun Number.encoderVelocity(cpr: Int) = AngularVelocity((toDouble() / (cpr * 4.0)) * AngularVelocity.ROTATIONS_TO_RADIANS * 10)

class AngularVelocity(private val _radiansPerSecond: Double) {
    companion object {
        const val DEGREES_TO_RADIANS = PI / 180.0
        const val ROTATIONS_TO_RADIANS = PI * 2.0
        const val MINUTE_TO_SECOND = 1/60.0
    }

    operator fun times(other: Length) = LinearVelocity(other.feet * _radiansPerSecond)
    operator fun times(other: Double) = AngularVelocity(other * _radiansPerSecond)

    val radiansPerSecond get() = _radiansPerSecond
    val degreesPerSecond get() = _radiansPerSecond / DEGREES_TO_RADIANS
    val rpm get() = _radiansPerSecond / (ROTATIONS_TO_RADIANS * MINUTE_TO_SECOND)
    fun encoderVelocity(cpr: Int) = (_radiansPerSecond / (ROTATIONS_TO_RADIANS * 10)) * (cpr * 4.0)

    override fun toString(): String {
        return "$degreesPerSecond deg/sec"
    }


}