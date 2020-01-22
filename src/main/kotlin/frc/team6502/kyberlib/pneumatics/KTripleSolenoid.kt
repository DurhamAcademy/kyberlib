package frc.team6502.kyberlib.pneumatics

import edu.wpi.first.wpilibj.Solenoid

/**
 *  A_____B______C
 * ||     |      ||______|
 * |______________|      |
 *
 */
class KTripleSolenoid(val module: Int, val portA: Int, val portB: Int, val portC: Int) {

    private val solenoidA = Solenoid(module, portA)
    private val solenoidB = Solenoid(module, portB)
    private val solenoidC = Solenoid(module, portC)

    var state = TripleSolenoidState.OFF
        set(value) {
            solenoidA.set(
                when (value) {
                    TripleSolenoidState.MIDDLE,
                    TripleSolenoidState.EXTENDED -> true
                    else -> false
                }
            )
            solenoidB.set(
                when (value) {
                    TripleSolenoidState.EXTENDED -> true
                    else -> false
                }
            )
            solenoidC.set(
                when (value) {
                    TripleSolenoidState.RETRACTED -> true
                    else -> false
                }
            )

            field = value
        }
}

enum class TripleSolenoidState {
    RETRACTED,
    MIDDLE,
    EXTENDED,
    OFF
}
