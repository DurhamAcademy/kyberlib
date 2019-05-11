package frc.team6502.kyberlib.path.followers

import frc.team6502.kyberlib.drive.DriveCommand
import frc.team6502.kyberlib.math.geom.Transform2
import frc.team6502.kyberlib.path.Path

abstract class Follower(val path: Path) {
    abstract fun getDriveCommand(t: Double, pose: Transform2?): DriveCommand
}