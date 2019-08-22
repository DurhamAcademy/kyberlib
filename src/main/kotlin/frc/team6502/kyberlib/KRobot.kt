package frc.team6502.kyberlib

import edu.wpi.first.hal.FRCNetComm
import edu.wpi.first.hal.HAL
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.frc2.command.Command
import edu.wpi.first.wpilibj.frc2.command.CommandGroupBase
import edu.wpi.first.wpilibj.frc2.command.CommandScheduler
import edu.wpi.first.wpilibj.livewindow.LiveWindow
import frc.team6502.kyberlib.diagnostics.Diagnostics

operator fun CommandGroupBase.plusAssign(cmd: Command) {
    println(cmd.name)
    addCommands(cmd)
}

open class KRobot(period: Double = 0.02) : TimedRobot(period) {


    final override fun robotInit() {
        HAL.report(FRCNetComm.tResourceType.kResourceType_Language, 6)
        rInit()
    }

    override fun loopFunc() {
        super.loopFunc()
    }

    final override fun robotPeriodic() {
        CommandScheduler.getInstance().run()
        rLoop()
    }

    final override fun testInit() {
        LiveWindow.setEnabled(false)
        Diagnostics.schedule()
    }

    final override fun testPeriodic() {

    }

    final override fun autonomousPeriodic() {
        aLoop()
    }

    final override fun autonomousInit() {
        aInit()
    }

    final override fun teleopInit() {
        tInit()
    }

    final override fun disabledInit() {
        Diagnostics.cancel()
        dInit()
    }

    final override fun disabledPeriodic() {
        dLoop()
    }

    final override fun teleopPeriodic() {
        tLoop()
    }

    open fun rInit() {}
    open fun rLoop() {}
    open fun dInit() {}
    open fun dLoop() {}
    open fun aInit() {}
    open fun aLoop() {}
    open fun tInit() {}
    open fun tLoop() {}
}