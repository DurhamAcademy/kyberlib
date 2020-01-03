package frc.team6502.kyberlib.input

import edu.wpi.first.wpilibj.Joystick

abstract class KController(port: Int = 0) {

    val joystick = Joystick(port)
}
