import java.io.File

fun main() {
	generateRandomDataCsvFile(10000)
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
fun generateRandomDataCsvFile(n: Int, filename: String = "hashes.csv") {
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
