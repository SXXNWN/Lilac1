package com.example.lilac_project

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.lilac_project.data.model.LilacReply
import com.example.lilac_project.data.model.LilacStudy
import com.example.lilac_project.data.source.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostingViewerViewModel(application: Application) : AndroidViewModel(application) {
    private val db by lazy { AppDatabase.getInstance(application) }

    fun getStudy(id: Long) = db.studyDao().study(id)

    fun getReplies(id: Long) = db.replyDao().replies(id)

    suspend fun update(study: LilacStudy) = withContext(Dispatchers.IO) {
        db.studyDao().update(study)
    }

    suspend fun reply(id: Long, name: String, content: String) = withContext(Dispatchers.IO) {
        db.replyDao().insert(LilacReply(0, id, name, false, content))
    }
}