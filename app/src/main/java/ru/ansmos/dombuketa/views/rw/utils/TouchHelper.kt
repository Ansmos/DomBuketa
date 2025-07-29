package ru.ansmos.dombuketa.views.rw.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.ansmos.dombuketa.App
import ru.ansmos.dombuketa.views.rw.*

class TouchHelper(val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) : ItemTouchHelper.Callback() {
    val interactor = App.instance.dagger.getInteractor()

    override fun isLongPressDragEnabled(): Boolean = false //Не поддерживается

    override fun isItemViewSwipeEnabled(): Boolean = true // Поддерживается

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(0, ItemTouchHelper.START or ItemTouchHelper.END)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = true

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //Удаляем элемент из списка после жеста swipe
        viewHolder
        when (viewHolder){
            is Product_V_ViewHolder ->{
                interactor.removeFavorite(
                    (adapter as Product_V_Adapter).removeItemFavorite(viewHolder.bindingAdapterPosition))
            }
            is Product_H_ViewHolder ->{
                interactor.removeVisited(
                    (adapter as Product_H_Adapter).removeItemVisited(viewHolder.bindingAdapterPosition))

            }
            is Notification_ViewHolder ->{
                interactor.cancelNotification(
                    (adapter as Notification_Adapter).removeNotification(viewHolder.bindingAdapterPosition))

            }
        }
    }
}