package frc.team6502.kyberlib.motorcontrol

import frc.team6502.kyberlib.math.units.Angle
import frc.team6502.kyberlib.math.units.AngularVelocity
import frc.team6502.kyberlib.math.units.Length

interface KMotorController {
    var percentOutput: Double
    var kP: Double
    var kI: Double
    var kD: Double
    var rotationSetpoint: Angle
    var rotation: Angle
    var positionSetpoint: Length
    var position: Length
}