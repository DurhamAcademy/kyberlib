package frc.team6502.kyberlib.math

import org.junit.Assert.assertEquals
import org.junit.Test

internal class DifferentiatorTest {

    @Test
    fun testIncreasing() {
        val d = Differentiator()
        d.calculate(0.0)
        Thread.sleep(500)
        assertEquals(2.0, d.calculate(1.0), 0.1)
    }

    @Test
    fun testSteady() {
        val d = Differentiator()
        d.calculate(1.0)
        Thread.sleep(500)
        assertEquals(0.0, d.calculate(1.0), 0.1)
    }
}
