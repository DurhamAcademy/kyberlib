package frc.team6502.kyberlib.util.logging

import java.io.File

class CSVWriter(val filename: String) {
    private val f = File(filename)

    init {
        if (f.exists()) {
            f.delete()
        }
    }

    fun write(vararg csvWriteables: CSVWriteable) {
        if (!f.exists()) {
            println("File does not exist, creating...")
            f.createNewFile()
            println("Created ${f.absolutePath}")
            for (csvWriteable in csvWriteables.dropLast(1)) {
                f.appendText(csvWriteable.toCSVHeader() + ",")
            }
            f.appendText(csvWriteables.last().toCSVHeader() + "\n")
        }
        for (csvWriteable in csvWriteables.dropLast(1)) {
            f.appendText(csvWriteable.toCSV() + ",")
        }
        f.appendText(csvWriteables.last().toCSV() + "\n")
    }
}
