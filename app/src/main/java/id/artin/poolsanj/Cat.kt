package id.artin.poolsanj

/** Market catalog — mirrors the PoolSanj bot. */
object Cat {
    val ARZ = listOf(
        "price_dollar_rl" to "🇺🇸 دلار آمریکا",
        "price_eur" to "🇪🇺 یورو",
        "price_aed" to "🇦🇪 درهم امارات",
        "price_gbp" to "🇬🇧 پوند انگلیس",
        "price_try" to "🇹🇷 لیر ترکیه",
        "price_cny" to "🇨🇳 یوان چین",
        "price_jpy" to "🇯🇵 ین ژاپن",
        "price_chf" to "🇨🇭 فرانک سوئیس",
        "price_cad" to "🇨🇦 دلار کانادا",
        "price_aud" to "🇦🇺 دلار استرالیا",
        "price_rub" to "🇷🇺 روبل روسیه",
        "price_sek" to "🇸🇪 کرون سوئد",
        "price_azn" to "🇦🇿 منات آذربایجان",
        "price_amd" to "🇦🇲 درام ارمنستان",
        "price_iqd" to "🇮🇶 دینار عراق",
        "price_kwd" to "🇰🇼 دینار کویت",
        "price_bhd" to "🇧🇭 دینار بحرین",
        "price_omr" to "🇴🇲 ریال عمان",
        "price_qar" to "🇶🇦 ریال قطر",
        "price_sar" to "🇸🇦 ریال عربستان",
        "price_myr" to "🇲🇾 رینگیت مالزی",
        "price_inr" to "🇮🇳 روپیه هند",
        "price_pkr" to "🇵🇰 روپیه پاکستان",
        "price_hkd" to "🇭🇰 دلار هنگ‌کنگ",
        "price_sgd" to "🇸🇬 دلار سنگاپور",
        "price_nok" to "🇳🇴 کرون نروژ",
        "price_dkk" to "🇩🇰 کرون دانمارک",
        "price_thb" to "🇹🇭 بات تایلند",
        "price_krw" to "🇰🇷 وون کره",
        "price_uah" to "🇺🇦 گریوانا اوکراین",
        "price_kzt" to "🇰🇿 تنگه قزاقستان",
        "price_gek" to "🇬🇪 لاری گرجستان",
        "price_nzd" to "🇳🇿 دلار نیوزیلند",
        "price_brl" to "🇧🇷 رئال برزیل",
        "price_mxn" to "🇲🇽 پزو مکزیک",
        "price_zar" to "🇿🇦 راند آفریقای جنوبی"
    )

    val TALA = listOf(
        "geram18" to "🏅 طلای ۱۸ عیار",
        "geram24" to "🏅 طلای ۲۴ عیار",
        "mesghal" to "⚖️ مثقال طلا",
        "gold_futures" to "📈 طلای بازار آتی",
        "ons_geram18" to "🌍 اونس جهانی (گرم ۱۸)",
        "silver_geram" to "⬜️ نقره (گرم)"
    )

    val SEKKE = listOf(
        "sekee" to "🪙 سکه امامی",
        "sekeb" to "🪙 بهار آزادی",
        "nim" to "🪙 نیم سکه",
        "rob" to "🪙 ربع سکه",
        "gerami" to "🪙 سکه گرمی",
        "coin_blubber" to "🫧 سکه حبابی"
    )

    val CRYPTO = listOf(
        "crypto-bitcoin" to "🟠 بیت‌کوین",
        "crypto-tether" to "💵 تتر",
        "crypto-ethereum" to "🔷 اتریوم",
        "crypto-binance-coin" to "🟡 بایننس کوین",
        "crypto-solana" to "🟣 سولانا",
        "crypto-ripple" to "✖️ ریپل",
        "crypto-dogecoin" to "🐕 دوج‌کوین",
        "crypto-tron" to "⚡️ ترون",
        "crypto-cardano" to "🔵 کاردانو",
        "crypto-litecoin" to "🔘 لایت‌کوین",
        "crypto-bitcoin-cash" to "🔶 بیت‌کوین کش",
        "crypto-stellar" to "⭐️ استلار",
        "crypto-polkadot" to "🔮 پولکادات",
        "crypto-avalanche" to "🔺 آوالانچ",
        "crypto-dash" to "🌀 دش",
        "crypto-shiba-inu" to "🐾 شیبا اینو",
        "crypto-toncoin" to "💎 تون‌کوین"
    )

    /** Summary = top-10 like the bot. */
    val SUMMARY = listOf(
        "price_dollar_rl", "price_eur", "sekee", "sekeb", "geram18",
        "mesghal", "ons", "crypto-bitcoin", "crypto-tether", "crypto-ethereum"
    )

    fun allSlugs(): List<String> =
        (ARZ + TALA + SEKKE + CRYPTO).map { it.first } + "ons"

    fun titleOf(slug: String): String? =
        (ARZ + TALA + SEKKE + CRYPTO).firstOrNull { it.first == slug }?.second
            ?: if (slug == "ons") "🌍 اونس جهانی" else null
}
