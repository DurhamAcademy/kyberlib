package frc.team6502.kyberlib.math.units

import org.junit.Assert.*
import org.junit.Test
import kotlin.math.PI


internal class AngleTest {
    val sigma = 0.001

    @Test
    fun getRadians() {
        assertEquals(0.degrees.radians, 0.0, sigma)
        assertEquals(90.degrees.radians, PI/2, sigma)
        assertEquals(180.degrees.radians, PI, sigma)
        assertEquals(360.degrees.radians, PI*2, sigma)
    }

    @Test
    fun getDegrees() {
        assertEquals(0.radians.degrees, 0.0, sigma)
        assertEquals(PI.radians.degrees, 180.0, sigma)
        assertEquals((PI*2).radians.degrees, 360.0, sigma)
    }

    @Test
    fun getRotations() {
        assertEquals(-360.degrees.rotations, -1.0, sigma)
        assertEquals(0.degrees.rotations, 0.0, sigma)
        assertEquals(180.degrees.rotations, 0.5, sigma)
        assertEquals(360.degrees.rotations, 1.0, sigma)
        assertEquals(720.degrees.rotations, 2.0, sigma)
    }

    @Test
    fun encoderAngle() {
        assertEquals(180.degrees.encoderAngle(1024), 2048.0, sigma)
        assertEquals(180.degrees.encoderAngle(2048), 4096.0, sigma)
    }

    @Test
    fun plus() {
        assertEquals(180.degrees + 180.degrees, 360.degrees)
        assertEquals(180.degrees + 0.degrees, 180.degrees)
    }

    @Test
    fun minus() {
        assertEquals(10.degrees-0.degrees, 10.degrees)
        assertEquals(0.degrees-350.degrees, 10.degrees)
        assertEquals(0.degrees-270.degrees, 90.degrees)
    }

    @Test
    fun times() {
        assertEquals(360.degrees * 6.inches, 6.inches * TAU)
        assertEquals(180.degrees * 6.inches, 3.inches * TAU)
    }


}