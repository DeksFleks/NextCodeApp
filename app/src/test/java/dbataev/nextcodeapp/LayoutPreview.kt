package dbataev.nextcodeapp

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import java.io.File

/** Synchronous software rendering avoids PixelCopy's asynchronous window callback in Robolectric. */
fun ComposeContentTestRule.saveLayoutPreview(name: String) {
    runOnIdle {
        val activity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED).single()
        val view = activity.window.decorView
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        view.draw(Canvas(bitmap))
        val directory = File("build/reports/task-layouts").apply { mkdirs() }
        File(directory,"$name.png").outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG,100,it) }
        bitmap.recycle()
    }
}
