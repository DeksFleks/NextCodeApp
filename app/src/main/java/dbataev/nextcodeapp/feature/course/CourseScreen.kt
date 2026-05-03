package dbataev.nextcodeapp.feature.course

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dbataev.nextcodeapp.core.data.remote.dto.CourseDto
import dbataev.nextcodeapp.core.designsystem.component.NcNullBottomBar
import dbataev.nextcodeapp.core.designsystem.component.NcNullTopBar
import dbataev.nextcodeapp.core.designsystem.component.NextCodeGrayButton
import dbataev.nextcodeapp.core.designsystem.theme.DefaultAppTextStyles
import dbataev.nextcodeapp.core.designsystem.theme.NcAccentColor
import dbataev.nextcodeapp.core.model.Course

@Composable
fun CourseScreen(
    viewModel: CourseViewModel = viewModel(),
    onCourseClick: (Long) -> Unit
) {
    val courses = viewModel.courses

    LaunchedEffect(Unit) {
        viewModel.loadCourses()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box {
            NcNullTopBar(modifier = Modifier)

            Box(
                modifier = Modifier.padding(25.dp)
            ) {
                Text(
                    text = "Выбери курс:",
                    style = DefaultAppTextStyles.bebasBookCourse,
                    color = NcAccentColor
                )
            }
        }

        CourseList(
            courses = courses,
            onCourseClick = { course ->
                if (course.isActive) {
                    onCourseClick(course.id)
                }
            },
            modifier = Modifier.weight(1f)
        )

        NcNullBottomBar()
    }
}

@Composable
fun CourseList(
    courses: List<CourseDto>,
    onCourseClick: (CourseDto) -> Unit,
    modifier: Modifier
) {
    val sortedCourses = courses.sortedByDescending { it.isActive }

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(sortedCourses) { course ->
            NextCodeGrayButton(
                text = course.title,
                isActive = course.isActive,
                onClick = { onCourseClick(course) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


