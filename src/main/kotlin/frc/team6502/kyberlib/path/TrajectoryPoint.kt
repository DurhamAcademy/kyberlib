package frc.team6502.kyberlib.path

import frc.team6502.kyberlib.math.units.Angle
import frc.team6502.kyberlib.math.units.AngularVelocity

data class TrajectoryPoint(val t: Double, val x: Double, val y: Double, val h: Angle, val vel: Double, val avel: AngularVelocity) {
    fun toWaypoint() = Waypoint(x, y, h)
}