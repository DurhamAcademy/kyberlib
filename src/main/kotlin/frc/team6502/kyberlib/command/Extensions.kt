package frc.team6502.kyberlib.command

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandGroupBase

operator fun CommandGroupBase.plusAssign(cmd: Command) {
    addCommands(cmd)
}