package frc.team6502.kyberlib.util.logging

class CSVField(val name: String, val value: Any) : CSVWriteable {
    override fun toCSV() = value.toString()
    override fun toCSVHeader() = name
}
