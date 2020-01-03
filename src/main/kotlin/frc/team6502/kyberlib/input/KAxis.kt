package frc.team6502.kyberlib.input

import kotlin.math.abs

class KAxis(private val raw: () -> Double) {

    var rate = 1.0
    var expo = 0.0
    var superRate = 0.0
    var deadband = 0.01

    val value: Double
        get() {
            var command = raw.invoke()

            // apply deadband
            command = when {
                command > deadband -> (1 / (1 - deadband)) * command - (deadband / (1 - deadband))
                command < -deadband -> (1 / (1 - deadband)) * command + (deadband / (1 - deadband))
                else -> 0.0
            }

            var retval = ((1 + 0.01 * expo * (command * command - 1.0)) * command)
            retval = (retval * (rate + (abs(retval) * rate * superRate * 0.01)))
            return retval
        }
}
