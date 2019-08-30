package frc.team6502.kyberlib.diagnostics

import edu.wpi.first.wpilibj.frc2.command.SendableCommandBase

abstract class DiagnosticCommand(val commandName: String) : SendableCommandBase() {

    var indexInSuite = 0
    var suiteLength = 1
    var executed = false

    init {
        name = commandName
    }


    final override fun end(interrupted: Boolean) {
        if (!interrupted) {
            executed = true
            report()
            if (!hasPassed()) Diagnostics.cancel()
            cleanup()
        }
    }

    /**
     * Put any cleanup in here, executed when the command finishes
     */
    open fun cleanup() {

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
        println(" ${if (indexInSuite == 0) '┌' else '├'} [${if (hasPassed()) "\u001b[32mPASS\u001B[0m" else "\u001b[31mFAIL\u001B[0m"}] $commandName (${indexInSuite + 1}/$suiteLength)")
    }
}