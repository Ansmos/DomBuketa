package ru.ansmos.dombuketa.views.rw

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ru.ansmos.dombuketa.R
import ru.ansmos.dombuketa.databinding.ItemNotificationBinding
import ru.ansmos.dombuketa.models_bll.Notification

//в параметр передаем слушатель, чтобы мы потом могли обрабатывать нажатия из класса Activity
class Notification_Adapter(private val clickListener: IOnItemClicListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var items = mutableListOf<Notification>()      //Здесь у нас хранится список элементов для RV

    //В этом методе мы привязываем наш ViewHolder и передаем туда "надутую" верстку нашего фильма
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return Notification_ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //Проверяем какой у нас ViewHolder
        when (holder){
            is Notification_ViewHolder -> {
                //Вызываем метод bind(), который мы создали, и передаем туда объект
                //из нашей базы данных с указанием позиции
                holder.bund(items[position])
                //Обрабатываем нажатие на весь элемент целиком(можно сделать на отдельный элемент
                //например, картинку) и вызываем метод нашего листенера, который мы получаем из
                //конструктора адаптера
                holder.itemView.findViewById<CardView>(R.id.item_container).setOnClickListener{
                    clickListener.click(items[position], false)
                }
                holder.itemView.findViewById<ImageButton>(R.id.btn_set).setOnClickListener {
                    clickListener.click(items[position], true)
                    Log.i("Notif_Adapter","onClick")
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun getItems() : List<Notification> = items.toList()

    //Метод очистки адаптера
    fun clearItems() = items.clear()

    //Метод для добавления объектов в наш список
    fun addItems(list: List<Notification>){
        //items.clear()
        items.addAll(list)
        notifyDataSetChanged()  //если без DiffUtils
    }

    fun replaceItems(list: List<Notification>){
        items = list.toMutableList()
    }

    fun removeNotification(position: Int) : Int {
        val notification_id = items[position].productId
        items.removeAt(position)
        notifyItemRemoved(position)
        return notification_id //Возвращаем для удаления из БД
    }

    //Интерфейс для обработки кликов
    //actionSet - true Измеить настройку, иначе открыть карточку фильма
    interface IOnItemClicListener{
        fun click(notification: Notification, actionSet: Boolean)
    }

}