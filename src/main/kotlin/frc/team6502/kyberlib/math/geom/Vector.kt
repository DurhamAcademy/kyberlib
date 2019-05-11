package frc.team6502.kyberlib.math.geom

import frc.team6502.kyberlib.math.units.Angle
import frc.team6502.kyberlib.math.units.degrees
import frc.team6502.kyberlib.math.units.radians
import frc.team6502.kyberlib.util.logging.CSVWriteable
import kotlin.math.*

open class Vector2(var x: Double = 0.0, var y: Double = 0.0, var name: String = ""): CSVWriteable {
    override fun toCSV(): String = "$x,$y"
    override fun toCSVHeader(): String = "${name}x,${name}y"

    val magnitude get() = hypot(x,y)
    val direction get() = atan2(y,x)
    val inverse get() = Vector2(-x, -y)
    override fun toString(): String {
        return "Vector2(x=${"%.5f".format(x)}, y=${"%.5f".format(y)})"
    }
}

class Transform2(x: Double = 0.0, y: Double = 0.0, var h: Angle = 0.radians, name: String = ""): Vector2(x,y,name) {
    override fun toCSV(): String = "$x,$y,$h"
    override fun toCSVHeader(): String = "${name}x,${name}y,${name}h"
    override fun toString(): String {
        return "Transform2(x=${"%.5f".format(x)}, y=${"%.5f".format(y)}, h=${"%.5f".format(h.degrees)})"
    }
}

data class Vector3(var x: Double = 0.0, var y: Double = 0.0, var z: Double = 0.0){
    val magnitude get() = hypot(hypot(x,y),z)
}