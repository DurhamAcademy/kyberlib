package frc.team6502.kyberlib.diagnostics

import edu.wpi.first.wpilibj.command.CommandGroup

class DiagnosticSuite(val suiteName: String, vararg val commands: DiagnosticCommand): CommandGroup(){

    init {
        setRunWhenDisabled(true)
        for(i in 0 until commands.size){
            commands[i].suiteLength = commands.size
            commands[i].indexInSuite = i
            commands[i].setRunWhenDisabled(true)
            addSequential(commands[i])
        }
        print("Starting diagnostic suite $suiteName")
        start()
    }

    override fun end() {
        var success = true
        commands.forEach {
            if(!it.hasPassed()){
                success = false
            }
        }
        if(!success) error("One or more commands in diagnostic suite $suiteName failed")
    }

}