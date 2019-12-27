package frc.team6502.kyberlib.command

import edu.wpi.first.hal.FRCNetComm
import edu.wpi.first.hal.HAL
import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.livewindow.LiveWindow
import edu.wpi.first.wpilibj2.command.CommandScheduler
import frc.team6502.kyberlib.diagnostics.Diagnostics

open class KRobot(private val period: Double = 0.02) {

    private inner class KRobotInternal : TimedRobot(period) {

        override fun robotInit() {
            HAL.report(FRCNetComm.tResourceType.kResourceType_Language, 6)
            LiveWindow.disableAllTelemetry()
            this@KRobot.robotInit()
        }

        override fun robotPeriodic() {
            CommandScheduler.getInstance().run()
            this@KRobot.robotPeriodic()
        }

        override fun disabledInit() {
            Diagnostics.singleton?.cancel()
            Diagnostics.singleton = null
            this@KRobot.disabledInit()
        }

        override fun disabledPeriodic() {
            this@KRobot.disabledPeriodic()
        }

        override fun autonomousInit() {
            this@KRobot.autonomousInit()
        }

        override fun autonomousPeriodic() {
            this@KRobot.autonomousPeriodic()
        }

        override fun teleopInit() {
            this@KRobot.teleopInit()
        }

        override fun teleopPeriodic() {
            this@KRobot.teleopPeriodic()
        }

        override fun testInit() {
            LiveWindow.setEnabled(false)
            Diagnostics.singleton = Diagnostics()
            diagnostics(Diagnostics.singleton!!)
            Diagnostics.singleton!!.schedule()
        }

    }

    private val internalRobot = KRobotInternal()

    open fun robotInit() {}
    open fun robotPeriodic() {}
    open fun autonomousPeriodic() {}
    open fun autonomousInit() {}
    open fun disabledInit() {}
    open fun disabledPeriodic() {}
    open fun teleopInit() {}
    open fun teleopPeriodic() {}

    /**
     * Add commands to diagnostics here
     */
    open fun diagnostics(diagnostics: Diagnostics) {}

    fun initialize() {
        RobotBase.startRobot { internalRobot }
    }
}