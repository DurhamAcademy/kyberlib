package frc.team6502.kyberlib.input

import org.junit.Assert.assertEquals
import org.junit.Test

internal class KAxisTest {

    @Test
    fun zero() {
        val axis = KAxis { 0.0 }
        assertEquals(axis.value, 0.0, 0.001)
        axis.rate = 10.0
        axis.expo = 3.0
        axis.superRate = 10.0
        assertEquals(axis.value, 0.0, 0.001)
    }

    @Test
    fun rawResponse() {
        val axis = KAxis { 0.5 }
        assertEquals(axis.value, 0.5, 0.01)
    }

    @Test
    fun linearResponse() {
        val axis = KAxis { 0.5 }
        axis.rate = 2.0
        println(axis.value)
        assertEquals(axis.value, 1.0, 0.05)
    }

}
