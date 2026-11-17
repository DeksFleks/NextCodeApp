package dbataev.nextcodeapp
import dbataev.nextcodeapp.feature.lesson.tasks.TaskAnswerViewModel
import dbataev.nextcodeapp.feature.lesson.tasks.lessonEnd.LessonEndViewModel
import dbataev.nextcodeapp.core.common.TaskType
import dbataev.nextcodeapp.core.data.remote.dto.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
@OptIn(ExperimentalCoroutinesApi::class)
class LessonFlowTest {
    @get:Rule val main = MainDispatcherRule()
    @Test fun failedCompletionCanBeRetriedAndSuccessIsNotSentTwice() = runTest {
        var calls = 0
        val vm = LessonEndViewModel {
            calls++
            if (calls == 1) Response.error(500,"failed".toResponseBody()) else Response.success(emptyList())
        }
        vm.lessonCompleted(8); advanceUntilIdle()
        assertFalse(vm.isCompleted.value); assertNotNull(vm.error.value)
        vm.lessonCompleted(8); advanceUntilIdle()
        assertTrue(vm.isCompleted.value); assertNull(vm.error.value)
        vm.lessonCompleted(8); advanceUntilIdle()
        assertEquals(2,calls)
    }
    @Test fun concurrentCompletionClicksSendOneRequest() = runTest {
        val response = CompletableDeferred<Response<List<AchievementDto>>>()
        var calls = 0
        val vm = LessonEndViewModel { calls++; response.await() }
        vm.lessonCompleted(8); vm.lessonCompleted(8); runCurrent()
        assertTrue(vm.isLoading.value); assertFalse(vm.isCompleted.value); assertEquals(1,calls)
        response.complete(Response.success(emptyList())); advanceUntilIdle()
        assertTrue(vm.isCompleted.value)
    }
    @Test fun invalidLessonNeverSendsCompletion() = runTest {
        var calls = 0
        val vm = LessonEndViewModel { calls++; Response.success(emptyList()) }
        vm.lessonCompleted(-1); advanceUntilIdle()
        assertEquals(0,calls); assertFalse(vm.isCompleted.value); assertNotNull(vm.error.value)
    }
    @Test fun backToPreviouslyPassedTaskCanContinueAgain() = runTest {
        var checks = 0; var navigations = 0
        val vm = TaskAnswerViewModel { _, _ -> checks++; TaskResultDto(true,null,"EXACT_TEXT") }
        val task = TaskDto(2,TaskType.FILL_BLANK,"Question",null)
        vm.input = "println"
        vm.submit(task,8) { navigations++ }; advanceUntilIdle()
        vm.submit(task,8) { navigations++ }; advanceUntilIdle()
        assertEquals(2,checks); assertEquals(2,navigations)
    }
}
