package frc.team6502.kyberlib.diagnostics

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup

class Diagnostics : SequentialCommandGroup() {
    companion object {
        var singleton: Diagnostics? = null
    }
}