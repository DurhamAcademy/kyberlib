package frc.team6502.kyberlib.path.followers

import frc.team6502.kyberlib.math.geom.Transform2
import frc.team6502.kyberlib.math.units.degrees
import frc.team6502.kyberlib.math.units.radians
import frc.team6502.kyberlib.path.Path
import frc.team6502.kyberlib.path.Waypoint
import koma.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.absoluteValue
import kotlin.math.hypot

class FollowerTest {

    fun ramsTest(path: Path, b: Double, zeta: Double, displayPlot: Boolean = false, bumpTime: Double? = null) : Double{
        var t = 0.0

        val f = RamseteFollower(path, b,zeta)
        val odometry = Transform2(0.0, 0.0, 0.degrees)

        var dxs = doubleArrayOf()
        var dys = doubleArrayOf()

        var xs = doubleArrayOf()
        var ys = doubleArrayOf()

        while(t < path.duration){

            val command = f.getDriveCommand(t, odometry)
//            println(command)
            odometry.h += (0.02*command.avel.radiansPerSecond.coerceIn(-PI, PI)).radians
            odometry.x += command.vel.coerceIn(0.0, 5.0) * 0.02 * odometry.h.cos
            odometry.y += command.vel.coerceIn(0.0, 5.0) * 0.02 * odometry.h.sin

//            println(t)
            if(bumpTime != null && (t - bumpTime).absoluteValue < 0.001) {
                odometry.x -= 0.5
                odometry.y += 0.5
            }

            xs += odometry.x
            ys += odometry.y

            dxs += path.getPointAtTime(t).x
            dys += path.getPointAtTime(t).y

//            println(odometry)
            t += 0.02
        }

        if(displayPlot) {
            figure(0)
            plotArrays(dxs, dys, "r", "desired")
            plotArrays(xs, ys, "b", "actual")
            xlabel("x")
            ylabel("y")
            title("path")
        }

        // wait for graph to close
        Thread.sleep(1000)
        while (figures.first() != null) {
            Thread.sleep(1000)
        }

        val err = hypot(dys.last()-ys.last(), dxs.last()-xs.last())
        println("Final error: $err")
        return err
    }

    @Test
    fun basicRamsete(){
        // generate a quick path
        val p = Path(arrayListOf(Waypoint(0.0,0.0,0.degrees), Waypoint(10.0, 10.0, 0.degrees)), 2.0, 1.0)

        val e = ramsTest(p, 3.0, 0.7, displayPlot = true)

        assertTrue(e < 0.1)
    }

    @Test
    fun bumpTest(){
        // generate a quick path
        val p = Path(arrayListOf(Waypoint(0.0,0.0,0.degrees), Waypoint(10.0, 10.0, 0.degrees)), 2.0, 1.0)

        val e = ramsTest(p, 3.0, 0.7, true, 3.0)

        assertTrue(e < 0.1)



    }

}