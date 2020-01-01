package frc.team6502.kyberlib.math.units

import kotlin.math.PI

object LengthConversions {
    const val feetToMeters = 0.3048
    const val inchesToFeet = 1.0 / 12
    const val milesToFeet = 5280
}

object AngleConversions {
    const val degreesToRadians = PI / 180.0
    const val rotationsToRadians = 2 * PI
}

object TimeConversions {
    const val minutesToSeconds = 60
    const val hoursToMinutes = 60
}
