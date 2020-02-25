package frc.team6502.kyberlib.math

import kotlin.math.pow

class Polynomial(vararg val coeffs: Double, val variableName: Char = 'x') {
    val degree = coeffs.size
    fun eval(x: Double): Double {
        var total = 0.0
        for (i in coeffs.indices) {
            total += coeffs[i] * x.pow(coeffs.size - i - 1)
        }
        return total
    }

    override fun toString(): String {
        var s = ""
        for (i in coeffs.indices) {
            s += "${coeffs[i]}$variableName^${coeffs.size - i - 1}"
            if (i < coeffs.size - 1 && coeffs[i + 1] >= 0.0) s += "+"
        }
        return s
    }
}
