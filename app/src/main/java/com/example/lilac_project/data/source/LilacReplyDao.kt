package com.example.lilac_project.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lilac_project.data.model.LilacReply
import com.example.lilac_project.data.model.LilacStudy
import kotlinx.coroutines.flow.Flow

@Dao
interface LilacReplyDao {
    @Query("SELECT * FROM lilacreply WHERE study_id = :id ORDER BY timestamp DESC")
    fun replies(id: Long): Flow<List<LilacReply>>

    @Insert
    fun insert(reply: LilacReply)
}