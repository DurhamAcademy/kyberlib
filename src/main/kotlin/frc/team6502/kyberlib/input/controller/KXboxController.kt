package frc.team6502.kyberlib.input.controller

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj2.command.button.JoystickButton
import frc.team6502.kyberlib.input.KAxis
import frc.team6502.kyberlib.input.KController

class KXboxController(port: Int) : KController(port) {
    val leftX = KAxis { joystick.getRawAxis(0) }
    val leftY = KAxis { joystick.getRawAxis(1) }

    val rightX = KAxis{ joystick.getRawAxis(4) }
    val rightY = KAxis{ joystick.getRawAxis(5) }

    val aButton = JoystickButton(joystick, 1)
    val bButton = JoystickButton(joystick, 2)
    val xButton = JoystickButton(joystick, 3)
    val yButton = JoystickButton(joystick, 4)
}
