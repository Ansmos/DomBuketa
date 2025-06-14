package ru.ansmos.dombuketa.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import ru.ansmos.dombuketa.App
import ru.ansmos.dombuketa.AppConstants
import ru.ansmos.dombuketa.domain.Interactor
import ru.ansmos.dombuketa.models_bll.Product
import ru.ansmos.dombuketa.models_bll.Tag
import javax.inject.Inject

class DeliveresViewModel : ViewModel() {
    @Inject
    lateinit var interactor: Interactor
    val productList : Observable<List<Product>>

    init{
        App.instance.dagger.inject(this)
        productList = interactor.getProductListFromAPI(0, 1, AppConstants.PRODUCT_PAGE_SIZE)
    }

    fun refreshProductss() {
        Observable.fromArray(interactor.getProductListFromAPI(0,1, AppConstants.PRODUCT_PAGE_SIZE)).flatMap {
            productList
        }

    }
}