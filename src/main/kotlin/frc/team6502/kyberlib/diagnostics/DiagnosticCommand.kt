package frc.team6502.kyberlib.diagnostics

import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj2.command.CommandBase

abstract class DiagnosticCommand(val commandName: String, val timeout: Double = 0.0): CommandBase() {

    internal lateinit var suite: DiagnosticSuite
    var indexInSuite = 0
    var suiteLength = 1
    private var executed = false

    private val timer = Timer()

    init {
        name = commandName
    }

    final override fun initialize() {
        timer.reset()
        timer.start()
        setup()
    }

    final override fun end(interrupted: Boolean) {
        if (!interrupted && !executed) {
            executed = true
            report()
            if (!hasPassed()) Diagnostics.singleton?.cancel()
            if (hasPassed()) suite.passed++
            cleanup()
        }
    }

    /**
     * Put any cleanup in here, executed when the command finishes
     */
    abstract fun cleanup()
    abstract fun setup()

    override fun isFinished(): Boolean {
//        println(timer.get() >= timeout)
        return timer.get() >= timeout
    }
    /**
     * Called after the command completes.
     * @return true if test passed, false otherwise
     */
    abstract fun hasPassed(): Boolean

    /**
     * Reports the command's status
     */
    private fun report() {
        println(" - [${if (hasPassed()) "PASS" else "FAIL"}] $commandName (${indexInSuite + 1}/$suiteLength)")
    }
}