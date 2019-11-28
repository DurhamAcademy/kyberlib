package frc.team6502.kyberlib

import edu.wpi.first.hal.FRCNetComm
import edu.wpi.first.hal.HAL
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.livewindow.LiveWindow
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandGroupBase
import edu.wpi.first.wpilibj2.command.CommandScheduler
import frc.team6502.kyberlib.diagnostics.Diagnostics

operator fun CommandGroupBase.plusAssign(cmd: Command) {
    addCommands(cmd)
}

open class KRobot(period: Double = 0.02) : TimedRobot(period) {

    companion object {
        internal var diagnostics: Diagnostics? = null
    }

    final override fun testPeriodic() {}
    final override fun autonomousPeriodic() { aLoop() }
    final override fun autonomousInit() { aInit() }
    final override fun teleopInit() { tInit() }
    final override fun disabledPeriodic() { dLoop() }
    final override fun teleopPeriodic() { tLoop() }

    final override fun robotInit() {
        HAL.report(FRCNetComm.tResourceType.kResourceType_Language, 6)
        rInit()
    }

    final override fun robotPeriodic() {
        CommandScheduler.getInstance().run()
        rLoop()
    }

    open fun diagnostics(diagnostics: Diagnostics) {

    }

    final override fun testInit() {
        LiveWindow.setEnabled(false)
        diagnostics = Diagnostics()
        diagnostics(diagnostics!!)
        diagnostics!!.schedule()
    }

    final override fun disabledInit() {
        diagnostics?.cancel()
        diagnostics = null
        dInit()
    }

    /**
     * Executed on boot. Initialize subsystems here. (dont)
     */
    open fun rInit() {}

    /**
     * Always executed repeatedly. Do not run the scheduler here, it is internally called.
     */
    open fun rLoop() {}

    /**
     * Executed when the robot moves to a disabled state, including on boot.
     */
    open fun dInit() {}

    /**
     * Executed constantly when the robot is disabled.
     */
    open fun dLoop() {}

    /**
     * Executed at the beginning of autonomous.
     */
    open fun aInit() {}

    /**
     * Executed constantly during autonomous.
     */
    open fun aLoop() {}

    /**
     * Executed at the beginning of teleop.
     */
    open fun tInit() {}

    /**
     * Executed constantly during teleop.
     */
    open fun tLoop() {}
}