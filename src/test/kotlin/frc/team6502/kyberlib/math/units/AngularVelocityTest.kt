package frc.team6502.kyberlib.math.units

import frc.team6502.kyberlib.math.units.extensions.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.PI


internal class AngularVelocityTest {
    val sigma = 0.001

    @Test
    fun getRadiansPerSecond() {
        assertEquals(0.degreesPerSecond.radiansPerSecond, 0.0, sigma)
        assertEquals(90.degreesPerSecond.radiansPerSecond, PI/2, sigma)
        assertEquals(180.degreesPerSecond.radiansPerSecond, PI, sigma)
        assertEquals(360.degreesPerSecond.radiansPerSecond, PI*2, sigma)
    }

    @Test
    fun getDegreesPerSecond() {
        assertEquals(0.radiansPerSecond.degreesPerSecond, 0.0, sigma)
        assertEquals(PI.radiansPerSecond.degreesPerSecond, 180.0, sigma)
        assertEquals((PI*2).radiansPerSecond.degreesPerSecond, 360.0, sigma)
    }

    @Test
    fun getRPM() {
        assertEquals(-360.degreesPerSecond.rpm, -60.0, sigma)
        assertEquals(0.degreesPerSecond.rpm, 0.0, sigma)
        assertEquals(180.degreesPerSecond.rpm, 30.0, sigma)
        assertEquals(360.degreesPerSecond.rpm, 60.0, sigma)
        assertEquals(720.degreesPerSecond.rpm, 120.0, sigma)
    }

    @Test
    fun encoderVelocity() {
        assertEquals(180.degreesPerSecond.encoderVelocity(1024), 204.8, sigma)
        assertEquals(180.degreesPerSecond.encoderVelocity(2048), 409.6, sigma)
    }

    @Test
    fun times() {
        assertTrue(60.rpm.toTangentialVelocity(6.inches) epsilonEquals (PI).feetPerSecond)
    }


}
