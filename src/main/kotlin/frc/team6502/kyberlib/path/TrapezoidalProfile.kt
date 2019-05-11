package frc.team6502.kyberlib.path

import kotlin.math.sqrt

class TrapezoidalProfile(val distance: Double, val cruise: Double, val accel: Double){
    var startTime = 0.0
        private set
    var cruiseStartTime = 0.0
        private set
    var cruiseEndTime = 0.0
        private set
    var endTime = 0.0
        private set
    var isTriangular = false
        private set
    var peakVelocity = 0.0
        private set

    init {
        val area = distance

        // generate the base and height of the needed triangles
        val b = sqrt(area/accel)
        val h = area/b

        println("b=$b h=$h")

        // calculate dimensions of extra triangle
        val tb = 2*(b-(cruise/accel))

        val th = h-cruise


        println("tb=$tb th=$th")

        if(tb > 0){
            // trapezoid
            val ea = 0.5*tb*th + tb*cruise
            val et = ea/cruise
            println(et)

             startTime = 0.0
             cruiseStartTime = cruise/accel
             cruiseEndTime = cruise/accel + et
             endTime = 2*cruise/accel + et
            peakVelocity = cruise

            println("distance=$distance")
            println("area=${cruise*(cruiseStartTime-startTime) + cruise*(cruiseEndTime-cruiseStartTime)}")
        } else {
            // triangle
            startTime = 0.0
            cruiseStartTime = b
            cruiseEndTime = b
            endTime = (b*2)
            peakVelocity = h
            isTriangular = true
            println("distance=$distance")
            println("area=${b*h}")
        }
    }

    fun eval(t: Double): Double {
        return when {
            t < cruiseStartTime -> return t * accel
            t >= cruiseStartTime && t < cruiseEndTime -> return peakVelocity
            t >= cruiseEndTime -> return peakVelocity - (t-cruiseEndTime)*(accel)
            else -> 0.0
        }
    }

    override fun toString(): String {
        return "TrapezoidalProfile(distance=$distance, cruise=$cruise, accel=$accel, startTime=$startTime, cruiseStartTime=$cruiseStartTime, cruiseEndTime=$cruiseEndTime, endTime=$endTime, isTriangular=$isTriangular)"
    }
}