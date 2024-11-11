package com.example.lilac_project.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class LilacReply(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "study_id") val studyId: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "anonymity") val anonymity: Boolean,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long = Date().time,
)