package dbataev.nextcodeapp.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dbataev.nextcodeapp.core.data.remote.dto.LessonDto

@Composable
fun NextCodeModules(
    moduleOrderIndex: Int,
    title: String,
    lessons: List<LessonDto>,
    onLessonClick: (LessonDto) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        NextCodeModulesTitles(
            title = title,
            orderIndex = moduleOrderIndex
        )

        lessons.forEachIndexed { index, lesson ->
            val xOffset = if (index % 2 == 0) (-65).dp else 65.dp

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CreateButton(
                    lesson = lesson,
                    modifier = Modifier.offset(x = xOffset),
                    onClick = {
                        onLessonClick(lesson)
                    }
                )
            }
        }
    }
}

@Composable
fun CreateButton(
    lesson: LessonDto,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        NextCodeLessonButton(
            modifier = Modifier,
            lessonId = lesson.id,
            state = lesson.state,
            onClick = onClick
        )
    }
}