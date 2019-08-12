package frc.team6502.kyberlib.math.units

import kotlin.math.absoluteValue

val Number.feet get() = Length(toDouble())
val Number.inches get() = Length(toDouble() * Length.INCHES_TO_FEET)

class Length(private val _feet: Double) {
    companion object {
        const val INCHES_TO_FEET = 1/12.0
    }
    operator fun plus(other: Length) = Length(_feet + other.feet)
    operator fun times(scalar: Double) = Length(_feet * scalar)
    operator fun times(angle: Angle) = angle * this
    operator fun div(radius: Length) = Angle(_feet / radius.feet)



    val feet get() = _feet
    val inches get() = _feet / INCHES_TO_FEET

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Length

        if ((_feet - other._feet).absoluteValue < 0.00001) return false

        return true
    }

}