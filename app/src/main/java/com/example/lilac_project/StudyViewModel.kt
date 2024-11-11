package com.example.lilac_project

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.lilac_project.data.source.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow

class StudyViewModel(application: Application) : AndroidViewModel(application) {
    val db by lazy { AppDatabase.getInstance(application) }

    val query = MutableStateFlow("")
}