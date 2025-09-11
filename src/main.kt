import java.io.File

fun main() {

	// generateRandomDataCsvFile(10000)
	val badHT: HashTable = BadHashTable()
    badHT.fillDataFromCsvFile("hashes.csv")

	val fileCols = File("collisions.csv")
	fileCols.writeText(badHT.exportCsv())

    val goodHT: HashTable = GoodHashTable()
    goodHT.fillDataFromCsvFile("hashes.csv")
    val fileSizes = File("sizes.csv")
    fileSizes.writeText(goodHT.exportCsv())
}

/* Заполнение хеш таблицы сгенерированными данными
 */
fun HashTable.fillDataFromCsvFile(filename: String){
    val file = File(filename)
    file.readLines().forEachIndexed{ i, line ->
        if (i == 0) return@forEachIndexed

        put(line to i.toString())
    }
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
