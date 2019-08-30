package frc.team6502.kyberlib.diagnostics

import edu.wpi.first.wpilibj.frc2.command.SequentialCommandGroup
import frc.team6502.kyberlib.plusAssign


class DiagnosticSuite(val suiteName: String, vararg val commands: DiagnosticCommand) : SequentialCommandGroup() {

    init {
        for (i in 0 until commands.size) {
            commands[i].suiteLength = commands.size
            commands[i].indexInSuite = i
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
        var i = 0
        commands.forEach {
            if(!it.hasPassed()){
                success = false
            }
            if (it.executed) i++
        }
        println("[${if (success) "\u001b[32mPASS\u001B[0m" else "\u001b[31mFAIL\u001B[0m"}] Executed $i command${if (i != 1) "s" else ""}.")
        if (interrupted) println("[\u001B[31mABORTED\u001B[0m] A command failed, so diagnostics have been canceled.")
    }

}