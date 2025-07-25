package ru.ansmos.dombuketa.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import ru.ansmos.dombuketa.App
import ru.ansmos.dombuketa.R
import ru.ansmos.dombuketa.domain.Interactor
import ru.ansmos.dombuketa.models_bll.Notification
import ru.ansmos.dombuketa.models_bll.Product
import ru.ansmos.dombuketa.net_module.ApiConstants
import ru.ansmos.dombuketa.views.MainActivity
import java.time.LocalDateTime
import java.util.*

object NotificationHelper {
    const val CHANNEL_ID = "DomBuketa_channel"
    const val CHANNEL_TILTLE = "Не забыть подарить."

    val interactor: Interactor = App.instance.dagger.getInteractor()

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel_name = "DoMakePresentChannel"
            val channel_desc = "DomBuketa channel"
            val channel_impt = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, channel_name, channel_impt)
            channel.description = channel_desc
            NotificationManagerCompat.from(context).createNotificationChannel(channel)
        }
    }

    fun createNotification(context: Context, product: Product) {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("product", product)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationManager = NotificationManagerCompat.from(context)
        val notifBuilder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_outline_watch_later_24)
            setContentText(product.name)
            setContentTitle(CHANNEL_TILTLE)
            priority = NotificationCompat.PRIORITY_DEFAULT
            setContentIntent((pendingIntent))
            setAutoCancel(true)
        }
        //Загружаем картинку
        if (product.imageCart != null) {
            Glide.with(context)
                .asBitmap()
                .load(ApiConstants.IMAGES_URL + product.imageCart.path + product.imageCart.fileName)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        //Создаем нотификацию
                        notifBuilder.setStyle(
                            NotificationCompat.BigPictureStyle().bigPicture(resource)
                        )
                        //Обновляем нотификацию
                        notificationManager.notify(product.id, notifBuilder.build())
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
            //Отправляем изначальную нотификацию в стандартном исполнении
            notificationManager.notify(product.id, notifBuilder.build())

        }
    }

    fun notificationSet(context: Context, productOrNotification: Any?) {
        if (productOrNotification == null) return
        val calendar = Calendar.getInstance()
        val curY = calendar.get(Calendar.YEAR)
        val curM = calendar.get(Calendar.MONTH)
        val curD = calendar.get(Calendar.DAY_OF_MONTH)
        val curH = calendar.get(Calendar.HOUR_OF_DAY)
        val curm = calendar.get(Calendar.MINUTE)
        // Оставлю для примера when с объектом
        // Вся эта тема связана с тем, что когда нет сети, мы не можем получить фильм, а в БД его может уже не быть
        // поэтому берем все его данные из нотификации, чтобы напоминание про него все равно пришло
        // Енсли прислали вообще другой объект, то выходим из процедуры, ничего не делая.
        var notification: Notification? = null
        when (productOrNotification) {
            is Product -> {
                notification = ru.ansmos.dombuketa.models_bll.Notification(
                    id =  0,
                    productId = productOrNotification.id,
                    name = productOrNotification.name,
                    image = (productOrNotification.imageCart?.path ?: "") +
                            (productOrNotification.imageCart?.fileName ?: ""),
                    description = productOrNotification.description,
                    notificationTime = LocalDateTime.of(curY, curM, curD, curH, curm),
                    isActive = true
                )
            }
            is Notification -> notification = productOrNotification
            else -> null
        }
        if (notification == null) return

        DatePickerDialog(context,{
                _, dpdYear, dpdMonth, dayOfMonth ->
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, pickerMinute ->
                val pickerDateTime = Calendar.getInstance()
                pickerDateTime.set(dpdYear, dpdMonth, dayOfMonth, hourOfDay, pickerMinute, 0)
                val dateTimeInMillis = pickerDateTime.timeInMillis
                //interactor.insertNotification(notification)
                val newTime = LocalDateTime.ofInstant(pickerDateTime.toInstant(), pickerDateTime.timeZone.toZoneId())
                notification.notificationTime = newTime
                interactor.updateNotification(notification)
                createWatchLaterEvent(context, dateTimeInMillis, notification.toProduct())
            }
            TimePickerDialog(context, timeSetListener, curH, curm, true).show()
        }, curY, curM, curD).show()

    }

    private fun createWatchLaterEvent(context: Context, dateTimeInMillis: Long, product: Product) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(product.name, null, context, ReminderSeeLater():: class.java)
        val bundle = Bundle()
        bundle.putParcelable(ReminderSeeLater.PRODUCT, product)
        intent.putExtra(ReminderSeeLater.PRODUCT_BUNDLE, bundle)

        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        // Устанавливаем напоминалку
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, dateTimeInMillis, pendingIntent)
    }
}
