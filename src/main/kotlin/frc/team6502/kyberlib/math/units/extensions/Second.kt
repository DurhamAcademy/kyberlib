package frc.team6502.kyberlib.math.units.extensions

import frc.team6502.kyberlib.math.units.KUnit
import frc.team6502.kyberlib.math.units.Second
import frc.team6502.kyberlib.math.units.TimeConversions

typealias Time = KUnit<Second>

val Double.seconds get() = Time(this)
val Double.minutes get() = Time(this * TimeConversions.minutesToSeconds)
val Double.hours get() = Time(this * TimeConversions.hoursToMinutes * TimeConversions.minutesToSeconds)

val Number.seconds get() = toDouble().seconds
val Number.minutes get() = toDouble().minutes
val Number.hours get() = toDouble().hours

val Time.seconds get() = value
val Time.minutes get() = value / TimeConversions.minutesToSeconds
val Time.hours get() = value / (TimeConversions.hoursToMinutes * TimeConversions.minutesToSeconds)
