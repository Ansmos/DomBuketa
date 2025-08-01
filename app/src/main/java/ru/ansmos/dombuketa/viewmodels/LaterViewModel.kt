package ru.ansmos.dombuketa.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.ansmos.dombuketa.App
import ru.ansmos.dombuketa.domain.Interactor
import ru.ansmos.dombuketa.models_bll.Notification
import ru.ansmos.dombuketa.models_bll.Product
import javax.inject.Inject

class LaterViewModel: ViewModel() {
    lateinit var notificationsRx : Observable<List<Notification>>
    @Inject
    lateinit var interactor: Interactor

    init{
        App.instance.dagger.inject(this)
        notificationsRx = interactor.getNotifications()
    }

    fun getProduct(id: Int) : Maybe<Product> {
        return interactor.getProductById_API(id)
    }

    fun getNotification(id: Int) : Single<Notification>?{
        return interactor.getNotificationById(id)
    }
}