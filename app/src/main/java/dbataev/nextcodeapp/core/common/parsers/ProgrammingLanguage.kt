package dbataev.nextcodeapp.core.common.parsers

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import dbataev.nextcodeapp.core.designsystem.theme.NcAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcClassCodeColor
import dbataev.nextcodeapp.core.designsystem.theme.NcEscapeCodeColor
import dbataev.nextcodeapp.core.designsystem.theme.NcMethodCodeColor
import dbataev.nextcodeapp.core.designsystem.theme.NcSecondMethodCodeColor
import dbataev.nextcodeapp.core.designsystem.theme.NcStringCodeColor
import dbataev.nextcodeapp.core.designsystem.theme.NcThirdAccentColor

object ProgrammingLanguage {

    fun highlightJavaCode(code: String): AnnotatedString {
        val keywords = setOf(
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while",
            "true", "false", "null"
        )

        val commonClasses = setOf(
            "String", "System", "Integer", "Double", "Float",
            "Boolean", "Character", "Long", "Short", "Byte",
            "Math", "Scanner", "ArrayList", "List", "Map", "Set"
        )

        val commonMethods = setOf(
            "out", "println", "print", "printf", "main", "nextLine",
            "nextInt", "length", "charAt", "equals", "toString"
        )

        val keywordStyle = SpanStyle(
            color = NcClassCodeColor,
            fontWeight = FontWeight.Bold
        )

        val classStyle = SpanStyle(
            color = NcAccentColor
        )

        val methodStyle = SpanStyle(
            color = NcSecondMethodCodeColor
        )

        val stringStyle = SpanStyle(
            color = NcStringCodeColor
        )

        val escapeStyle = SpanStyle(
            color = NcEscapeCodeColor
        )

        val numberStyle = SpanStyle(
            color = NcAccentColor
        )

        val commentStyle = SpanStyle(
            color = Color(0xFF808080)
        )

        val annotationStyle = SpanStyle(
            color = Color(0xFFBBB529)
        )

        val defaultStyle = SpanStyle(
            color = NcAccentColor
        )

        return buildAnnotatedString {
            var i = 0

            fun appendStyled(text: String, style: SpanStyle) {
                withStyle(style) {
                    append(text)
                }
            }

            fun appendStringOrCharLiteral(quote: Char) {
                appendStyled(quote.toString(), stringStyle)
                i++

                while (i < code.length) {
                    when {
                        code[i] == '\\' -> {
                            val escapeStart = i
                            i++

                            if (i < code.length) {
                                when (code[i]) {
                                    'u' -> {
                                        i++

                                        repeat(4) {
                                            if (i < code.length && code[i].isLetterOrDigit()) {
                                                i++
                                            }
                                        }
                                    }

                                    else -> {
                                        i++
                                    }
                                }
                            }

                            appendStyled(
                                text = code.substring(escapeStart, i),
                                style = escapeStyle
                            )
                        }

                        code[i] == quote -> {
                            appendStyled(quote.toString(), stringStyle)
                            i++
                            break
                        }

                        else -> {
                            appendStyled(code[i].toString(), stringStyle)
                            i++
                        }
                    }
                }
            }

            fun appendEscapeSequence() {
                val escapeStart = i
                i++

                if (i < code.length) {
                    when (code[i]) {
                        'u' -> {
                            i++

                            repeat(4) {
                                if (i < code.length && code[i].isLetterOrDigit()) {
                                    i++
                                }
                            }
                        }

                        else -> {
                            i++
                        }
                    }
                }

                appendStyled(
                    text = code.substring(escapeStart, i),
                    style = escapeStyle
                )
            }

            while (i < code.length) {
                when {
                    code.startsWith("//", i) -> {
                        val end = code.indexOf('\n', startIndex = i)
                            .let { if (it == -1) code.length else it }

                        appendStyled(
                            text = code.substring(i, end),
                            style = commentStyle
                        )

                        i = end
                    }

                    code.startsWith("/*", i) -> {
                        val end = code.indexOf("*/", startIndex = i + 2)
                            .let { if (it == -1) code.length else it + 2 }

                        appendStyled(
                            text = code.substring(i, end),
                            style = commentStyle
                        )

                        i = end
                    }

                    // ВАЖНО: обработка \n, \t, \", \\ вне строк
                    code[i] == '\\' -> {
                        appendEscapeSequence()
                    }

                    code[i] == '"' -> {
                        appendStringOrCharLiteral('"')
                    }

                    code[i] == '\'' -> {
                        appendStringOrCharLiteral('\'')
                    }

                    code[i] == '@' -> {
                        val start = i
                        i++

                        while (
                            i < code.length &&
                            (code[i].isLetterOrDigit() || code[i] == '_')
                        ) {
                            i++
                        }

                        appendStyled(
                            text = code.substring(start, i),
                            style = annotationStyle
                        )
                    }

                    code[i].isDigit() -> {
                        val start = i
                        i++

                        while (
                            i < code.length &&
                            (
                                    code[i].isDigit() ||
                                            code[i] == '.' ||
                                            code[i] == '_' ||
                                            code[i] == 'L' ||
                                            code[i] == 'l' ||
                                            code[i] == 'f' ||
                                            code[i] == 'F' ||
                                            code[i] == 'd' ||
                                            code[i] == 'D'
                                    )
                        ) {
                            i++
                        }

                        appendStyled(
                            text = code.substring(start, i),
                            style = numberStyle
                        )
                    }

                    code[i].isLetter() || code[i] == '_' -> {
                        val start = i
                        i++

                        while (
                            i < code.length &&
                            (code[i].isLetterOrDigit() || code[i] == '_')
                        ) {
                            i++
                        }

                        val word = code.substring(start, i)

                        when {
                            word in keywords -> appendStyled(word, keywordStyle)
                            word in commonClasses -> appendStyled(word, classStyle)
                            word in commonMethods -> appendStyled(word, methodStyle)
                            else -> appendStyled(word, defaultStyle)
                        }
                    }

                    else -> {
                        appendStyled(
                            text = code[i].toString(),
                            style = defaultStyle
                        )
                        i++
                    }
                }
            }
        }
    }

    fun highlightPythonCode(code: String): AnnotatedString {
        val keywords = setOf(
            "False", "None", "True",
            "and", "as", "assert", "async", "await",
            "break", "case", "class", "continue",
            "def", "del", "elif", "else", "except",
            "finally", "for", "from", "global",
            "if", "import", "in", "is",
            "lambda", "match", "nonlocal", "not",
            "or", "pass", "raise", "return",
            "try", "while", "with", "yield"
        )

        val builtInTypes = setOf(
            "bool", "bytearray", "bytes", "complex",
            "dict", "float", "frozenset", "int",
            "list", "memoryview", "object", "range",
            "set", "slice", "str", "tuple", "type",

            "BaseException", "Exception",
            "ArithmeticError", "AssertionError",
            "AttributeError", "EOFError",
            "ImportError", "IndexError",
            "KeyError", "LookupError",
            "NameError", "NotImplementedError",
            "OSError", "OverflowError",
            "RuntimeError", "StopIteration",
            "SyntaxError", "TypeError",
            "ValueError", "ZeroDivisionError"
        )

        val builtInFunctions = setOf(
            "abs", "all", "any", "ascii",
            "bin", "breakpoint", "callable", "chr",
            "classmethod", "compile", "delattr",
            "dir", "divmod", "enumerate",
            "eval", "exec", "filter", "format",
            "getattr", "globals", "hasattr",
            "hash", "help", "hex", "id",
            "input", "isinstance", "issubclass",
            "iter", "len", "locals", "map",
            "max", "min", "next", "oct",
            "open", "ord", "pow", "print",
            "property", "repr", "reversed",
            "round", "setattr", "sorted",
            "staticmethod", "sum", "super",
            "vars", "zip", "__import__"
        )

        /*
         * Палитра, близкая к PyCharm Darcula:
         *
         * Default:      #A9B7C6
         * Keywords:     #CC7832
         * Strings:      #6A8759
         * Numbers:      #6897BB
         * Functions:    #FFC66D
         * Built-ins:    #8888C6
         * Comments:     #808080
         * Decorators:   #BBB529
         */

        val defaultStyle = SpanStyle(
            color = Color(0xFFA9B7C6)
        )

        val keywordStyle = SpanStyle(
            color = Color(0xFFCC7832),
            fontWeight = FontWeight.Bold
        )

        val builtInTypeStyle = SpanStyle(
            color = Color(0xFF8888C6)
        )

        val builtInFunctionStyle = SpanStyle(
            color = Color(0xFF8888C6)
        )

        val functionStyle = SpanStyle(
            color = Color(0xFFFFC66D)
        )

        val functionDeclarationStyle = SpanStyle(
            color = Color(0xFFFFC66D),
            fontWeight = FontWeight.Bold
        )

        val classDeclarationStyle = SpanStyle(
            color = Color(0xFFA9B7C6),
            fontWeight = FontWeight.Bold
        )

        val stringStyle = SpanStyle(
            color = Color(0xFF6A8759)
        )

        val escapeStyle = SpanStyle(
            color = Color(0xFFCC7832),
            fontWeight = FontWeight.Bold
        )

        val numberStyle = SpanStyle(
            color = Color(0xFF6897BB)
        )

        val commentStyle = SpanStyle(
            color = Color(0xFF808080),
            fontStyle = FontStyle.Italic
        )

        val decoratorStyle = SpanStyle(
            color = Color(0xFFBBB529)
        )

        val selfStyle = SpanStyle(
            color = Color(0xFF94558D),
            fontStyle = FontStyle.Italic
        )

        val dunderStyle = SpanStyle(
            color = Color(0xFF9876AA)
        )

        return buildAnnotatedString {
            var i = 0

            var expectFunctionName = false
            var expectClassName = false

            fun appendStyled(
                text: String,
                style: SpanStyle
            ) {
                withStyle(style) {
                    append(text)
                }
            }

            fun isHexDigit(char: Char): Boolean {
                return char.isDigit() ||
                        char in 'a'..'f' ||
                        char in 'A'..'F'
            }

            fun isDecoratorStart(index: Int): Boolean {
                var currentIndex = index - 1

                while (
                    currentIndex >= 0 &&
                    code[currentIndex] != '\n'
                ) {
                    if (!code[currentIndex].isWhitespace()) {
                        return false
                    }

                    currentIndex--
                }

                return true
            }

            fun nextNonWhitespaceIndex(startIndex: Int): Int {
                var currentIndex = startIndex

                while (
                    currentIndex < code.length &&
                    (
                            code[currentIndex] == ' ' ||
                                    code[currentIndex] == '\t'
                            )
                ) {
                    currentIndex++
                }

                return currentIndex
            }

            fun getStringPrefixLength(index: Int): Int {
                val prefixes = listOf(
                    "fr", "rf",
                    "br", "rb",
                    "f", "r", "b", "u"
                )

                for (prefix in prefixes) {
                    val quoteIndex = index + prefix.length

                    if (
                        quoteIndex < code.length &&
                        code.regionMatches(
                            thisOffset = index,
                            other = prefix,
                            otherOffset = 0,
                            length = prefix.length,
                            ignoreCase = true
                        ) &&
                        (
                                code[quoteIndex] == '"' ||
                                        code[quoteIndex] == '\''
                                )
                    ) {
                        return prefix.length
                    }
                }

                return 0
            }

            fun appendEscapeSequence() {
                val escapeStart = i

                // Пропускаем обратный слеш.
                i++

                if (i >= code.length) {
                    appendStyled(
                        text = code.substring(escapeStart, i),
                        style = escapeStyle
                    )
                    return
                }

                when (code[i]) {
                    'x' -> {
                        i++

                        repeat(2) {
                            if (
                                i < code.length &&
                                isHexDigit(code[i])
                            ) {
                                i++
                            }
                        }
                    }

                    'u' -> {
                        i++

                        repeat(4) {
                            if (
                                i < code.length &&
                                isHexDigit(code[i])
                            ) {
                                i++
                            }
                        }
                    }

                    'U' -> {
                        i++

                        repeat(8) {
                            if (
                                i < code.length &&
                                isHexDigit(code[i])
                            ) {
                                i++
                            }
                        }
                    }

                    'N' -> {
                        i++

                        if (
                            i < code.length &&
                            code[i] == '{'
                        ) {
                            i++

                            while (
                                i < code.length &&
                                code[i] != '}'
                            ) {
                                i++
                            }

                            if (
                                i < code.length &&
                                code[i] == '}'
                            ) {
                                i++
                            }
                        }
                    }

                    in '0'..'7' -> {
                        var digitsCount = 0

                        while (
                            i < code.length &&
                            code[i] in '0'..'7' &&
                            digitsCount < 3
                        ) {
                            i++
                            digitsCount++
                        }
                    }

                    else -> {
                        i++
                    }
                }

                appendStyled(
                    text = code.substring(escapeStart, i),
                    style = escapeStyle
                )
            }

            fun appendPythonString(prefixLength: Int = 0) {
                val prefixStart = i
                val prefixEnd = i + prefixLength

                val prefix = if (prefixLength > 0) {
                    code.substring(prefixStart, prefixEnd)
                } else {
                    ""
                }

                val isRawString = prefix.contains(
                    other = "r",
                    ignoreCase = true
                )

                val isFormattedString = prefix.contains(
                    other = "f",
                    ignoreCase = true
                )

                if (prefixLength > 0) {
                    appendStyled(
                        text = prefix,
                        style = keywordStyle
                    )

                    i += prefixLength
                }

                if (i >= code.length) {
                    return
                }

                val quote = code[i]

                val isTripleQuoted =
                    i + 2 < code.length &&
                            code[i + 1] == quote &&
                            code[i + 2] == quote

                val delimiterLength = if (isTripleQuoted) 3 else 1

                appendStyled(
                    text = code.substring(
                        startIndex = i,
                        endIndex = i + delimiterLength
                    ),
                    style = stringStyle
                )

                i += delimiterLength

                while (i < code.length) {
                    val isClosingDelimiter = if (isTripleQuoted) {
                        i + 2 < code.length &&
                                code[i] == quote &&
                                code[i + 1] == quote &&
                                code[i + 2] == quote
                    } else {
                        code[i] == quote
                    }

                    when {
                        isClosingDelimiter -> {
                            appendStyled(
                                text = code.substring(
                                    startIndex = i,
                                    endIndex = i + delimiterLength
                                ),
                                style = stringStyle
                            )

                            i += delimiterLength
                            break
                        }

                        code[i] == '\\' -> {
                            if (isRawString) {
                                /*
                                 * Даже в raw-строках обратный слеш перед кавычкой
                                 * влияет на определение конца строки.
                                 */
                                val end = minOf(
                                    i + 2,
                                    code.length
                                )

                                appendStyled(
                                    text = code.substring(i, end),
                                    style = stringStyle
                                )

                                i = end
                            } else {
                                appendEscapeSequence()
                            }
                        }

                        isFormattedString &&
                                (code[i] == '{' || code[i] == '}') -> {
                            val brace = code[i]

                            /*
                             * {{ и }} внутри f-строки означают обычную скобку.
                             */
                            if (
                                i + 1 < code.length &&
                                code[i + 1] == brace
                            ) {
                                appendStyled(
                                    text = code.substring(i, i + 2),
                                    style = stringStyle
                                )

                                i += 2
                            } else {
                                appendStyled(
                                    text = brace.toString(),
                                    style = escapeStyle
                                )

                                i++
                            }
                        }

                        !isTripleQuoted && code[i] == '\n' -> {
                            /*
                             * Обычная строка не может продолжаться на следующей
                             * строке без escape-последовательности.
                             */
                            break
                        }

                        else -> {
                            appendStyled(
                                text = code[i].toString(),
                                style = stringStyle
                            )

                            i++
                        }
                    }
                }
            }

            fun appendNumber() {
                val start = i

                if (
                    code[i] == '.' &&
                    i + 1 < code.length &&
                    code[i + 1].isDigit()
                ) {
                    i++

                    while (
                        i < code.length &&
                        (code[i].isDigit() || code[i] == '_')
                    ) {
                        i++
                    }
                } else if (
                    code[i] == '0' &&
                    i + 1 < code.length
                ) {
                    when (code[i + 1].lowercaseChar()) {
                        'x' -> {
                            i += 2

                            while (
                                i < code.length &&
                                (
                                        isHexDigit(code[i]) ||
                                                code[i] == '_'
                                        )
                            ) {
                                i++
                            }
                        }

                        'b' -> {
                            i += 2

                            while (
                                i < code.length &&
                                (
                                        code[i] == '0' ||
                                                code[i] == '1' ||
                                                code[i] == '_'
                                        )
                            ) {
                                i++
                            }
                        }

                        'o' -> {
                            i += 2

                            while (
                                i < code.length &&
                                (
                                        code[i] in '0'..'7' ||
                                                code[i] == '_'
                                        )
                            ) {
                                i++
                            }
                        }

                        else -> {
                            while (
                                i < code.length &&
                                (code[i].isDigit() || code[i] == '_')
                            ) {
                                i++
                            }
                        }
                    }
                } else {
                    while (
                        i < code.length &&
                        (code[i].isDigit() || code[i] == '_')
                    ) {
                        i++
                    }
                }

                /*
                 * Дробная часть.
                 */
                if (
                    i < code.length &&
                    code[i] == '.'
                ) {
                    i++

                    while (
                        i < code.length &&
                        (code[i].isDigit() || code[i] == '_')
                    ) {
                        i++
                    }
                }

                /*
                 * Экспоненциальная запись:
                 * 1e10
                 * 1.5e-4
                 */
                if (
                    i < code.length &&
                    (code[i] == 'e' || code[i] == 'E')
                ) {
                    i++

                    if (
                        i < code.length &&
                        (code[i] == '+' || code[i] == '-')
                    ) {
                        i++
                    }

                    while (
                        i < code.length &&
                        (code[i].isDigit() || code[i] == '_')
                    ) {
                        i++
                    }
                }

                /*
                 * Комплексное число:
                 * 5j
                 * 2.5J
                 */
                if (
                    i < code.length &&
                    (code[i] == 'j' || code[i] == 'J')
                ) {
                    i++
                }

                appendStyled(
                    text = code.substring(start, i),
                    style = numberStyle
                )
            }

            while (i < code.length) {
                val stringPrefixLength = if (code[i].isLetter()) {
                    getStringPrefixLength(i)
                } else {
                    0
                }

                when {
                    /*
                     * Однострочные комментарии Python.
                     */
                    code[i] == '#' -> {
                        val end = code.indexOf(
                            char = '\n',
                            startIndex = i
                        ).let { index ->
                            if (index == -1) code.length else index
                        }

                        appendStyled(
                            text = code.substring(i, end),
                            style = commentStyle
                        )

                        i = end
                    }

                    /*
                     * Обычные и тройные строки.
                     */
                    code[i] == '"' || code[i] == '\'' -> {
                        appendPythonString()
                    }

                    /*
                     * Строки с префиксами:
                     * f"..."
                     * r"..."
                     * fr"..."
                     */
                    stringPrefixLength > 0 -> {
                        appendPythonString(stringPrefixLength)
                    }

                    /*
                     * Декораторы:
                     * @staticmethod
                     * @app.route
                     */
                    code[i] == '@' && isDecoratorStart(i) -> {
                        val start = i
                        i++

                        while (
                            i < code.length &&
                            (
                                    code[i].isLetterOrDigit() ||
                                            code[i] == '_' ||
                                            code[i] == '.'
                                    )
                        ) {
                            i++
                        }

                        appendStyled(
                            text = code.substring(start, i),
                            style = decoratorStyle
                        )
                    }

                    /*
                     * Целые, дробные и комплексные числа.
                     */
                    code[i].isDigit() ||
                            (
                                    code[i] == '.' &&
                                            i + 1 < code.length &&
                                            code[i + 1].isDigit()
                                    ) -> {
                        appendNumber()
                    }

                    /*
                     * Идентификаторы, ключевые слова,
                     * функции, классы и встроенные элементы.
                     */
                    code[i].isLetter() || code[i] == '_' -> {
                        val start = i
                        i++

                        while (
                            i < code.length &&
                            (
                                    code[i].isLetterOrDigit() ||
                                            code[i] == '_'
                                    )
                        ) {
                            i++
                        }

                        val word = code.substring(start, i)
                        val nextIndex = nextNonWhitespaceIndex(i)

                        val isFunctionCall =
                            nextIndex < code.length &&
                                    code[nextIndex] == '('

                        when {
                            expectFunctionName -> {
                                appendStyled(
                                    text = word,
                                    style = functionDeclarationStyle
                                )

                                expectFunctionName = false
                            }

                            expectClassName -> {
                                appendStyled(
                                    text = word,
                                    style = classDeclarationStyle
                                )

                                expectClassName = false
                            }

                            word in keywords -> {
                                appendStyled(
                                    text = word,
                                    style = keywordStyle
                                )

                                when (word) {
                                    "def" -> {
                                        expectFunctionName = true
                                    }

                                    "class" -> {
                                        expectClassName = true
                                    }
                                }
                            }

                            word == "self" || word == "cls" -> {
                                appendStyled(
                                    text = word,
                                    style = selfStyle
                                )
                            }

                            word.startsWith("__") &&
                                    word.endsWith("__") &&
                                    word.length > 4 -> {
                                appendStyled(
                                    text = word,
                                    style = dunderStyle
                                )
                            }

                            word in builtInTypes -> {
                                appendStyled(
                                    text = word,
                                    style = builtInTypeStyle
                                )
                            }

                            word in builtInFunctions -> {
                                appendStyled(
                                    text = word,
                                    style = builtInFunctionStyle
                                )
                            }

                            isFunctionCall -> {
                                appendStyled(
                                    text = word,
                                    style = functionStyle
                                )
                            }

                            else -> {
                                appendStyled(
                                    text = word,
                                    style = defaultStyle
                                )
                            }
                        }
                    }

                    else -> {
                        if (code[i] == '\n') {
                            /*
                             * Не переносим ожидание имени функции или класса
                             * на следующую строку для некорректного кода.
                             */
                            expectFunctionName = false
                            expectClassName = false
                        }

                        appendStyled(
                            text = code[i].toString(),
                            style = defaultStyle
                        )

                        i++
                    }
                }
            }
        }
    }

    fun highlightCSharpCode(code: String): AnnotatedString {
        val keywords = setOf(
            // Основные ключевые слова
            "abstract", "as", "base", "bool", "break", "byte",
            "case", "catch", "char", "checked", "class", "const",
            "continue", "decimal", "default", "delegate", "do",
            "double", "else", "enum", "event", "explicit", "extern",
            "false", "finally", "fixed", "float", "for", "foreach",
            "goto", "if", "implicit", "in", "int", "interface",
            "internal", "is", "lock", "long", "namespace", "new",
            "null", "object", "operator", "out", "override", "params",
            "private", "protected", "public", "readonly", "ref",
            "return", "sbyte", "sealed", "short", "sizeof",
            "stackalloc", "static", "string", "struct", "switch",
            "this", "throw", "true", "try", "typeof", "uint",
            "ulong", "unchecked", "unsafe", "ushort", "using",
            "virtual", "void", "volatile", "while",

            // Контекстные ключевые слова
            "add", "alias", "and", "ascending", "async", "await",
            "by", "descending", "dynamic", "equals", "file", "from",
            "get", "global", "group", "init", "into", "join",
            "let", "managed", "nameof", "nint", "not", "notnull",
            "nuint", "on", "or", "orderby", "partial", "record",
            "remove", "required", "scoped", "select", "set",
            "unmanaged", "value", "var", "when", "where", "with",
            "yield"
        )

        val commonClasses = setOf(
            "String", "Object", "Console", "Math", "Convert",
            "DateTime", "TimeSpan", "Random", "Guid",
            "Exception", "ArgumentException", "InvalidOperationException",
            "List", "Dictionary", "HashSet", "Queue", "Stack",
            "IEnumerable", "ICollection", "IList", "IDictionary",
            "Task", "Thread", "CancellationToken",
            "StringBuilder", "File", "Directory", "Path",
            "Enumerable", "Array", "Tuple"
        )

        val commonMethods = setOf(
            "Main", "Write", "WriteLine", "Read", "ReadLine",
            "Parse", "TryParse", "ToString", "Equals",
            "GetHashCode", "GetType", "CompareTo",
            "Add", "AddRange", "Remove", "RemoveAt", "Clear",
            "Contains", "Count", "Length", "IndexOf",
            "Substring", "Replace", "Split", "Trim",
            "ToUpper", "ToLower", "StartsWith", "EndsWith",
            "Where", "Select", "OrderBy", "First", "FirstOrDefault",
            "Any", "All", "Sum", "Min", "Max",
            "Run", "Delay", "Start", "Dispose"
        )


        val keywordStyle = SpanStyle(
            color = Color(0xFF569CD6), // class, public, void, return
            fontWeight = FontWeight.Bold
        )

        val classStyle = SpanStyle(
            color = Color(0xFF4EC9B0) // Console, String, List, DateTime
        )

        val methodStyle = SpanStyle(
            color = Color(0xFFDCDCAA) // WriteLine, ToString, Add
        )

        val stringStyle = SpanStyle(
            color = Color(0xFFCE9178) // "Hello, World!"
        )

        val escapeStyle = SpanStyle(
            color = Color(0xFFD7BA7D), // \n, \t, \"
            fontWeight = FontWeight.Medium
        )

        val numberStyle = SpanStyle(
            color = Color(0xFFB5CEA8) // 10, 3.14, 0xFF
        )

        val commentStyle = SpanStyle(
            color = Color(0xFF6A9955) // // comment
        )

        val preprocessorStyle = SpanStyle(
            color = Color(0xFFC586C0) // #if, #region, #define
        )

        val attributeStyle = SpanStyle(
            color = Color(0xFFDCDCAA) // [Serializable], [Obsolete]
        )

        val defaultStyle = SpanStyle(
            color = Color(0xFFD4D4D4) // переменные, операторы, скобки
        )

        return buildAnnotatedString {
            var i = 0

            fun appendStyled(
                text: String,
                style: SpanStyle
            ) {
                withStyle(style) {
                    append(text)
                }
            }

            fun appendEscapeSequence() {
                val start = i
                i++

                if (i < code.length) {
                    when (code[i]) {
                        'u' -> {
                            i++

                            repeat(4) {
                                if (
                                    i < code.length &&
                                    code[i].isLetterOrDigit()
                                ) {
                                    i++
                                }
                            }
                        }

                        'U' -> {
                            i++

                            repeat(8) {
                                if (
                                    i < code.length &&
                                    code[i].isLetterOrDigit()
                                ) {
                                    i++
                                }
                            }
                        }

                        'x' -> {
                            i++

                            repeat(4) {
                                if (
                                    i < code.length &&
                                    code[i].isLetterOrDigit()
                                ) {
                                    i++
                                }
                            }
                        }

                        else -> i++
                    }
                }

                appendStyled(
                    text = code.substring(start, i),
                    style = escapeStyle
                )
            }

            fun appendNormalStringOrChar(quote: Char) {
                appendStyled(
                    text = quote.toString(),
                    style = stringStyle
                )

                i++

                while (i < code.length) {
                    when {
                        code[i] == '\\' -> {
                            appendEscapeSequence()
                        }

                        code[i] == quote -> {
                            appendStyled(
                                text = quote.toString(),
                                style = stringStyle
                            )

                            i++
                            break
                        }

                        else -> {
                            appendStyled(
                                text = code[i].toString(),
                                style = stringStyle
                            )

                            i++
                        }
                    }
                }
            }

            /*
             * Строки вида:
             *
             * @"C:\Users\Deks"
             * $@"Привет, {name}"
             * @$"Привет, {name}"
             *
             * В verbatim-строках двойная кавычка записывается как "".
             */
            fun appendVerbatimString(prefixLength: Int) {
                appendStyled(
                    text = code.substring(i, i + prefixLength),
                    style = stringStyle
                )

                i += prefixLength

                while (i < code.length) {
                    when {
                        code.startsWith("\"\"", i) -> {
                            appendStyled(
                                text = "\"\"",
                                style = escapeStyle
                            )

                            i += 2
                        }

                        code[i] == '"' -> {
                            appendStyled(
                                text = "\"",
                                style = stringStyle
                            )

                            i++
                            break
                        }

                        else -> {
                            appendStyled(
                                text = code[i].toString(),
                                style = stringStyle
                            )

                            i++
                        }
                    }
                }
            }

            fun appendNumber() {
                val start = i
                i++

                while (
                    i < code.length &&
                    (
                            code[i].isLetterOrDigit() ||
                                    code[i] == '.' ||
                                    code[i] == '_' ||
                                    code[i] == '+' ||
                                    code[i] == '-'
                            )
                ) {
                    /*
                     * + и - включаются только после экспоненты:
                     * 1e+10
                     * 1.5E-3
                     */
                    if (
                        (code[i] == '+' || code[i] == '-') &&
                        code.getOrNull(i - 1)?.lowercaseChar() != 'e'
                    ) {
                        break
                    }

                    i++
                }

                appendStyled(
                    text = code.substring(start, i),
                    style = numberStyle
                )
            }

            while (i < code.length) {
                when {
                    // Однострочный комментарий
                    code.startsWith("//", i) -> {
                        val end = code.indexOf(
                            char = '\n',
                            startIndex = i
                        ).let {
                            if (it == -1) code.length else it
                        }

                        appendStyled(
                            text = code.substring(i, end),
                            style = commentStyle
                        )

                        i = end
                    }

                    // Многострочный комментарий
                    code.startsWith("/*", i) -> {
                        val end = code.indexOf(
                            string = "*/",
                            startIndex = i + 2
                        ).let {
                            if (it == -1) code.length else it + 2
                        }

                        appendStyled(
                            text = code.substring(i, end),
                            style = commentStyle
                        )

                        i = end
                    }

                    // Интерполированная verbatim-строка: $@"..."
                    code.startsWith("\$@\"", i) -> {
                        appendVerbatimString(prefixLength = 3)
                    }

                    // Интерполированная verbatim-строка: @$"..."
                    code.startsWith("@\$\"", i) -> {
                        appendVerbatimString(prefixLength = 3)
                    }

                    // Обычная verbatim-строка: @"..."
                    code.startsWith("@\"", i) -> {
                        appendVerbatimString(prefixLength = 2)
                    }

                    // Интерполированная обычная строка: $"..."
                    code.startsWith("\$\"", i) -> {
                        appendStyled(
                            text = "$",
                            style = stringStyle
                        )

                        i++
                        appendNormalStringOrChar('"')
                    }

                    // Обычная строка
                    code[i] == '"' -> {
                        appendNormalStringOrChar('"')
                    }

                    // Символьный литерал
                    code[i] == '\'' -> {
                        appendNormalStringOrChar('\'')
                    }

                    // Escape-последовательность вне строки
                    code[i] == '\\' -> {
                        appendEscapeSequence()
                    }

                    // Директивы препроцессора:
                    // #if, #define, #region, #nullable и т. д.
                    code[i] == '#' -> {
                        val end = code.indexOf(
                            char = '\n',
                            startIndex = i
                        ).let {
                            if (it == -1) code.length else it
                        }

                        appendStyled(
                            text = code.substring(i, end),
                            style = preprocessorStyle
                        )

                        i = end
                    }

                    /*
                     * Атрибуты C#:
                     *
                     * [Obsolete]
                     * [Serializable]
                     * [Route("api/users")]
                     */
                    code[i] == '[' -> {
                        val end = code.indexOf(
                            char = ']',
                            startIndex = i + 1
                        )

                        if (end != -1) {
                            appendStyled(
                                text = code.substring(i, end + 1),
                                style = attributeStyle
                            )

                            i = end + 1
                        } else {
                            appendStyled(
                                text = code[i].toString(),
                                style = defaultStyle
                            )

                            i++
                        }
                    }

                    // Числа: 10, 3.14f, 0xFF, 0b1010, 100_000
                    code[i].isDigit() -> {
                        appendNumber()
                    }

                    // Идентификаторы с префиксом @: @class, @event
                    code[i] == '@' &&
                            (
                                    code.getOrNull(i + 1)?.isLetter() == true ||
                                            code.getOrNull(i + 1) == '_'
                                    ) -> {
                        val start = i
                        i += 2

                        while (
                            i < code.length &&
                            (
                                    code[i].isLetterOrDigit() ||
                                            code[i] == '_'
                                    )
                        ) {
                            i++
                        }

                        appendStyled(
                            text = code.substring(start, i),
                            style = defaultStyle
                        )
                    }

                    // Ключевые слова, классы, методы и переменные
                    code[i].isLetter() || code[i] == '_' -> {
                        val start = i
                        i++

                        while (
                            i < code.length &&
                            (
                                    code[i].isLetterOrDigit() ||
                                            code[i] == '_'
                                    )
                        ) {
                            i++
                        }

                        val word = code.substring(start, i)

                        when {
                            word in keywords -> {
                                appendStyled(word, keywordStyle)
                            }

                            word in commonClasses -> {
                                appendStyled(word, classStyle)
                            }

                            word in commonMethods -> {
                                appendStyled(word, methodStyle)
                            }

                            // Интерфейсы C# обычно начинаются на I
                            word.length > 1 &&
                                    word[0] == 'I' &&
                                    word[1].isUpperCase() -> {
                                appendStyled(word, classStyle)
                            }

                            else -> {
                                appendStyled(word, defaultStyle)
                            }
                        }
                    }

                    else -> {
                        appendStyled(
                            text = code[i].toString(),
                            style = defaultStyle
                        )

                        i++
                    }
                }
            }
        }
    }

    fun highlightGoCode(code: String): AnnotatedString {
        val keywords = setOf(
            "break", "default", "func", "interface", "select",
            "case", "defer", "go", "map", "struct",
            "chan", "else", "goto", "package", "switch",
            "const", "fallthrough", "if", "range", "type",
            "continue", "for", "import", "return", "var"
        )

        /*
         * В Go это не ключевые слова, а встроенные типы.
         */
        val builtInTypes = setOf(
            "any", "bool", "byte", "comparable",
            "complex64", "complex128",
            "error",
            "float32", "float64",
            "int", "int8", "int16", "int32", "int64",
            "rune", "string",
            "uint", "uint8", "uint16", "uint32", "uint64",
            "uintptr"
        )

        val builtInConstants = setOf(
            "true", "false", "nil", "iota"
        )

        val builtInFunctions = setOf(
            "append", "cap", "clear", "close",
            "complex", "copy", "delete", "imag",
            "len", "make", "max", "min", "new",
            "panic", "print", "println",
            "real", "recover"
        )

        val commonPackages = setOf(
            "fmt", "strings", "strconv", "math",
            "time", "os", "io", "bufio",
            "sync", "context", "errors",
            "net", "http", "json",
            "sort", "slices", "maps"
        )

        val commonTypes = setOf(
            "Context", "Reader", "Writer",
            "Scanner", "Buffer",
            "WaitGroup", "Mutex", "RWMutex",
            "Time", "Duration",
            "File", "Client", "Server",
            "Request", "Response"
        )

        val keywordStyle = SpanStyle(
            color = Color(0xFFCC7832), // func, package, return, if, for
            fontWeight = FontWeight.Bold
        )

        val typeStyle = SpanStyle(
            color = Color(0xFF4EC9B0) // string, int, bool, error, Context
        )

        val packageStyle = SpanStyle(
            color = Color(0xFF9876AA) // fmt, strings, time, context
        )

        val functionStyle = SpanStyle(
            color = Color(0xFFFFC66D) // Println, append, len, make
        )

        val stringStyle = SpanStyle(
            color = Color(0xFF6A8759) // "Hello", `raw string`
        )

        val escapeStyle = SpanStyle(
            color = Color(0xFFE8BF6A), // \n, \t, \u1234
            fontWeight = FontWeight.Medium
        )

        val numberStyle = SpanStyle(
            color = Color(0xFF6897BB) // 10, 3.14, 0xFF, 0b1010
        )

        val commentStyle = SpanStyle(
            color = Color(0xFF808080), // // comment, /* comment */
            fontStyle = FontStyle.Italic
        )

        val constantStyle = SpanStyle(
            color = Color(0xFFCC7832), // true, false, nil, iota
            fontWeight = FontWeight.Bold
        )

        val defaultStyle = SpanStyle(
            color = Color(0xFFA9B7C6) // переменные, операторы, скобки
        )

        return buildAnnotatedString {
            var i = 0

            fun appendStyled(
                text: String,
                style: SpanStyle
            ) {
                withStyle(style) {
                    append(text)
                }
            }

            fun isHexDigit(char: Char): Boolean {
                return char.isDigit() || char.lowercaseChar() in 'a'..'f'
            }

            /*
             * Escape-последовательности:
             *
             * \n
             * \t
             * \\
             * \"
             * \xFF
             * \u00FF
             * \U000000FF
             * \123
             */
            fun appendEscapeSequence() {
                val start = i

                i++

                if (i < code.length) {
                    when (code[i]) {
                        'x' -> {
                            i++

                            repeat(2) {
                                if (
                                    i < code.length &&
                                    isHexDigit(code[i])
                                ) {
                                    i++
                                }
                            }
                        }

                        'u' -> {
                            i++

                            repeat(4) {
                                if (
                                    i < code.length &&
                                    isHexDigit(code[i])
                                ) {
                                    i++
                                }
                            }
                        }

                        'U' -> {
                            i++

                            repeat(8) {
                                if (
                                    i < code.length &&
                                    isHexDigit(code[i])
                                ) {
                                    i++
                                }
                            }
                        }

                        in '0'..'7' -> {
                            var octalDigits = 0

                            while (
                                i < code.length &&
                                code[i] in '0'..'7' &&
                                octalDigits < 3
                            ) {
                                i++
                                octalDigits++
                            }
                        }

                        else -> {
                            i++
                        }
                    }
                }

                appendStyled(
                    text = code.substring(start, i),
                    style = escapeStyle
                )
            }

            /*
             * Обычная строка:
             *
             * "Hello\nWorld"
             *
             * Или rune-литерал:
             *
             * 'A'
             * '\n'
             */
            fun appendInterpretedLiteral(quote: Char) {
                appendStyled(
                    text = quote.toString(),
                    style = stringStyle
                )

                i++

                while (i < code.length) {
                    when {
                        code[i] == '\\' -> {
                            appendEscapeSequence()
                        }

                        code[i] == quote -> {
                            appendStyled(
                                text = quote.toString(),
                                style = stringStyle
                            )

                            i++
                            break
                        }

                        else -> {
                            appendStyled(
                                text = code[i].toString(),
                                style = stringStyle
                            )

                            i++
                        }
                    }
                }
            }

            /*
             * Raw-строка Go:
             *
             * `C:\Users\Deks`
             *
             * Внутри неё escape-последовательности не обрабатываются.
             */
            fun appendRawString() {
                appendStyled(
                    text = "`",
                    style = stringStyle
                )

                i++

                while (i < code.length) {
                    if (code[i] == '`') {
                        appendStyled(
                            text = "`",
                            style = stringStyle
                        )

                        i++
                        break
                    }

                    appendStyled(
                        text = code[i].toString(),
                        style = stringStyle
                    )

                    i++
                }
            }

            /*
             * Числа Go:
             *
             * 10
             * 1_000_000
             * 3.14
             * 1.5e10
             * 0xFF
             * 0b1010
             * 0o755
             * 2.5i
             */
            fun appendNumber() {
                val start = i

                i++

                while (i < code.length) {
                    val current = code[i]
                    val previous = code.getOrNull(i - 1)

                    when {
                        current.isLetterOrDigit() -> {
                            i++
                        }

                        current == '_' || current == '.' -> {
                            i++
                        }

                        (
                                current == '+' ||
                                        current == '-'
                                ) &&
                                (
                                        previous == 'e' ||
                                                previous == 'E' ||
                                                previous == 'p' ||
                                                previous == 'P'
                                        ) -> {
                            i++
                        }

                        else -> {
                            break
                        }
                    }
                }

                appendStyled(
                    text = code.substring(start, i),
                    style = numberStyle
                )
            }

            fun nextNonWhitespaceIndex(startIndex: Int): Int {
                var index = startIndex

                while (
                    index < code.length &&
                    code[index].isWhitespace()
                ) {
                    index++
                }

                return index
            }

            while (i < code.length) {
                when {
                    // Однострочный комментарий
                    code.startsWith("//", i) -> {
                        val end = code.indexOf(
                            char = '\n',
                            startIndex = i
                        ).let {
                            if (it == -1) code.length else it
                        }

                        appendStyled(
                            text = code.substring(i, end),
                            style = commentStyle
                        )

                        i = end
                    }

                    // Многострочный комментарий
                    code.startsWith("/*", i) -> {
                        val end = code.indexOf(
                            string = "*/",
                            startIndex = i + 2
                        ).let {
                            if (it == -1) code.length else it + 2
                        }

                        appendStyled(
                            text = code.substring(i, end),
                            style = commentStyle
                        )

                        i = end
                    }

                    // Обычная строка
                    code[i] == '"' -> {
                        appendInterpretedLiteral('"')
                    }

                    // Rune-литерал
                    code[i] == '\'' -> {
                        appendInterpretedLiteral('\'')
                    }

                    // Raw-строка
                    code[i] == '`' -> {
                        appendRawString()
                    }

                    /*
                     * Нужно для отображения \n, \t и других escape-последовательностей
                     * вне строк в учебном тексте.
                     */
                    code[i] == '\\' -> {
                        appendEscapeSequence()
                    }

                    code[i].isDigit() -> {
                        appendNumber()
                    }

                    code[i].isLetter() || code[i] == '_' -> {
                        val start = i

                        i++

                        while (
                            i < code.length &&
                            (
                                    code[i].isLetterOrDigit() ||
                                            code[i] == '_'
                                    )
                        ) {
                            i++
                        }

                        val word = code.substring(start, i)

                        val nextIndex = nextNonWhitespaceIndex(i)

                        val isFunction = nextIndex < code.length &&
                                code[nextIndex] == '('

                        when {
                            word in keywords -> {
                                appendStyled(
                                    text = word,
                                    style = keywordStyle
                                )
                            }

                            word in builtInConstants -> {
                                appendStyled(
                                    text = word,
                                    style = constantStyle
                                )
                            }

                            word in builtInTypes -> {
                                appendStyled(
                                    text = word,
                                    style = typeStyle
                                )
                            }

                            word in commonTypes -> {
                                appendStyled(
                                    text = word,
                                    style = typeStyle
                                )
                            }

                            word in commonPackages -> {
                                appendStyled(
                                    text = word,
                                    style = packageStyle
                                )
                            }

                            word in builtInFunctions || isFunction -> {
                                appendStyled(
                                    text = word,
                                    style = functionStyle
                                )
                            }

                            else -> {
                                appendStyled(
                                    text = word,
                                    style = defaultStyle
                                )
                            }
                        }
                    }

                    else -> {
                        appendStyled(
                            text = code[i].toString(),
                            style = defaultStyle
                        )

                        i++
                    }
                }
            }
        }
    }
}