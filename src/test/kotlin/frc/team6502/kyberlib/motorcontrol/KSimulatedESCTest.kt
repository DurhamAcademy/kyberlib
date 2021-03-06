package frc.team6502.kyberlib.motorcontrol

import frc.team6502.kyberlib.math.units.extensions.rotations
import org.junit.Assert.assertEquals
import org.junit.Test

internal class KSimulatedESCTest {

    @Test
    fun testPercentOutput() {
        val sim = KSimulatedESC("sim")
        sim.percentOutput = 0.5

        assertEquals(0.5, sim.appliedOutput, 0.01)
    }

    @Test
    fun testFollow() {
        val master = KSimulatedESC("master")
        val slave = KSimulatedESC("slave")

        slave.percentOutput = 0.2

        master += slave
//        master.update()

        master.percentOutput = 0.4
        master.update()
        assertEquals(master.appliedOutput, slave.appliedOutput, 0.01)

        master.percentOutput = 0.4
        master.reversed = false
        slave.reversed = true
        master.update()
        assertEquals("Slave reversed", -0.4, slave.appliedOutput, 0.01)

        master.reversed = true
        slave.reversed = false
        master.percentOutput = 0.4
        master.update()
        assertEquals("Master reversed", -0.4, slave.appliedOutput, 0.01)

        master.percentOutput = 0.4
        master.reversed = true
        slave.reversed = true
        master.update()
        assertEquals("Both reversed", 0.4, slave.appliedOutput, 0.01)
    }

    @Test
    fun testPID() {
        val sim = KSimulatedESC("sim") {
            kP = 0.1
            encoderConfig = KEncoderConfig(1024, EncoderType.QUADRATURE)
        }

        sim.positionSetpoint = 1.rotations

        assertEquals(0.1, sim.appliedOutput, 0.01)

        sim.currentPos = 1.rotations

        assertEquals(0.0, sim.appliedOutput, 0.01)
    }
}
