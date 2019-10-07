package frc.team6502.kyberlib.motorcontrol

import frc.team6502.kyberlib.math.units.Angle
import frc.team6502.kyberlib.math.units.AngularVelocity
import frc.team6502.kyberlib.math.units.Length
import frc.team6502.kyberlib.math.units.LinearVelocity

interface KMotorController {
    var percentOutput: Double
    var kP: Double
    var kI: Double
    var kD: Double
    var brake: Boolean
    var feedForward: KFeedForward?
    var positionSetpoint: Angle
    var position: Angle
    var linearPositionSetpoint: Length
    var linearPosition: Length
    var velocitySetpoint: AngularVelocity
    var velocity: AngularVelocity
    var linearVelocitySetpoint: LinearVelocity
    var linearVelocity: LinearVelocity
}