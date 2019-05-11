package frc.team6502.kyberlib.motorcontrol

import frc.team6502.kyberlib.math.units.Angle
import frc.team6502.kyberlib.math.units.AngularVelocity

fun talonsrx(canID: Int, init: KTalonSRX.() -> Unit): KTalonSRX {
    val t = KTalonSRX(canID)
    t.init()
    return t
}


interface KMotorController {
    var percentOutput: Double
}

interface KClosedLoopMotorController {
    var velocity: AngularVelocity
    var sensorVelocity: AngularVelocity
    var position: Angle
    var sensorPosition: Angle
}


class ProfileParams(val maxVelocity: Double, val maxAcceleration: Double, val smoothing: Double = 0.0)