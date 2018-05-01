package co.ello.android.ello

import kotlin.math.pow
import kotlin.math.round


val billion = 1000000000.0
val million = 1000000.0
val thousand = 1000.0


fun Int.numberToHuman(rounding: Int = 1, showZero: Boolean = false): String {
    if (this == 0 && !showZero) { return "" }

    val roundingFactor = 10.toDouble().pow(rounding.toDouble())
    val double = this.toDouble()
    val num: Double
    val suffix: String
    if (double >= billion) {
        num = round(double / billion * roundingFactor) / roundingFactor
        suffix = "B"
    }
    else if (double >= million) {
        num = round(double / million * roundingFactor) / roundingFactor
        suffix = "M"
    }
    else if (double >= thousand) {
        num = round(double / thousand * roundingFactor) / roundingFactor
        suffix = "K"
    }
    else {
        num = round(double * roundingFactor) / roundingFactor
        suffix = ""
    }
    var strNum = "$num"
    val strArr = strNum.split(".")
    if (strArr.last() == "0") {
        strNum = strArr.first()
    }
    return "$strNum$suffix"
}
