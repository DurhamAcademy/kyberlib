package frc.team6502.kyberlib.diagnostics

import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.CommandGroup

abstract class DiagnosticCommand(val commandName: String): Command() {

    var indexInSuite = 0
    var suiteLength = 1

    override fun end() {
        cleanup()
        report()
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

    private fun report() {
        println("[${indexInSuite + 1}/$suiteLength] $commandName: ${if (hasPassed()) "PASS" else "FAIL"}")
    }
}