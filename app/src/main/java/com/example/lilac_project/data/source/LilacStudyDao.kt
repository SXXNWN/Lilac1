package com.example.lilac_project.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.lilac_project.data.model.LilacStudy
import kotlinx.coroutines.flow.Flow

@Dao
interface LilacStudyDao {
    @Query("SELECT * FROM lilacstudy WHERE title LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun studies(query: String): Flow<List<LilacStudy>>

    @Query("SELECT * FROM lilacstudy WHERE category = :category AND title LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun studies(category: String, query: String): Flow<List<LilacStudy>>

    @Query("SELECT * FROM lilacstudy WHERE id = :id")
    fun study(id: Long): Flow<LilacStudy?>

    @Insert
    fun insert(study: LilacStudy)

    @Update
    fun update(study: LilacStudy)
}