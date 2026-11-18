package dbataev.nextcodeapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import dbataev.nextcodeapp.core.common.TaskType
import dbataev.nextcodeapp.core.data.remote.dto.*
import dbataev.nextcodeapp.feature.lesson.tasks.TaskAnswerViewModel
import dbataev.nextcodeapp.feature.lesson.tasks.test.TestScreen
import dbataev.nextcodeapp.feature.lesson.tasks.write.WriteScreen
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.*

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = "w320dp-h600dp-mdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@LooperMode(LooperMode.Mode.PAUSED)
class TaskLayoutTest {
    @get:Rule val compose = createComposeRule()
    @Test fun blankIsInsideCodeRowAndCanBeSubmittedInReducedViewport() {
        var answer: TaskAnswerDto? = null
        val vm = TaskAnswerViewModel { _, value -> answer = value; TaskResultDto(true,null,"EXACT_TEXT") }
        val task = TaskDto(1,TaskType.FILL_BLANK,"Укажи тип переменной.",null,sentenceAfter=" age = 18;")
        compose.setContent {
            CompositionLocalProvider(LocalDensity provides Density(1f,1.3f)) {
                Box(Modifier.requiredSize(320.dp,380.dp)) { WriteScreen(task,8,viewModel=vm,onTaskComplete={}) }
            }
        }
        compose.onNodeWithTag("blank-input").performScrollTo().performTextInput("int")
        compose.saveLayoutPreview("fill-blank-small")
        val input = compose.onNodeWithTag("blank-input").fetchSemanticsNode().boundsInRoot
        val viewport = compose.onNodeWithTag("task-scroll").fetchSemanticsNode().boundsInRoot
        assertTrue("Focused blank must not hide under the submit footer", input.bottom <= viewport.bottom + 1f)
        val suffix = compose.onNodeWithText(" age = 18;").fetchSemanticsNode().boundsInRoot
        assertTrue("The blank and its context must share one code row", input.top < suffix.bottom && suffix.top < input.bottom)
        compose.onNodeWithTag("task-submit").assertIsDisplayed().assertIsEnabled().performClick()
        compose.waitForIdle()
        assertEquals("int",answer?.answer)
        assertEquals(8L,answer?.lessonId)
    }
    @Test fun codingEditorPreservesStarterCodeAndAcceptsEdits() {
        val vm=TaskAnswerViewModel { _, value -> assertEquals("int age = 18;",value.answer); TaskResultDto(true,null,"EXACT_SOURCE") }
        val task=TaskDto(2,TaskType.CODING,"Объяви переменную.",null,starterCode="int age = ;",programmingLanguage="java")
        compose.setContent { Box(Modifier.requiredSize(320.dp,400.dp)) { WriteScreen(task,8,viewModel=vm,onTaskComplete={}) } }
        compose.onNodeWithTag("coding-input").performScrollTo().assertTextEquals("int age = ;")
            .performTextReplacement("int age = 18;")
        compose.saveLayoutPreview("coding-small")
        compose.onNodeWithTag("task-submit").assertIsDisplayed().performClick()
        compose.waitForIdle()
    }
    @Test fun singleChoiceReplacesSelectionAndDoesNotDeselectOnRepeatedTap() {
        val vm=TaskAnswerViewModel { _, _ -> TaskResultDto(true,null,"OPTION_SET") }
        val task=TaskDto(3,TaskType.MULTIPLE_CHOICE,"Выбери ответ.",listOf(TaskOptionDto(1,"Первый"),TaskOptionDto(2,"Второй")))
        compose.setContent { TestScreen(task,8,viewModel=vm,onTaskComplete={}) }
        compose.onNodeWithText("Первый").performClick()
        compose.onNodeWithText("Второй").performClick()
        compose.onNodeWithText("Второй").performClick()
        compose.runOnIdle { assertEquals(setOf(2L),vm.selected) }
    }
    @Test fun multipleChoiceKeepsBothOptionsAndLongAnswersRemainReachable() {
        val vm=TaskAnswerViewModel { _, _ -> TaskResultDto(true,null,"OPTION_SET") }
        val long="String description = \"Длинный вариант ответа должен переноситься без обрезания\";"
        val task=TaskDto(4,TaskType.MULTIPLE_CHOICE,"Выбери оба ответа.",listOf(TaskOptionDto(1,"int age = 18;"),TaskOptionDto(2,long)),multipleSelect=true)
        compose.setContent {
            CompositionLocalProvider(LocalDensity provides Density(1f,1.5f)) {
                Box(Modifier.requiredSize(320.dp,400.dp)) { TestScreen(task,8,viewModel=vm,onTaskComplete={}) }
            }
        }
        compose.onNodeWithText("int age = 18;").performScrollTo().performClick()
        compose.onNodeWithText(long).performScrollTo().assertIsDisplayed().performClick()
        compose.runOnIdle { assertEquals(setOf(1L,2L),vm.selected) }
        compose.saveLayoutPreview("multiple-choice-small")
        compose.onNodeWithTag("task-submit").assertIsDisplayed().assertIsEnabled()
    }
    @Test
    @Config(qualifiers = "w360dp-h800dp-mdpi")
    fun allTaskTypesStayUsableInFullPortraitLayout() {
        val blank=TaskDto(10,TaskType.FILL_BLANK,"Укажи тип целочисленной переменной.",null,sentenceAfter=" age = 18;")
        val coding=TaskDto(11,TaskType.CODING,"Напиши строку в точности: int age = 18;",null,starterCode="int age = ;",programmingLanguage="java")
        val choice=TaskDto(12,TaskType.MULTIPLE_CHOICE,"Выбери ОБА корректных объявления переменных.",
            listOf(TaskOptionDto(1,"int age = 18;"),TaskOptionDto(2,"String name = \"Deks\";"),TaskOptionDto(3,"int 2age = 18;")),multipleSelect=true)
        val current=mutableStateOf(blank)
        compose.setContent {
            val task=current.value
            key(task.id) {
                val vm=remember { TaskAnswerViewModel { _, _ -> TaskResultDto(true,null,"test") } }
                if (task.type==TaskType.MULTIPLE_CHOICE) TestScreen(task,8,viewModel=vm,onBack={},onTaskComplete={})
                else WriteScreen(task,8,viewModel=vm,onBack={},onTaskComplete={})
            }
        }
        compose.onNodeWithTag("blank-input").performTextInput("int")
        compose.onNodeWithTag("task-submit").assertIsDisplayed().assertIsEnabled()
        compose.saveLayoutPreview("fill-blank-portrait")
        compose.runOnIdle { current.value=coding }
        compose.onNodeWithTag("coding-input").assertIsDisplayed().assertTextEquals("int age = ;")
        compose.onNodeWithTag("task-submit").assertIsDisplayed()
        compose.saveLayoutPreview("coding-portrait")
        compose.runOnIdle { current.value=choice }
        compose.onNodeWithText("int age = 18;").performClick()
        compose.onNodeWithText("String name = \"Deks\";").performClick()
        compose.onNodeWithTag("task-submit").assertIsDisplayed().assertIsEnabled()
        compose.saveLayoutPreview("multiple-choice-portrait")
    }

}
