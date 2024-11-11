package com.example.lilac_project.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lilac_project.data.model.LilacReply
import com.example.lilac_project.data.model.LilacStudy

@Database(entities = [LilacStudy::class, LilacReply::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studyDao(): LilacStudyDao
    abstract fun replyDao(): LilacReplyDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "lilac-db"
                ).fallbackToDestructiveMigration()
                    .build()
            }

            return instance!!
        }
    }
}