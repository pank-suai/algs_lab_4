import java.util.LinkedList

/**
 * Хэш-таблица с разрешением колизий
 *
 * @param size - размер таблицы
 */
class GoodHashTable(override val size: Int = 1500) : HashTable {
    private val table = Array(size) { LinkedList<Item>() }


	/**
	 * Хеширование (просто суммируем квадраты и берём остаток по размеру)
	 */
	override fun hash(key: String): Int {
		require(HashTable.isValidKey(key)) { "Ключ не валидный" }
        val p = 31
        var hash = 0
        for (char in key){
            hash += hash * p + char.code
        }

        return hash % size
	}

	override fun put(pair: Pair<String, String>) {
		val (key, value) = pair
		val index = hash(key)
		table[index].forEach {
			if (it.key == key) {
				it.value = value
				return
			}
		}
		table[index].add(Item(key, value))
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

	override fun find(key: String): String? {
		val index = hash(key)
		table[index].forEach { item ->
			if (item.key == key) return item.value
		}
		return null
	}

	override fun remove(key: String) {
		val index = hash(key)
		val list = table[index]
		val iterator = list.iterator()
		while (iterator.hasNext()) {
			if (iterator.next().key == key) {
				iterator.remove()
			}
		}
	}

	override fun exportCsv(): String = buildString {
		appendLine("index,size")
		table.forEachIndexed { index, items ->
			appendLine("$index,${items.size}")
		}
	}


}