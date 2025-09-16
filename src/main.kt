import java.io.File

fun main() {
    var variant: UShort? = null
    while (variant == null) {
        println("Выберете какую хеш-таблицу создать:")
        println("1. Без разрешения коллизий и полиномиального хеширования")
        println("2. С методом разрешений коллизий и полиномиальным хешированием")
        variant = readln().toUShortOrNull() ?: continue
        if (variant !in (1.toUShort()..2.toUShort())) {
            variant = null
            println("Введите корректный вариант")
        }
    }
    val hashTable = if (variant == 1.toUShort()) BadHashTable() else GoodHashTable()
    variant = null
    while (true) {
        while (variant == null) {
            println("Что вы хотите сделать?")
            println("1. Добавить/заменить элемент в таблице")
            println("2. Удалить элемент из таблицы")
            println("3. Вывести таблицу")
            println("4. Найти элемент в таблице")
            println("5. Сгенерировать файл с кодами")
            println("6. Импортировать файл с кодами")
            println("7. Сохранить информацию о коллизиях")
            println("8. Выйти")
            variant = readln().toUShortOrNull() ?: continue
            if (variant !in (1.toUShort()..8.toUShort())) {
                variant = null
                println("Введите корректный вариант")
            }
        }
        when (variant.toInt()) {
            1 -> hashTable.interactiveAdd()
            2 -> hashTable.interactiveDelete()
            3 -> println(hashTable.toString())
            4 -> hashTable.interactiveFind()
            5 -> interactiveGenerateRandomCodesCsvFile()
            6 -> hashTable.interactiveImportCodes()
            7 -> hashTable.interactiveSaveCollisions()
            8 -> break
        }
        variant = null
    }


}

private fun HashTable.interactiveSaveCollisions() {
    print("Введите название файла.")

    val filename = inputValue()
    val file = File(filename)
    file.writeText(this.exportCsv())
}

private fun HashTable.interactiveImportCodes() {
    print("Введите название файла.")
    val filename = inputValue()

    val file = File(filename)

    try {
        fillDataFromCsvFile(file)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// Тестовый сценарий
fun testTable() {
    generateRandomDataCsvFile(10000)

    val badHT: HashTable = BadHashTable()
    badHT.fillDataFromCsvFile("hashes.csv")

    val fileCols = File("collisions.csv")
    fileCols.writeText(badHT.exportCsv())

    val goodHT: HashTable = GoodHashTable()
    goodHT.fillDataFromCsvFile("hashes.csv")
    val fileSizes = File("sizes.csv")
    fileSizes.writeText(goodHT.exportCsv())
}

fun interactiveGenerateRandomCodesCsvFile() {
    var n: Int? = null
    while (n == null) {
        println("Введите количество ключей для генерации")
        n = readln().toIntOrNull() ?: continue
        if (n < 1) {
            n = null
        }
    }
    print("Введите название файла.")

    val filename = inputValue()
    generateRandomDataCsvFile(n, filename)
    println("Коды сохранены")
}

private fun HashTable.interactiveFind() {
    var isSearchByKey: Boolean? = null
    while (isSearchByKey == null) {
        println("Искать по ключу (1) или по значению (2)?")
        val input = readln().toIntOrNull() ?: continue
        isSearchByKey = if (input == 1) true else if (input == 2) false else continue
    }
    val element: Item?
    if (isSearchByKey) {
        val key = inputKey()
        element = this.findByKey(key)
    } else {
        val value = inputValue()
        element = this.findByValue(value)
    }
    if (element == null) {
        println("Элемент не найден")
        return
    }
    println("Найден элемент \"${element.key}\":\"${element.value}\"")


}

private fun HashTable.interactiveDelete() {
    val key = inputKey()
    this.remove(key)
    println("Элемент с ключом $key удален")
}


private fun HashTable.interactiveAdd() {
    val key: String = inputKey()
    val value: String = inputValue()
    this.put(key to value)
    println("Элемент \"$key\":\"$value\" добавлен")

}

private fun inputValue(): String {
    var value: String? = null
    while (value == null) {
        println("Введите значение:")
        value = readln()
        if (value.isBlank()) {
            value = null
            println("Вы ввели пустое значение")
        }

    }
    return value
}

private fun inputKey(): String {
    var key: String? = null
    while (key == null) {
        println("Введите ключ (цБББББ):")
        key = readln()
        if (!HashTable.isValidKey(key)) {
            key = null
            println("КЛЮЧ НЕ ВАЛИДНЫЙ")
        }
    }
    return key
}


/* Заполнение хеш таблицы сгенерированными данными
 */
fun HashTable.fillDataFromCsvFile(filename: String) {
    val file = File(filename)
    fillDataFromCsvFile(file)
}

private fun HashTable.fillDataFromCsvFile(file: File) {
    file.readLines().forEachIndexed { i, line ->
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
