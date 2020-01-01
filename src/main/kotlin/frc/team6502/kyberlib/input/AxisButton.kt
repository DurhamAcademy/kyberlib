package frc.team6502.kyberlib.input

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj2.command.button.Trigger

class AxisButton(joystick: Joystick, axis: Int, condition: (value: Double) -> Boolean) : Trigger({ condition(joystick.getRawAxis(axis)) })
