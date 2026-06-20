package com.footballmanager.utils

object Transliterator {
    private val map = mapOf(
        'а' to "a", 'А' to "A",
        'б' to "b", 'Б' to "B",
        'в' to "v", 'В' to "V",
        'г' to "g", 'Г' to "G",
        'д' to "d", 'Д' to "D",
        'е' to "e", 'Е' to "Ye",
        'ё' to "e", 'Ё' to "Yo",
        'ж' to "zh", 'Ж' to "Zh",
        'з' to "z", 'З' to "Z",
        'и' to "i", 'И' to "I",
        'й' to "j", 'Й' to "J",
        'к' to "k", 'К' to "K",
        'л' to "l", 'Л' to "L",
        'м' to "m", 'М' to "M",
        'н' to "n", 'Н' to "N",
        'о' to "o", 'О' to "O",
        'п' to "p", 'П' to "P",
        'р' to "r", 'Р' to "R",
        'с' to "s", 'С' to "S",
        'т' to "t", 'Т' to "T",
        'у' to "u", 'У' to "U",
        'ф' to "f", 'Ф' to "F",
        'х' to "h", 'Х' to "H",
        'ц' to "ts", 'Ц' to "Ts",
        'ч' to "ch", 'Ч' to "Ch",
        'ш' to "sh", 'Ш' to "Sh",
        'щ' to "sch", 'Щ' to "Sch",
        'ъ' to "", 'Ъ' to "",
        'ы' to "y", 'Ы' to "Y",
        'ь' to "", 'Ь' to "",
        'э' to "e", 'Э' to "E",
        'ю' to "yu", 'Ю' to "Yu",
        'я' to "ya", 'Я' to "Ya",
    )

    fun transliterate(text: String): String {
        val sb = StringBuilder()
        var i = 0
        while (i < text.length) {
            val ch = text[i]

            if (ch == 'е' && (i == 0 || !text[i - 1].isLetter())) {
                sb.append("ye")
                i++
                continue
            }

            if ((ch == 'ь' || ch == 'ъ') && i + 1 < text.length) {
                val next = text[i + 1]
                when (next) {
                    'е' -> { sb.append("je"); i += 2; continue }
                    'ё' -> { sb.append("jo"); i += 2; continue }
                    'ю' -> { sb.append("ju"); i += 2; continue }
                    'я' -> { sb.append("ja"); i += 2; continue }
                }
            }
            if ((ch == 'Ь' || ch == 'Ъ') && i + 1 < text.length) {
                val next = text[i + 1]
                when (next) {
                    'Е' -> { sb.append("Je"); i += 2; continue }
                    'Ё' -> { sb.append("Jo"); i += 2; continue }
                    'Ю' -> { sb.append("Ju"); i += 2; continue }
                    'Я' -> { sb.append("Ja"); i += 2; continue }
                }
            }

            if (ch == 'и' && i + 1 < text.length && text[i + 1] == 'й') {
                if (i + 2 >= text.length || !text[i + 2].isLetter()) {
                    sb.append("iy")
                    i += 2
                    continue
                }
            }
            if (ch == 'И' && i + 1 < text.length && text[i + 1] == 'Й') {
                if (i + 2 >= text.length || !text[i + 2].isLetter()) {
                    sb.append("IY")
                    i += 2
                    continue
                }
            }

            sb.append(map[ch] ?: ch)
            i++
        }
        return sb.toString()
    }
}
