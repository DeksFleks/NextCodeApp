package dbataev.nextcodeapp.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import dbataev.nextcodeapp.R

@Composable
fun NcBackgroundText(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.ic_background_text),
        contentDescription = null,
        modifier = modifier
            .fillMaxSize()
            .alpha(0.50f),
        contentScale = ContentScale.Crop
    )
}

