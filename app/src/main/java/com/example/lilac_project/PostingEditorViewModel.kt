package com.example.lilac_project

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.lilac_project.data.model.LilacStudy
import com.example.lilac_project.data.source.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext

class PostingEditorViewModel(application: Application) : AndroidViewModel(application) {
    private val db by lazy { AppDatabase.getInstance(application) }

    val category = MutableStateFlow<String?>(null)
    val maxPeopleCount = MutableStateFlow(3)
    val name = MutableStateFlow<String?>(null)
    val anonymity = MutableStateFlow(false)
    val title = MutableStateFlow<String?>(null)
    val content = MutableStateFlow<String?>(null)

    val isValidated = combine(listOf(category, maxPeopleCount, name, anonymity, title, content)) {
        return@combine category.value?.isNotBlank() == true &&
                name.value?.isNotBlank() == true &&
                title.value?.isNotBlank() == true &&
                content.value?.isNotBlank() == true
    }

    suspend fun submit() = withContext(Dispatchers.IO) {
        db.studyDao().insert(
            LilacStudy(
                0,
                category.value!!,
                name.value!!.trim(),
                0,
                maxPeopleCount.value,
                anonymity.value,
                title.value!!.trim(),
                content.value!!.trim(),
                0
            )
        )
    }
}