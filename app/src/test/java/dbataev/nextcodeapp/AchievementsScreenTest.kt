package dbataev.nextcodeapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import dbataev.nextcodeapp.core.common.*
import dbataev.nextcodeapp.core.common.viewModel.UserViewModel
import dbataev.nextcodeapp.core.data.local.*
import dbataev.nextcodeapp.core.data.remote.dto.*
import dbataev.nextcodeapp.feature.achievements.*
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.*

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = "w400dp-h800dp-mdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@LooperMode(LooperMode.Mode.PAUSED)
class AchievementsScreenTest {
    @get:Rule val compose=createComposeRule()
    @Test fun achievementsUseSharedProfileAndReactToCompletedLessonRefresh() {
        val store=object:SessionTokenStore {
            override fun getToken()="test-token"
            override fun saveToken(token:String){}
            override fun clearToken(){}
        }
        val session=SessionManager(store)
        var completed=0L
        val shared=UserViewModel({ UserDto(id=1,username="dev",nickname="Deks",xp=10,level=2,streak=1,
            xpForNextLevel=150,currentCourseId=42,totalXp=110,bestStreak=1,completedLessons=completed) },{session})
        runBlocking { shared.refreshUser() }
        val achievements=AchievementViewModel { listOf(
            AchievementDto("Один урок","Пройти один урок",AchievementType.LESSONS_COMPLETED,1,LevelAchievementType.BRONZE),
            AchievementDto("Два урока","Пройти два урока",AchievementType.LESSONS_COMPLETED,2,LevelAchievementType.SILVER),
            AchievementDto("Три урока","Пройти три урока",AchievementType.LESSONS_COMPLETED,3,LevelAchievementType.AMETHYST)) }
        compose.setContent { AchievementsScreen(viewModel=achievements,userViewModel=shared) }
        compose.waitUntil { compose.onAllNodesWithText("0%").fetchSemanticsNodes().size==3 }
        compose.runOnIdle { completed=3; shared.loadUser() }
        compose.waitUntil { compose.onAllNodesWithText("100%").fetchSemanticsNodes().size==3 }
        compose.onAllNodesWithText("100%").assertCountEquals(3)
        compose.saveLayoutPreview("achievements-completed")
    }
}
