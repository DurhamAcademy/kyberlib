package frc.team6502.kyberlib.util.logging

interface CSVWriteable {
    fun toCSV(): String
    fun toCSVHeader(): String
}