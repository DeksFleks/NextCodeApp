package dbataev.nextcodeapp.core.data.local.demo

import dbataev.nextcodeapp.core.common.*
import dbataev.nextcodeapp.core.data.remote.dto.*

/** Immutable content is copied into SQLite on the first launch; no download is needed. */
internal object DemoContent {
    data class Exercise(val task: TaskDto, val answer: String, val explanation: String)
    data class Lesson(val id: Long, val module: Int, val title: String, val theory: String, val xp: Int, val exercises: List<Exercise>)
    private fun choice(id: Long, question: String, options: List<String>, answer: String, explanation: String, multi: Boolean = false) =
        Exercise(TaskDto(id, TaskType.MULTIPLE_CHOICE, question, options.mapIndexed { index, text -> TaskOptionDto(id * 10 + index, text) }, multipleSelect = multi), answer, explanation)
    private fun blank(id: Long, question: String, before: String, after: String, answer: String, explanation: String) =
        Exercise(TaskDto(id, TaskType.FILL_BLANK, question, null, sentenceBefore = before, sentenceAfter = after, programmingLanguage = "java"), answer, explanation)
    private fun code(id: Long, question: String, starter: String, answer: String, explanation: String) =
        Exercise(TaskDto(id, TaskType.CODING, question, null, starterCode = starter, programmingLanguage = "java"), answer, explanation)

    val lessons = listOf(
        Lesson(1, 1, "Привет, Java!", "Java различает регистр букв. Команда System.out.println выводит текст и переводит строку, а System.out.print — только выводит. Текст записывают в двойных кавычках, команду завершают точкой с запятой.\n\nSystem.out.println(\"Hello\");", 25, listOf(
            choice(1, "Какая команда выведет Hello с переводом строки?", listOf("System.out.println(\"Hello\");", "System.out.print(\"Hello\");", "system.out.println(\"Hello\");"), "10", "println добавляет перевод строки. System пишется с большой S."),
            blank(2, "Дополни имя метода, который переводит строку.", "System.out.", "(\"Hello\");", "println", "Для вывода с переводом строки используется println."),
            code(3, "Выведи Java с переводом строки. Напиши точно: System.out.println(\"Java\");", "System.out.println(\"\");", "System.out.println(\"Java\");", "Строка Java должна находиться в двойных кавычках.")
        )),
        Lesson(2, 1, "Переменные", "Переменная хранит значение. int — целое число, String — текст. Сначала указывают тип, затем имя и значение. Имя не может начинаться с цифры.\n\nint age = 18;\nString name = \"Deks\";", 35, listOf(
            choice(4, "Выбери оба корректных объявления переменных.", listOf("int age = 18;", "String name = \"Deks\";", "int 2age = 18;"), "40,41", "Имя не может начинаться с цифры. Первые два объявления корректны.", true),
            blank(5, "Укажи тип целочисленной переменной.", "", " age = 18;", "int", "Тип int хранит целые числа."),
            code(6, "Объяви целую переменную: int score = 10;", "int score = ;", "int score = 10;", "Тип int, имя score и значение 10.")
        )),
        Lesson(3, 1, "Арифметика", "Java поддерживает +, -, * и /. Умножение выполняется раньше сложения. Скобки меняют порядок действий. При делении двух int дробная часть отбрасывается.\n\n2 + 3 * 4 = 14\n(2 + 3) * 4 = 20\n7 / 2 = 3", 40, listOf(
            choice(7, "Чему равно 2 + 3 * 4?", listOf("14", "20", "24"), "70", "Сначала 3 * 4 = 12, затем 2 + 12 = 14."),
            blank(8, "Вставь оператор умножения.", "int result = 6 ", " 7;", "*", "Звёздочка — оператор умножения."),
            code(9, "Запиши выражение со скобками точно: int result = (2 + 3) * 4;", "int result = ;", "int result = (2 + 3) * 4;", "Скобки заставляют сначала выполнить сложение.")
        )),
        Lesson(4, 2, "Условия", "Условие if выполняет код, если выражение истинно. else задаёт альтернативу. Операторы сравнения: ==, !=, >, <, >=, <=. Одно = присваивает значение, два == сравнивают.\n\nif (age >= 18) {\n    System.out.println(\"Welcome\");\n}", 45, listOf(
            choice(10, "Как сравнить два целых числа на равенство?", listOf("a == b", "a = b", "a => b"), "100", "Оператор == сравнивает, а = присваивает значение."),
            blank(11, "Дополни условие: возраст не меньше 18.", "if (age ", " 18) { }", ">=", ">= означает больше или равно."),
            code(12, "Объяви логическую переменную точно: boolean ready = true;", "boolean ready = ;", "boolean ready = true;", "Тип boolean принимает значения true и false.")
        )),
        Lesson(5, 2, "Циклы", "Цикл for повторяет действия. В скобках три части: начальное значение; условие продолжения; изменение счётчика. i++ увеличивает i на один.\n\nfor (int i = 0; i < 3; i++) {\n    System.out.println(i);\n}\n\nЭтот цикл выведет 0, 1 и 2 — всего три раза.", 50, listOf(
            choice(13, "Сколько раз выполнится тело for (int i = 0; i < 3; i++)?", listOf("3", "2", "4"), "130", "Счётчик принимает значения 0, 1 и 2."),
            blank(14, "Увеличь счётчик на один.", "i", ";", "++", "i++ увеличивает значение i на один."),
            code(15, "Объяви начальное значение счётчика точно: int i = 0;", "int i = ;", "int i = 0;", "Счётчик начнётся с нуля.")
        )),
        Lesson(6, 2, "Массивы", "Массив хранит несколько значений одного типа. Индексация начинается с нуля. length — количество элементов.\n\nint[] numbers = {10, 20, 30};\n\nПервый элемент numbers[0] равен 10, последний numbers[2] равен 30. numbers.length равен 3.", 55, listOf(
            choice(16, "Выбери оба верных утверждения для int[] a = {10, 20, 30};", listOf("a[0] == 10", "a.length == 3", "a[3] == 30"), "160,161", "Индексы этого массива — 0, 1 и 2. Длина равна 3.", true),
            blank(17, "Обратись к первому элементу массива.", "numbers[", "]", "0", "Первый элемент имеет индекс 0."),
            code(18, "Сохрани длину массива точно: int size = numbers.length;", "int size = ;", "int size = numbers.length;", "Свойство length возвращает число элементов массива.")
        ))
    )
    val achievements = listOf(
        AchievementDto("Первый шаг", "Пройди первый урок.", AchievementType.LESSONS_COMPLETED, 1, LevelAchievementType.BRONZE),
        AchievementDto("Уверенный старт", "Пройди три урока.", AchievementType.LESSONS_COMPLETED, 3, LevelAchievementType.SILVER),
        AchievementDto("Основы освоены", "Пройди все шесть уроков.", AchievementType.LESSONS_COMPLETED, 6, LevelAchievementType.GOLD),
        AchievementDto("Новый уровень", "Достигни второго уровня.", AchievementType.LEVEL_REACHED, 2, LevelAchievementType.BRONZE),
        AchievementDto("Коллекционер опыта", "Получи 200 XP.", AchievementType.XP_EARNED, 200, LevelAchievementType.SILVER),
        AchievementDto("В ритме", "Занимайся три дня подряд.", AchievementType.STREAK_DAYS, 3, LevelAchievementType.AMETHYST)
    )
    val quotes = listOf(
        QuoteDto("Большой путь начинается с одной строки кода.", "NextCode"),
        QuoteDto("Ошибка — это подсказка о следующем шаге.", "NextCode"),
        QuoteDto("Немного практики каждый день превращается в навык.", "NextCode")
    )
}
