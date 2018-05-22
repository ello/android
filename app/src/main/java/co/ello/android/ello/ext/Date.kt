package co.ello.android.ello

import java.util.Date
import kotlin.math.round


object TimeAgoInWordsStrings {
    val About = "~"
    val Almost = "<"
    val Days = "d"
    val Hours = "h"
    val LessThan = "<"
    val Minutes = "m"
    val Months = "mth"
    val Over = ">"
    val Seconds = "s"
    val Years = "y"
}

private fun div(a: Long, b: Long): Long = round(a.toFloat() / b.toFloat()).toLong()
private fun div(a: Int, b: Int): Int = round(a.toFloat() / b.toFloat()).toInt()

fun Date.timeAgo(): String {
    val now = Globals.now
    val deltaMs = now.time - this.time
    val deltaInSeconds = deltaMs / 1000L
    val ONE_YEAR_SECS = 31_536_000L
    if (deltaInSeconds > Int.MAX_VALUE)  return ">${div(deltaInSeconds, ONE_YEAR_SECS)}"

    val deltaInMinutes = div(deltaInSeconds, 60)
    val ONE_DAY = 1_440
    val ONE_AND_HALF_DAY = 2_160
    val THIRTY_DAYS = 43_200
    val SIXTY_DAYS = 86_400
    val ONE_YEAR = 525_600
    val QUARTER_YEAR = 131_400
    val THREE_QUARTERS_YEAR = 394_200

    return when (deltaInMinutes) {
        in 0..1 -> when(deltaInSeconds) {
            in 0..4   -> TimeAgoInWordsStrings.LessThan + "5" + TimeAgoInWordsStrings.Seconds
            in 5..9   -> TimeAgoInWordsStrings.LessThan + "10" + TimeAgoInWordsStrings.Seconds
            in 10..19 -> TimeAgoInWordsStrings.LessThan + "20" + TimeAgoInWordsStrings.Seconds
            in 20..39 -> "30" + TimeAgoInWordsStrings.Seconds
            in 40..59 -> TimeAgoInWordsStrings.LessThan + "1" + TimeAgoInWordsStrings.Minutes
            else      -> "1" + TimeAgoInWordsStrings.Minutes
        }
        in 2..45                         -> "${deltaInMinutes}" + TimeAgoInWordsStrings.Minutes
        in 45..90                        -> TimeAgoInWordsStrings.About + "1" + TimeAgoInWordsStrings.Hours
        in 90..ONE_DAY                   -> TimeAgoInWordsStrings.About + "${deltaInMinutes / 60}" + TimeAgoInWordsStrings.Hours
        in ONE_DAY..ONE_AND_HALF_DAY     -> "1" + TimeAgoInWordsStrings.Days
        in ONE_AND_HALF_DAY..THIRTY_DAYS -> "${deltaInMinutes / ONE_DAY}" + TimeAgoInWordsStrings.Days
        in THIRTY_DAYS..SIXTY_DAYS       -> TimeAgoInWordsStrings.About + "${deltaInMinutes / THIRTY_DAYS}" + TimeAgoInWordsStrings.Months
        in SIXTY_DAYS..ONE_YEAR          -> "${deltaInMinutes / THIRTY_DAYS}" + TimeAgoInWordsStrings.Months
        else -> {
            val remainder = deltaInMinutes / ONE_YEAR
            val distanceInYears = deltaInMinutes / ONE_YEAR
            if (remainder < QUARTER_YEAR) {
                TimeAgoInWordsStrings.About + "${distanceInYears}" + TimeAgoInWordsStrings.Years
            }
            else if (remainder < THREE_QUARTERS_YEAR) {
                TimeAgoInWordsStrings.Over + "${distanceInYears}" + TimeAgoInWordsStrings.Years
            }
            else {
                TimeAgoInWordsStrings.Almost + "${distanceInYears + 1}" + TimeAgoInWordsStrings.Years
            }
        }
    }
}
