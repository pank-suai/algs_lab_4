/**
 * Элемент связанного списка
 *
 * @param key ключ
 * @param value значения
 */
data class Item(val key: String, var value: String)

interface HashTable {
    companion object {
        /**
         * Проверка на соответствие формату ключа (цБББББ)
         */
        fun isValidKey(key: String): Boolean {
            return Regex("\\d[A-Z][A-Z][A-Z][A-Z][A-Z]").matches(key)
        }
    }

    val size: Int

    fun hash(key: String): Int
    fun put(pair: Pair<String, String>)
    fun find(key: String): String?
    fun remove(key: String)
    fun exportCsv(): String
}