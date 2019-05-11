package frc.team6502.kyberlib.path

import frc.team6502.kyberlib.math.geom.Transform2
import frc.team6502.kyberlib.math.rtf
import frc.team6502.kyberlib.math.units.radians
import frc.team6502.kyberlib.math.units.radiansPerSecond
import frc.team6502.kyberlib.util.logging.CSVField
import frc.team6502.kyberlib.util.logging.CSVWriter
import koma.*

class Path(val waypoints: ArrayList<Waypoint>, val cruiseVelocity: Double, val acceleration: Double) {
    var length = 0.0
    val duration get() = trapezoidalProfile.endTime
    private var trapezoidalProfile: TrapezoidalProfile
    private val splines = arrayListOf<Spline>()
    val trajectory = arrayListOf<TrajectoryPoint>()

    init {
        for (i in 1 until waypoints.size) {
            splines.add(Spline(waypoints[i - 1], waypoints[i]))
        }
        for (spline in splines) {
            length += spline.length
        }
        println("TOTAL LENGTH: $length")
        trapezoidalProfile = TrapezoidalProfile(length, cruiseVelocity, acceleration)
        println(trapezoidalProfile)
        val csvWriter = CSVWriter("/home/co/path.csv")
        var distanceTraversed = 0.0
        var lastH = waypoints[0].h
        for (i in 0..(trapezoidalProfile.endTime / 0.02).toInt()) {
            val t = i * 0.02
            val vel = trapezoidalProfile.eval(t)
            distanceTraversed += vel * 0.02
            val pos = getPosition(distanceTraversed)
            val omega = (pos.h - lastH).radians / 0.02
//            println(omega)
//            println(omega.radiansPerSecond.radiansPerSecond)
            trajectory.add(TrajectoryPoint(t, pos.x.rtf, pos.y.rtf, pos.h.radians.rtf.radians, vel.rtf, omega.rtf.radiansPerSecond))
            csvWriter.write(CSVField("time", t.rtf), pos, CSVField("velocity", vel), CSVField("avel", omega), CSVField("distance", distanceTraversed))
            lastH = pos.h
        }
        println(distanceTraversed)
    }

    fun showPlots() {
        figure(1)
        plotArrays(trajectory.map { it.x }.toDoubleArray(), trajectory.map { it.y }.toDoubleArray())
        xlabel("x")
        ylabel("y")
        title("Path")
        figure(2)
        plotArrays(trajectory.map { it.t }.toDoubleArray(), trajectory.map { it.vel }.toDoubleArray())
        xlabel("time")
        ylabel("velocity")
        title("Velocity")
    }

    fun getPointAtTime(t: Double): TrajectoryPoint {
        return trajectory[floor(t/0.02).coerceIn(0 until trajectory.size)]
    }

    fun getPosition(dist: Double): Transform2 {
        var d = dist
        for (spline in splines) {
            if (d > spline.length)
                d -= spline.length
            else
                return spline.eval(d / spline.length)
        }
        return splines.last().eval(1.0)
    }

    fun getAvel(dist: Double): Double {
        var d = dist
        for (spline in splines) {
            if (d > spline.length)
                d -= spline.length
            else
                return spline.evalAvel(d / spline.length)
        }
        return splines.last().evalAvel(1.0)
    }
}