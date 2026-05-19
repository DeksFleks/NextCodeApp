package dbataev.nextcodeapp.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map

class ContextRepository(private val context: Context) {

    val courseIdFlow = context.dataStore.data
        .map { it[DataStoreKeys.CURRENT_COURSE_ID] ?: -1 }

    suspend fun saveCourseId(id: Int) {
        context.dataStore.edit {
            it[DataStoreKeys.CURRENT_COURSE_ID] = id
        }
    }
}