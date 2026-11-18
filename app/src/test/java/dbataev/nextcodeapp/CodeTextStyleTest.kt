package dbataev.nextcodeapp

import androidx.compose.ui.text.AnnotatedString
import dbataev.nextcodeapp.core.common.parsers.*
import org.junit.Assert.*
import org.junit.Test

class CodeTextStyleTest {
    @Test fun highlightingPreservesSourceAndEveryCursorOffset() {
        val snippets = listOf("int age = 18;", "System.out.println(\"Hello\\n\");", "// comment\nString name = \"Deks\";", "", "int age = ;")
        for (source in snippets.flatMap { full -> (0..full.length).map { full.take(it) } }) {
            val result = TaskCodeVisualTransformation("java").filter(AnnotatedString(source))
            assertEquals(source,result.text.text)
            for (offset in 0..source.length) {
                assertEquals(offset,result.offsetMapping.originalToTransformed(offset))
                assertEquals(offset,result.offsetMapping.transformedToOriginal(offset))
            }
        }
    }
    @Test fun javaUsesExistingLessonHighlighterAndUnknownLanguagesPreserveText() {
        val source = "int age = 18;"
        assertEquals(ProgrammingLanguage.highlightJavaCode(source),highlightTaskCode(source,"java"))
        assertEquals(AnnotatedString(source),highlightTaskCode(source,"unknown"))
    }
}
