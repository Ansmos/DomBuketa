package ru.ansmos.dombuketa.views.rw

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.ansmos.dombuketa.databinding.ItemNotificationBinding
import ru.ansmos.dombuketa.models_bll.Notification
import ru.ansmos.dombuketa.net_module.ApiConstants
import java.time.format.DateTimeFormatter

//В конструктор класс передается layout, который мы создали(film_item.xml)
class Notification_ViewHolder(var binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
    //В этом методе кладем данные из Film в наши View
    fun bund(notification: Notification){
        binding.title.text = notification.name
        //poster.setImageResource(film.poster) Оставил на память
        Glide.with(itemView)
            //Загружаем сам ресурс
            .load(ApiConstants.IMAGES_URL + notification.image)
            //Центруем изображение
            .centerCrop()
            //Указываем ImageView, куда будем загружать изображение
            .into(binding.poster)
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy в HH:mm")
        binding.notification.text = notification.notificationTime.format(formatter)
    }
}