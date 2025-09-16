class BadHashTable(override val size: Int = 1500) : HashTable {
	private val table = Array<Item?>(size) { null }
	private val collisions = Array<Int>(size) { 0 }

	override fun hash(key: String): Int {
		require(HashTable.isValidKey(key)) { "Ключ не валидный" }
		return (((key[1].code + key[2].code + key[3].code + key[4].code + key[5].code) * (key[0].digitToInt() + 1)) % 1500 )
	}

	override fun put(pair: Pair<String, String>) {
		val (key, value) = pair
		val index = hash(key)
		if (table[index]?.key != null && table[index]?.key != key) {
            println("Коллизия: $key c ${table[index]?.key}. Элемент перезаписан.")
			collisions[index]++
		}
		table[index] = Item(key, value)
	}

	override fun findByKey(key: String): Item? {
		val index = hash(key)
		return table.getOrNull(index)
	}

    override fun findByValue(value: String): Item? {
        table.forEach {
             if (it?.value == value){
                 return  it
             }
        }
        return null
    }

    override fun remove(key: String) {
		val index = hash(key)
		table[index] = null
	}


	override fun toString(): String =
		buildString {
			appendLine("{")
			table.filterNotNull().forEach { (key, value) ->
				appendLine("\t$key: $value")
			}
			appendLine("}")
		}

	override fun exportCsv(): String = buildString {
		appendLine("index,collisions")
		collisions.forEachIndexed { index, collisions ->
			appendLine("$index,${collisions}")
		}
	}

}