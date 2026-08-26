package id.artin.poolsanj

/** Persian digits + number-to-Persian-words converter. */
object NumFa {
    private val FA = mapOf(
        '0' to '۰', '1' to '۱', '2' to '۲', '3' to '۳', '4' to '۴',
        '5' to '۵', '6' to '۶', '7' to '۷', '8' to '۸', '9' to '۹'
    )

    fun faDigits(s: String): String = s.map { FA[it] ?: it }.joinToString("")

    private val ONES = listOf(
        "", "یک", "دو", "سه", "چهار", "پنج", "شش", "هفت", "هشت", "نه",
        "ده", "یازده", "دوازده", "سیزده", "چهارده", "پانزده", "شانزده",
        "هفده", "هجده", "نوزده"
    )
    private val TENS = listOf("", "", "بیست", "سی", "چهل", "پنجاه", "شصت", "هفتاد", "هشتاد", "نود")
    private val HUNDREDS = listOf(
        "", "صد", "دویست", "سیصد", "چهارصد", "پانصد", "ششصد", "هفتصد", "هشتصد", "نهصد"
    )
    private val SCALES = listOf("", " هزار", " میلیون", " میلیارد", " بیلیون")

    private fun threeDigits(n: Int): String {
        val parts = mutableListOf<String>()
        val h = n / 100
        val rest = n % 100
        if (h > 0) parts += HUNDREDS[h]
        when {
            rest == 0 -> {}
            rest < 20 -> parts += ONES[rest]
            else -> {
                val t = TENS[rest / 10]
                val o = ONES[rest % 10]
                parts += if (o.isEmpty()) t else "$t و $o"
            }
        }
        return parts.joinToString(" و ")
    }

    /** 25005000 -> "بیست و پنج میلیون و پنج هزار" */
    fun words(n: Long): String {
        if (n == 0L) return "صفر"
        if (n < 0) return "منفی " + words(-n)
        val chunks = mutableListOf<String>()
        var v = n
        var scale = 0
        while (v > 0) {
            val c = (v % 1000).toInt()
            if (c > 0) chunks += threeDigits(c) + SCALES[scale]
            v /= 1000
            scale++
        }
        return chunks.reversed().joinToString(" و ")
    }

    fun wordsToman(n: Long): String =
        "${words(n)} ${if (n != 1L) "تومان" else "تومان"}"

    private val GROUP = Regex("\\B(?=(\\d{3})+(?!\\d))")

    /** English grouping with Persian digits: 2005000 -> ۲٬۰۰۵٬۰۰۰ */
    fun grouped(s: String): String {
        val clean = s.replace(",", "").replace("٬", "")
        val lng = clean.toLongOrNull() ?: return s
        return faDigits(GROUP.replace(lng.toString(), "٬"))
    }
}
