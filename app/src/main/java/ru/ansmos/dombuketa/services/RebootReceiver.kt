package ru.ansmos.dombuketa.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import ru.ansmos.dombuketa.App

class RebootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return
        if (intent.action == Intent.ACTION_BOOT_COMPLETED){
            Log.i("bootReceiver", "Устройство было перезагружено.")
            val dagger = App.instance.dagger
            dagger.getInteractor().getNotifications()
                .subscribe({  list ->
                    list.forEach {
                        if (context != null){
                            dagger.getNotificationHelper().createNotification(context, it.toProduct())
                        }
                    }
                },{
                    Log.e("bootReceiver", "Ошибка загрузки нотификации из БД.")
                })
        }
    }
}