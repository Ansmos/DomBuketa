package ru.ansmos.dombuketa.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ru.ansmos.dombuketa.App
import ru.ansmos.dombuketa.AppConstants
import ru.ansmos.dombuketa.domain.Interactor
import ru.ansmos.dombuketa.models_bll.Product
import javax.inject.Inject

class DeliveresViewModel : ViewModel() {
    @Inject
    lateinit var interactor: Interactor
    val productListByTag: BehaviorSubject<List<Product>>

    init{
        App.instance.dagger.inject(this)
        productListByTag = interactor.productListByTag
    }

    fun refreshProductss() {
        Observable.fromArray(interactor.getProductListByTag_API(0,1, AppConstants.PRODUCT_PAGE_SIZE)).flatMap {
            productListByTag
        }
    }
}