package frc.team6502.kyberlib.input.controller

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj2.command.button.JoystickButton
import frc.team6502.kyberlib.input.KAxis
import frc.team6502.kyberlib.input.KController

class KXboxController(port: Int): KController(port){
    val leftX = KAxis { joystick.getX(GenericHID.Hand.kLeft) }
    val leftY = KAxis { joystick.getY(GenericHID.Hand.kLeft) }

    val rightX = KAxis { joystick.getX(GenericHID.Hand.kRight) }
    val rightY = KAxis { joystick.getY(GenericHID.Hand.kRight) }

    val aButton = JoystickButton(joystick, 0)
    val bButton = JoystickButton(joystick, 1)
    val xButton = JoystickButton(joystick, 2)
    val yButton = JoystickButton(joystick, 3)
}
