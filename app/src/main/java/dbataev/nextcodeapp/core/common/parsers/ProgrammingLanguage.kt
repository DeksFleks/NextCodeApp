package dbataev.nextcodeapp.core.common.parsers

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
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
}