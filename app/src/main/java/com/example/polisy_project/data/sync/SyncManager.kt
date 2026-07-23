package com.example.polisy_project.data.sync

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.polisy_project.data.api.RetrofitInstance
import com.example.polisy_project.data.local.AppDatabase
import com.example.polisy_project.data.model.Infraction
import com.google.gson.Gson

class SyncManager(private val context: Context, private val db: AppDatabase) {

    private val gson = Gson()

    suspend fun syncPendingItems() {
        if (!isOnline()) return

        val items = db.syncQueueDao().getAll()
        for (item in items) {
            try {
                when (item.entityType) {
                    "infraction" -> {
                        val infraction = gson.fromJson(item.payload, Infraction::class.java)
                        RetrofitInstance.api.insertInfraction(infraction)
                    }
                }
                db.syncQueueDao().deleteById(item.id)
            } catch (_: Exception) {
                break
            }
        }
    }

    private fun isOnline(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
