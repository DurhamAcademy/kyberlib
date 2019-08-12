package frc.team6502.kyberlib.path.followers

import frc.team6502.kyberlib.drive.ArcadeDriveCommand
import frc.team6502.kyberlib.math.geom.Transform2
import frc.team6502.kyberlib.math.units.Angle
import frc.team6502.kyberlib.math.units.radiansPerSecond
import frc.team6502.kyberlib.path.Path
import frc.team6502.kyberlib.path.TrajectoryPoint
import kotlin.math.pow
import kotlin.math.sqrt

class RamseteFollower(path: Path, val b: Double, val zeta: Double) : Follower(path) {
    override fun getDriveCommand(t: Double, pose: Transform2?): ArcadeDriveCommand {

        if(pose == null) throw InstantiationException("Odometry is a required parameter for ramsete")

        val p = path.getPointAtTime(t)

        val k1 = 2 * zeta * sqrt(p.avel.radiansPerSecond.pow(2) + b * p.vel.pow(2))
//        val k2 = b

        val v = p.vel * (p.h - pose.h).cos + k1 * ((p.x - pose.x) * pose.h.cos + (p.y - pose.y) * pose.h.sin)
        val av = p.avel.radiansPerSecond + b * p.vel * sinc(p.h - pose.h) * ((p.y - pose.y) * pose.h.cos - (p.x - pose.x) * pose.h.sin) + k1 * (p.h - pose.h).radians
//        println(av.radiansPerSecond)
        //TODO: add safety mechanism here

        return ArcadeDriveCommand(v, av.radiansPerSecond)
    }

    private fun sinc(delta: Angle) = delta.sin / delta.radians
}