package frc.team6502.kyberlib.lighting

import frc.team6502.kyberlib.lighting.animations.AnimationBlink
import frc.team6502.kyberlib.lighting.animations.AnimationPulse
import frc.team6502.kyberlib.lighting.animations.AnimationRGBFade
import frc.team6502.kyberlib.lighting.animations.AnimationRGBRain
import frc.team6502.kyberlib.lighting.animations.AnimationRGBWave
import frc.team6502.kyberlib.lighting.animations.AnimationRain
import frc.team6502.kyberlib.lighting.animations.AnimationSolid
import org.junit.Assert.assertEquals
import org.junit.Test
import java.awt.Color

class LEDTest {

    @Test
    fun testSolid() {
        val buf = KLEDRegion(AnimationSolid(Color.red), 0, 32).getBuffer(0)!!
        assertEquals(Color.red, buf[0])
    }

    @Test
    fun testBlink() {
        val bufOn = KLEDRegion(AnimationBlink(Color.orange, 20), 0, 32).getBuffer(5)!!
        val bufOff = KLEDRegion(AnimationBlink(Color.orange, 20), 0, 32).getBuffer(15)!!
        assertEquals(Color.orange, bufOn[0])
        assertEquals(Color.black, bufOff[0])
    }

    @Test
    fun testPulse() {
        val buf = KLEDRegion(AnimationPulse(Color.red, 10), 0, 32).getBuffer(0)!!
        // TODO
    }

    @Test
    fun testRain() {
        val buf = KLEDRegion(AnimationRain(Color.decode("#ff5d73"), 10, 10), 0, 32).getBuffer(0)!!
        // TODO
    }

    @Test
    fun testRGBRain() {
        val buf = KLEDRegion(AnimationRGBRain(0.1, 10, 10), 0, 32).getBuffer(0)!!
        val buf2 = KLEDRegion(AnimationRGBRain(2.0, 10, 10), 0, 32).getBuffer(0)!!
        // TODO
    }

    @Test
    fun testWave() {
        val buf = KLEDRegion(AnimationRGBWave(0.5, 10), 0, 32).getBuffer(0)!!
        val buf2 = KLEDRegion(AnimationRGBWave(2.0, 10), 0, 32).getBuffer(0)!!
        // TODO
    }

    @Test
    fun testFade() {
        val buf = KLEDRegion(AnimationRGBFade(10), 0, 32).getBuffer(0)!!
        assertEquals(Color.red, buf[0])
        // TODO
    }

    @Test
    fun testOpaqueComposite() {
        val buf = KLEDRegion.composite(32, 0, listOf(
                KLEDRegion(AnimationSolid(Color.red), 0, 32),
                KLEDRegion(AnimationSolid(Color.blue), 0, 16)
        ))
        assertEquals(Color.blue, buf[5])
    }

    @Test
    fun testTransparentComposite() {
        val buf = KLEDRegion.composite(32, 0, listOf(
                KLEDRegion(AnimationSolid(Color(255, 0, 0)), 0, 32, false),
                KLEDRegion(AnimationSolid(Color(0, 255, 0, 127)), 0, 16, true)
        ))
        assertEquals(Color(128, 127, 0), buf[5])
    }
}
