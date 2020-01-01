package frc.team6502.kyberlib.math.units

import frc.team6502.kyberlib.math.units.extensions.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
        assertTrue(180.degrees + 180.degrees epsilonEquals 360.degrees)
        assertTrue(180.degrees + 0.degrees epsilonEquals 180.degrees)
    }

    @Test
    fun minus() {
        assertTrue(10.degrees - 0.degrees epsilonEquals 10.degrees)
        assertTrue(10.degrees - 5.degrees epsilonEquals 5.degrees)
        assertTrue(0.degrees.subtractNearest(350.degrees) epsilonEquals 10.degrees)
        assertTrue(0.degrees.subtractNearest(270.degrees) epsilonEquals 90.degrees)
    }

    @Test
    fun toCircumference() {
        assertTrue(360.degrees.toCircumference(6.inches) epsilonEquals 6.inches * TAU)
        assertTrue(180.degrees.toCircumference(6.inches) epsilonEquals 3.inches * TAU)
    }


}