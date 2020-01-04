package frc.team6502.kyberlib.auto

object TrajectoryManager {
    internal val trajectories = mutableMapOf<String, KTrajectory>()
    operator fun get(s: String?) = trajectories[s]
    val list
        get() = trajectories.keys.toTypedArray()
}
