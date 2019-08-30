package frc.team6502.kyberlib.input

import kotlin.math.abs

class KAxis(private val raw: () -> Double){

    var rate = 1.0
    var expo = 0.0
    var superRate = 0.0


    val value: Double
    get() {
        val command = raw.invoke()

        var retval = ((1 + 0.01 * expo * (command * command - 1.0)) * command)
        retval = (retval * (rate + (abs(retval) * rate * superRate * 0.01)))
        return retval
    }
}