package frc.team6502.kyberlib.diagnostics

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup

/**
 * Handles automated testing of robot mechanisms
 */
class Diagnostics : SequentialCommandGroup() {
    companion object {
        internal var singleton: Diagnostics? = null
    }
}
