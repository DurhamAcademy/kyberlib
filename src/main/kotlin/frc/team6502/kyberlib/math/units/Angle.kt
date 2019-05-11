package frc.team6502.kyberlib.math.units

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.absoluteValue

val Number.radians get() = Angle(toDouble())
val Number.degrees get() = Angle(toDouble() * Angle.DEGREES_TO_RADIANS)
val Number.rotations get() = Angle(toDouble() * Angle.ROTATIONS_TO_RADIANS)
fun Number.encoderAngle(cpr: Int) = (toDouble() / (cpr * 4.0)).rotations

const val TAU = 2 * PI

class Angle(private val _radians: Double) {
    companion object {
        const val DEGREES_TO_RADIANS = PI / 180.0
        const val ROTATIONS_TO_RADIANS = PI * 2.0
    }

    operator fun plus(other: Angle) = Angle(_radians + other.radians)
    operator fun minus(other: Angle): Angle {
        val diff = (_radians - other.radians + PI) % TAU - PI
        return Angle(if(diff < -PI) diff + TAU else diff)
    }
    operator fun times(other: Length) = other * _radians

    val sin get() = kotlin.math.sin(_radians)
    val cos get() = kotlin.math.cos(_radians)
    val tan get() = kotlin.math.tan(_radians)

    val radians get() = _radians
    val degrees get() = _radians / Angle.DEGREES_TO_RADIANS
    val rotations get() = _radians / Angle.ROTATIONS_TO_RADIANS
    fun encoderAngle(cpr: Int) = rotations * (cpr * 4.0)

    override fun toString() = "$degrees deg"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Angle

        if (abs(_radians - other._radians) > 0.0001) return false

        return true
    }

    override fun hashCode(): Int {
        return _radians.hashCode()
    }


}