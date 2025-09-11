import java.io.File
import java.util.*
import kotlin.random.Random

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


class BadHashTable(override val size: Int = 1500) : HashTable {
	private val table = Array<Item?>(size) { null }
	private val collisions = Array<Int>(size) { 0 }

	override fun hash(key: String): Int {
		require(HashTable.isValidKey(key)) { "Ключ не валидный" }
		return key.sumOf { it.code * it.code } % size
	}

	override fun put(pair: Pair<String, String>) {
		val (key, value) = pair
		val index = hash(key)
		if (table[index]?.key != key) {
			collisions[index]++
		}
		table[index] = Item(key, value)
	}

	override fun find(key: String): String? {
		val index = hash(key)
		return table.getOrNull(index)?.value
	}

	override fun remove(key: String) {
		val index = hash(key)
		table[index] = null
	}


	override fun toString(): String =
		buildString {
			appendLine("{")
			table.filterNotNull().forEach { (key, value) ->
				println("\t$key: $value")
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
		return key.sumOf { it.code * it.code } % size
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
		appendLine("index,how")
		table.forEachIndexed { index, items ->
			appendLine("$index,${items.size}")
		}
	}


}

fun main() {
	generateRandomDataCsv(10000)
	val ht: HashTable = BadHashTable()

	val file = File("hashes.csv")
	file.readLines().forEachIndexed{ i, line ->
		if (i == 0) return@forEachIndexed

		ht.put(line to i.toString())
	}
	val fileCols = File("collisions.csv")
	fileCols.writeText(ht.exportCsv())
}


/**
 * Генерация случайных данных для ключей
 * @param n количество сгенерированных данных
 * @param filename имя файла в котором сохраняем сгенерированные данные
 */
fun generateRandomDataCsv(n: Int, filename: String = "hashes.csv") {
	val file = File(filename)
	val AZ = ('A'..'Z').map { it.toString() }
	val nums = ('0'..'9').map { it.toString() }
	val writer = file.writer()
	writer.appendLine("code")
	repeat(n) {
		val code = nums.random() + AZ.random() + AZ.random() + AZ.random() + AZ.random() + AZ.random()
		writer.appendLine(code)
	}
	writer.close()
}
