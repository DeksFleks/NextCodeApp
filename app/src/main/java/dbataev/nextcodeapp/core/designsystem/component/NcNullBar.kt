package dbataev.nextcodeapp.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dbataev.nextcodeapp.core.designsystem.theme.NcMainColor
import dbataev.nextcodeapp.core.designsystem.theme.NcSecondAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcSecondColor

@Composable
fun NcNullBottomBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(NcMainColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
        )
    }
}

@Composable
fun NcNullTopBar(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        bottomStart = 12.dp,
                        bottomEnd = 12.dp
                    )
                )
                .background(NcMainColor)
        ) {
            Spacer(
                modifier = modifier
                    .fillMaxWidth()
                    .windowInsetsTopHeight(WindowInsets.statusBars)
            )

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(40.dp)
            )
        }
    }
}