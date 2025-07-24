package ru.ansmos.dombuketa.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.ansmos.dombuketa.models_bll.Product

class ReminderSeeLater : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = intent?.getBundleExtra(PRODUCT_BUNDLE)
        val product : Product = bundle?.get(PRODUCT) as Product
        NotificationHelper.createNotification(context!!, product)
    }

    companion object{
        const val PRODUCT = "PRODUCT"
        const val PRODUCT_BUNDLE = "PRODUCT_BUNDLE"
    }
}
