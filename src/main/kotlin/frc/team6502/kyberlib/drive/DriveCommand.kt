package frc.team6502.kyberlib.drive

import frc.team6502.kyberlib.math.units.AngularVelocity

interface DriveCommand

/**
 * Drive command for a basic two-sided drivetrain (typically WCD)
 */
data class ArcadeDriveCommand(val vel: Double, val avel: AngularVelocity) : DriveCommand

/**
 * Drive command for drivetrains such as mecanum or H that can strafe left or right
 */
data class StrafingArcadeDriveCommand(val vel: Double, val avel: AngularVelocity, val strafeVel: Double) : DriveCommand

/**
 * Drive command for a field-oriented drive, typically swerve but can be used for any holonomic drive with a gyro
 * +X is straight away from the drive station
 */
data class FieldOrientedDriveCommand(val xvel: Double, val yvel: Double, val avel: AngularVelocity) : DriveCommand