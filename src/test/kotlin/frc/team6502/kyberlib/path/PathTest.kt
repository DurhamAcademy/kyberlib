package frc.team6502.kyberlib.path

import frc.team6502.kyberlib.math.units.degrees
import org.junit.Assert.*
import org.junit.Test

internal class PathTest {
    @Test
    fun generateStraightPath(){
        val p = Path(arrayListOf(Waypoint(0.0,0.0,0.degrees),Waypoint(10.0,0.0,0.degrees)), 2.0, 1.0)
        // make sure the end matches the target waypoint
        assertEquals(p.waypoints.last(), p.trajectory.last().toWaypoint())
    }
    @Test
    fun generateSimpleCurvedPath(){
        val p = Path(arrayListOf(Waypoint(0.0,0.0,0.degrees),Waypoint(10.0,10.0,0.degrees)), 2.0, 1.0)
        // make sure the end matches the target waypoint
        assertEquals(p.waypoints.last(), p.trajectory.last().toWaypoint())
    }
}