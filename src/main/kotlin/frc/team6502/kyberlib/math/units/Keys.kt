package frc.team6502.kyberlib.math.units

sealed class KUnitKey

class Mul<T : KUnitKey, U : KUnitKey> : KUnitKey()
class Div<T : KUnitKey, U : KUnitKey> : KUnitKey()

object Unitless : KUnitKey()
object Meter : KUnitKey()
object Second : KUnitKey()
