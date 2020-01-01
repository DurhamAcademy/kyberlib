package frc.team6502.kyberlib.math.units.extensions

import frc.team6502.kyberlib.math.units.KUnit
import frc.team6502.kyberlib.math.units.LengthConversions.feetToMeters
import frc.team6502.kyberlib.math.units.LengthConversions.inchesToFeet
import frc.team6502.kyberlib.math.units.Meter

typealias Length = KUnit<Meter>
typealias Distance = KUnit<Meter>

val Double.meters get() = Length(this)
val Double.feet get() = Length(this * feetToMeters)
val Double.inches get() = Length(this * inchesToFeet * feetToMeters)

val Number.meters get() = toDouble().meters
val Number.feet get() = toDouble().feet
val Number.inches get() = toDouble().inches

val Length.meters get() = value
val Length.feet get() = value / feetToMeters
val Length.inches get() = value / (inchesToFeet * feetToMeters)
fun Length.toAngle(radius: Length) = Angle(value / radius.value)
