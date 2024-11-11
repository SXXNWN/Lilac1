package com.example.lilac_project.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class LilacStudy(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "current_people_count") val currentPeopleCount: Int,
    @ColumnInfo(name = "max_people_count") val maxPeopleCount: Int,
    @ColumnInfo(name = "anonymity") val anonymity: Boolean,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "views") val views: Int,
    @ColumnInfo(name = "timestamp") val timestamp: Long = Date().time,
)