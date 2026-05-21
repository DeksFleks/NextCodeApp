package dbataev.nextcodeapp.feature.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dbataev.nextcodeapp.R
import dbataev.nextcodeapp.core.common.viewModel.UserViewModel
import dbataev.nextcodeapp.core.designsystem.component.NcBackgroundText
import dbataev.nextcodeapp.core.designsystem.component.NextCodeSettingButton
import dbataev.nextcodeapp.core.designsystem.component.NextCodeStatisticsProfileCard
import dbataev.nextcodeapp.core.designsystem.theme.DefaultAppTextStyles
import dbataev.nextcodeapp.core.designsystem.theme.NcAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcBackgroundColor

@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel()
) {
    val user by userViewModel.user.collectAsState()

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_image_profile),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            NextCodeSettingButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp),
                onClick = {
                    navController.navigate("settings")
                }
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                NcBackgroundColor.copy(alpha = 0.10f), // сверху
                                NcBackgroundColor.copy(alpha = 1f)     // снизу
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = user?.nickname ?: "Error: 404",
                    color = NcAccentColor,
                    style = DefaultAppTextStyles.bebasBold36
                )

                Text(
                    text = "${user?.level ?: 1} LVL",
                    color = NcAccentColor,
                    style = DefaultAppTextStyles.bebasBold36
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            NextCodeStatisticsProfileCard(
                statistics = user?.xp.toString(),
                text = "XP",
                modifier = Modifier.weight(1f),
            )

            NextCodeStatisticsProfileCard(
                statistics = user?.streak.toString(),
                text = "Streak",
                modifier = Modifier.weight(1f),
            )

            NextCodeStatisticsProfileCard(
                statistics = user?.level.toString(),
                text = "Level",
                modifier = Modifier.weight(1f),
            )
        }
    }
}