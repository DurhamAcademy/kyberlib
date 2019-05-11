package frc.team6502.kyberlib.path

import frc.team6502.kyberlib.math.geom.Transform2
import frc.team6502.kyberlib.math.geom.Vector2
import frc.team6502.kyberlib.math.units.degrees
import frc.team6502.kyberlib.math.units.radians
import koma.create
import koma.end
import koma.extensions.toDoubleArray
import koma.mat
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.sqrt

class Spline(val start: Waypoint, val end: Waypoint){
    var length = 0.0

    val tangentPower = hypot(start.x-end.x,start.y-end.y)
    val xCoeffsA = mat[
            0,0,0,1 end
                    1,1,1,1 end
                    0,0,1,0 end
                    3,2,1,0
    ]
    val xCoeffsB = create(doubleArrayOf(start.x, end.x, start.tangent.x * tangentPower, end.tangent.x * tangentPower), 4, 1)
    val yCoeffsB = create(doubleArrayOf(start.y, end.y, start.tangent.y * tangentPower, end.tangent.y * tangentPower), 4, 1)

    val xCoeffs = xCoeffsA.inv() * xCoeffsB
    val yCoeffs = xCoeffsA.inv() * yCoeffsB

    val xPoly = Polynomial(*xCoeffs.getCol(0).toDoubleArray(), variableName = 'u')
    val yPoly = Polynomial(*yCoeffs.getCol(0).toDoubleArray(), variableName = 'u')

    init {
        var lastX: Double? = null
        var lastY: Double? = null

        for(i in 0..1000){
            val d = i/1000.0
            val currentX = xPoly.eval(d)
            val currentY = yPoly.eval(d)
            if(lastX != null && lastY != null){
                val dx = (currentX-lastX)
                val dy = (currentY-lastY)
                val h = hypot(dx, dy)
                length += h
//                println("dx=$dx dy=$dy")
            }
            lastX = currentX
            lastY = currentY
        }

        println("Spline length: $length")
    }

    fun eval(t: Double): Transform2 {
        return Transform2(xPoly.eval(t), yPoly.eval(t), headingAt(t).radians)
    }

    fun headingAt(t: Double): Double{
        val offset = 0.01
        val dx = xPoly.eval(t+offset) - xPoly.eval(t-offset)
        val dy = yPoly.eval(t+offset) - yPoly.eval(t-offset)
        return atan2(dy, dx)
    }

    fun evalAvel(t: Double): Double {
        val offset = 0.02

        val dh = headingAt(t) - headingAt(t-offset)
        return dh / 0.02
    }


}