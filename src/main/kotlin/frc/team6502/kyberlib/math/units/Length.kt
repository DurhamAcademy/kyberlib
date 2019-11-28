package frc.team6502.kyberlib.math.units

val Number.feet get() = Length(toDouble())
val Number.inches get() = Length(toDouble() * Length.INCHES_TO_FEET)
val Number.meters get() = Length(toDouble() * Length.METER_TO_FEET)

data class Length(private val _feet: Double) {

    companion object {
        const val INCHES_TO_FEET = 1/12.0
        const val METER_TO_FEET = 1 / 0.3048
    }

    operator fun plus(other: Length) = Length(_feet + other.feet)
    operator fun minus(other: Length) = Length(_feet - other.feet)

    operator fun times(scalar: Double) = Length(_feet * scalar)
    operator fun times(angle: Angle) = angle * this

    operator fun div(scalar: Double) = Angle(_feet / scalar)
    operator fun div(radius: Length) = Angle(_feet / radius.feet)

    val feet get() = _feet
    val inches get() = _feet / INCHES_TO_FEET
    val meters get() = _feet / METER_TO_FEET
}