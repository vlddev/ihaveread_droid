package vlad.ihaveread

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Utils {
}

/**
 * convert [dateStr] formatted as YYYY-MM-DD
 * to DD.MM.YYYY
 */
fun convDate(dateStr: String): String {
    var formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    var date = LocalDate.parse(dateStr)
    return date.format(formatter)
}
