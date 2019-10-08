package frc.team6502.kyberlib.input.controller

import edu.wpi.first.wpilibj.buttons.JoystickButton
import frc.team6502.kyberlib.input.KAxis
import frc.team6502.kyberlib.input.KController

/**
 * Wrapper for a Logitech Extreme 3D Pro Joystick
 */
class KExtreme3DPro(port: Int) : KController(port) {
    //axes
    val x = KAxis { joystick.x }
    val y = KAxis { joystick.getRawAxis(2) }
    val twist = KAxis { joystick.getRawAxis(3) }
    val throttle = KAxis { joystick.getRawAxis(4) }
    val hatX = KAxis { joystick.getRawAxis(5)}
    val HatY = KAxis { joystick.getRawAxis(6) }
    //buttons
    val trigger = JoystickButton(joystick, 1)
    val thumbswitch = JoystickButton(joystick, 2)
    val button3 = JoystickButton(joystick, 3)
    val button4 = JoystickButton(joystick, 4)
    val button5 = JoystickButton(joystick, 5)
    val button6 = JoystickButton(joystick, 6)
    val button7 = JoystickButton(joystick, 7)
    val button8 = JoystickButton(joystick, 8)
    val button9 = JoystickButton(joystick, 9)
    val button10 = JoystickButton(joystick, 10)
    val button11 = JoystickButton(joystick, 11)
    val button12 = JoystickButton(joystick, 12)
}