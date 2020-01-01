package frc.team6502.kyberlib.math.units.extensions

import frc.team6502.kyberlib.math.units.*

typealias LinearVelocity = KUnit<Div<Meter, Second>>

val Double.metersPerSecond get() = LinearVelocity(this)
val Double.feetPerSecond get() = LinearVelocity(this * LengthConversions.feetToMeters)
val Double.milesPerHour get() = LinearVelocity(this * LengthConversions.milesToFeet * LengthConversions.feetToMeters / (TimeConversions.hoursToMinutes * TimeConversions.minutesToSeconds))

val Number.metersPerSecond get() = toDouble().metersPerSecond
val Number.feetPerSecond get() = toDouble().feetPerSecond
val Number.milesPerHour get() = toDouble().milesPerHour

val LinearVelocity.metersPerSecond get() = value
val LinearVelocity.feetPerSecond get() = value / LengthConversions.feetToMeters
val LinearVelocity.milesPerHour get() = value / (LengthConversions.milesToFeet * LengthConversions.feetToMeters / (TimeConversions.hoursToMinutes * TimeConversions.minutesToSeconds))
fun LinearVelocity.toAngularVelocity(radius: Length) = AngularVelocity(value / radius.value)
