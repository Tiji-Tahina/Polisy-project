package com.example.polisy_project.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_queue")
data class SyncQueueItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val entityType: String,
    val operation: String,
    val payload: String,
    val createdAt: Long = System.currentTimeMillis()
)
