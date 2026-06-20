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
        for (ch in text) {
            sb.append(map[ch] ?: ch)
        }
        return sb.toString()
    }
}
