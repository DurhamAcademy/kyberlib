package frc.team6502.kyberlib.math.geometry

import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import frc.team6502.kyberlib.math.units.Angle
import frc.team6502.kyberlib.math.units.Length

data class KPose2d(val x: Length, val y: Length, val rotation: Angle) {
    val wpiPose2d = Pose2d(x.meters, y.meters, Rotation2d.fromDegrees(rotation.degrees))
}