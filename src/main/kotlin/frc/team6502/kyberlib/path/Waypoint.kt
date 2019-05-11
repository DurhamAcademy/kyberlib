package frc.team6502.kyberlib.path

import frc.team6502.kyberlib.math.geom.Vector2
import frc.team6502.kyberlib.math.units.Angle

data class Waypoint(val x: Double, val y: Double, val h: Angle){
    val tangent get() = Vector2(h.cos, h.sin)
}