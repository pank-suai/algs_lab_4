/**
 * Хэш-таблица с разрешением колизий
 *
 * @param size - размер таблицы
 */
class GoodHashTable(override val size: Int = 1500) : HashTable {
    private val table = Array(size) { LinearLinkedList<Item>() }


    /**
     * Хеширование (просто суммируем квадраты и берём остаток по размеру)
     */
    override fun hash(key: String): Int {
        require(HashTable.isValidKey(key)) { "Ключ не валидный" }
        val p = 31
        var hash = 0
        for (char in key) {
            hash += hash * p + char.code
        }

        return hash % size
    }

    override fun put(pair: Pair<String, String>) {
        val (key, value) = pair
        val index = hash(key)
        val existedItem = table[index].findBy { it.key == key }
        if (existedItem != null) {
            existedItem.value = value
            return
        }
        table[index].addFirst(Item(key, value))
    }

    override fun toString(): String =
        buildString {
            appendLine("{")
            table.forEach { items ->
                items.forEach { (key, value) ->
                    appendLine("\t$key: $value")
                }
            }
            appendLine("}")
        }

    override fun findByKey(key: String): Item? {
        val index = hash(key)
        table[index].forEach { item ->
            if (item.key == key) return item
        }
        return null
    }

    override fun findByValue(value: String): Item? {
        table.forEach {
            it.forEach { item ->
                if (item.value == value) return item
            }
        }
        return null
    }

    override fun remove(key: String) {
        val index = hash(key)
        val list = table[index]
        list.deleteFirstBy { it.key == key }
    }

    override fun exportCsv(): String = buildString {
        appendLine("index,size")
        table.forEachIndexed { index, items ->
            appendLine("$index,${items.size}")
        }
    }


}