package frc.team6502.kyberlib.math

import org.junit.Assert.*
import org.junit.Test

internal class InterpolatorTest {

    @Test
    fun testInter() {

        val i = Interpolator(
            mapOf(
                0.0 to 0.0,
                1.0 to 5.0,
                2.0 to -5.0
            )
        )

        assertEquals(2.5, i.calculate(0.5))
        assertEquals(5.0, i.calculate(1.0))
        assertEquals(-5.0, i.calculate(2.0))
        assertEquals(0.0, i.calculate(0.0))
        assertEquals(0.0, i.calculate(1.5))
        assertEquals(null, i.calculate(-1.0))

    }

}
