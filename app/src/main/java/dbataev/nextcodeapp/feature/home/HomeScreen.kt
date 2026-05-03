package dbataev.nextcodeapp.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dbataev.nextcodeapp.core.data.remote.RetrofitClient
import dbataev.nextcodeapp.core.data.remote.dto.QuoteDto
import dbataev.nextcodeapp.core.designsystem.component.NcBackgroundText
import dbataev.nextcodeapp.core.designsystem.component.NextCodeQuoteDropdown
import dbataev.nextcodeapp.core.designsystem.theme.NcBackgroundColor
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dbataev.nextcodeapp.core.designsystem.component.NextCodeModules
import dbataev.nextcodeapp.core.designsystem.component.NextCodeModulesTitles
import dbataev.nextcodeapp.feature.module.ModuleViewModel
import kotlinx.coroutines.launch
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    courseId: Long,
    moduleViewModel: ModuleViewModel = viewModel(),
    navController: NavController
) {
    var quote: QuoteDto? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()

    suspend fun loadQuote() {
        try {
            quote = RetrofitClient.quoteApi.getRandomQuote()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    LaunchedEffect(Unit) {
        loadQuote()
    }

    LaunchedEffect(courseId) {
        moduleViewModel.loadModules(courseId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NcBackgroundColor)
    ) {
        NcBackgroundText()

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            NextCodeQuoteDropdown(
                quote = quote?.text ?: "Цитата компилируется...",
                author = quote?.author ?: "Ленивый разработчик",
                onCollapsed = {
                    scope.launch {
                        loadQuote()
                    }
                }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(moduleViewModel.modules) { module ->
                    NextCodeModules(
                        moduleOrderIndex = module.orderIndex,
                        title = module.title,
                        lessons = module.lessons,
                        onLessonClick = { lesson ->
                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("lessonId", lesson.id)

                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("theoryText", lesson.theoryText)

                            navController.navigate("theory")
                        }
                    )
                }
            }
        }
    }
}

