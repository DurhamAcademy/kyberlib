package frc.team6502.kyberlib.path

import koma.pow

class Polynomial(vararg val coeffs: Double, val variableName: Char = 'x'){
    val degree = coeffs.size
    fun eval(x:Double): Double{
        var total = 0.0
        for(i in 0 until coeffs.size){
//            println(coeffs.size-i-1)
//            println(coeffs[i])
            total += coeffs[i] * x.pow(coeffs.size-i-1)
        }
        return total
    }

    override fun toString(): String {
        var s = ""
        for(i in 0 until coeffs.size){
            s += "${coeffs[i]}$variableName^${coeffs.size-i-1}"
            if(i < coeffs.size-1 && coeffs[i+1] >= 0.0) s += "+"
        }
        return s
    }
}