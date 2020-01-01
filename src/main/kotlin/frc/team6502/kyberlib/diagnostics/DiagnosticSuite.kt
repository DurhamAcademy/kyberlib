package frc.team6502.kyberlib.diagnostics

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import frc.team6502.kyberlib.command.plusAssign


class DiagnosticSuite(val suiteName: String, vararg val commands: DiagnosticCommand) : SequentialCommandGroup() {

    internal var passed = 0

    init {
        for (i in commands.indices) {
            commands[i].suiteLength = commands.size
            commands[i].indexInSuite = i
            commands[i].suite = this
//            println(commands[i].commandName)
            this += commands[i]
        }
    }

    override fun initialize() {
        super.initialize()
        println("------- [$suiteName] -------")
    }

    override fun end(interrupted: Boolean) {
        super.end(interrupted)
        var success = true
        if(passed < commands.size) success = false
        println("[${if (success) "PASS" else "FAIL"}] Executed $passed command${if (passed != 1) "s" else ""}.")
        if (interrupted) println("[ABORTED] A command failed, so diagnostics have been canceled.")
    }

}
