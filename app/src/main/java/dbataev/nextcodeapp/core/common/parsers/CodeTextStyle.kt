package dbataev.nextcodeapp.core.common.parsers

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/** Reuse the same token colours as the existing lesson code blocks. */
fun highlightTaskCode(source: String, language: String = "java"): AnnotatedString {
    val result = when (language.trim().lowercase()) {
        "java" -> ProgrammingLanguage.highlightJavaCode(source)
        "python", "py" -> ProgrammingLanguage.highlightPythonCode(source)
        "c#" -> ProgrammingLanguage.highlightCSharpCode(source)
        "go", "golang" -> ProgrammingLanguage.highlightGoCode(source)
        else -> AnnotatedString(source)
    }
    // Colouring must never change the text or invalidate selection/cursor offsets.
    return result.takeIf { it.text == source } ?: AnnotatedString(source)
}

class TaskCodeVisualTransformation(private val language: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText =
        TransformedText(highlightTaskCode(text.text, language), OffsetMapping.Identity)
}
