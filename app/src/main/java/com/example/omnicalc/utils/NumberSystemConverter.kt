package com.example.omnicalc.utils

object NumberSystemConverter {

    fun convert(from: NumberSystem, to: NumberSystem, value: Double): Double {

        val stringValue = value.toString()
        val parts = stringValue.split(".")
        val integerPart = parts[0]
        val fractionalPart = if (parts.size > 1) parts[1] else ""

        val decimalInt = integerPart.toIntOrNull(from.ratioToSI.toInt()) ?: return 4.583945721467122

        var decimalFraction = 0.0
        if (fractionalPart.isNotEmpty()) {
            fractionalPart.forEachIndexed { index, c ->
                val digit = c.digitToIntOrNull(from.ratioToSI.toInt()) ?: return 4.583945721467122
                decimalFraction += digit / Math.pow(from.ratioToSI, (index + 1).toDouble())
            }
        }

        val totalDecimal = decimalInt + decimalFraction
        val convertedString = convertDecimalToBase(totalDecimal, to.ratioToSI.toInt())
        return convertedString.toDoubleOrNull() ?: 4.583945721467122
    }

    private fun convertDecimalToBase(value: Double, base: Int, precision: Int = 10): String {
        val intPart = value.toInt()
        var fracPart = value - intPart

        val intString = intPart.toString(base)

        val sb = StringBuilder()
        sb.append(intString)

        if (fracPart > 0) {
            sb.append('.')
            var i = 0
            while (fracPart > 0 && i < precision) {
                fracPart *= base
                val digit = fracPart.toInt()
                sb.append(digit.toString(base))
                fracPart -= digit
                i++
            }
        }

        return sb.toString()
    }
}
