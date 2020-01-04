package frc.team6502.kyberlib.auto

import edu.wpi.first.wpilibj.trajectory.Trajectory
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil
import frc.team6502.kyberlib.KyberlibConfig
import frc.team6502.kyberlib.math.geometry.KPose2d
import java.io.File

class KTrajectory(val name: String, val waypoints: List<KPose2d>, val config: KTrajectoryConfig) {

    val wpiTrajectory: Trajectory

    init {
        val wpiWaypoints = waypoints.map(KPose2d::wpiPose2d)

        val trajFolder = File(KyberlibConfig.TRAJECTORY_PATH)
        if (!trajFolder.exists()) {
            println("Main trajectory directory does not exist, creating...")
            trajFolder.mkdir()
        }

        val jsonFile = File("${KyberlibConfig.TRAJECTORY_PATH}/$name.json")
        val hashFile = File("${KyberlibConfig.TRAJECTORY_PATH}/$name.hash")
        if (jsonFile.exists() && hashFile.exists()) {
            // the trajectory exists
            if (hashTrajectory() != hashFile.readText().toInt()) {
                // hash does not match, regenerate the profile
                println("Trajectory $name out of date, recreating...")
                wpiTrajectory = TrajectoryGenerator.generateTrajectory(wpiWaypoints, config.wpiConfig)
                jsonFile.writeText(TrajectoryUtil.serializeTrajectory(wpiTrajectory))
                hashFile.writeText(hashTrajectory().toString())
            } else {
                // hash matches, load the profile
                println("Loading trajectory $name from storage...")
                wpiTrajectory = TrajectoryUtil.deserializeTrajectory(jsonFile.readText())
            }
        } else {
            // the trajectory does not exist, generate it now
            println("Trajectory $name does not exist, generating...")
            wpiTrajectory = TrajectoryGenerator.generateTrajectory(wpiWaypoints, config.wpiConfig)
            jsonFile.writeText(TrajectoryUtil.serializeTrajectory(wpiTrajectory))
            hashFile.writeText(hashTrajectory().toString())
        }

        TrajectoryManager.trajectories[name] = this
        println("Trajectory $name loaded!")
    }

    private fun hashTrajectory() = "${waypoints.hashCode()},${config.hashCode()}".hashCode()
}
