package ru.ansmos.dombuketa.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import ru.ansmos.dombuketa.App
import ru.ansmos.dombuketa.domain.Interactor
import ru.ansmos.dombuketa.models_bll.ProductListByTagAll
import ru.ansmos.dombuketa.models_bll.Tag
import javax.inject.Inject

class HomeViewModel : ViewModel() {
    @Inject
    lateinit var interactor: Interactor
    val tagList : Observable<List<Tag>>
    var productListByTagListAll: Observable<ProductListByTagAll>

    init{
        App.instance.dagger.inject(this)
        tagList = interactor.getTagListFromAPI()
        productListByTagListAll = interactor.getProductListByTagListAllFromAPI()
    }

    fun refreshTags() {
        Observable.fromArray(interactor.getTagListFromAPI()).flatMap {
            tagList
        }
    }
    fun refreshProductListByTagListAll() {
//        Observable.fromArray(interactor.getProductListByTagListAllFromAPI()).flatMap {
//            productListByTagListAll
//        }
        productListByTagListAll = interactor.getProductListByTagListAllFromAPI()
    }

}