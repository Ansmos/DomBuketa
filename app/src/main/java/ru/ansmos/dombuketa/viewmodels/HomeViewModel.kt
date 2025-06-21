package ru.ansmos.dombuketa.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ru.ansmos.dombuketa.App
import ru.ansmos.dombuketa.domain.Interactor
import ru.ansmos.dombuketa.models_bll.ProductListByTagAll
import ru.ansmos.dombuketa.models_bll.Tag
import javax.inject.Inject

class HomeViewModel : ViewModel() {
    @Inject
    lateinit var interactor: Interactor
    val tagList : Observable<List<Tag>>
    val showProgressBar : BehaviorSubject<Boolean>
    var productListByTagListAll: Observable<ProductListByTagAll>

    init{
        App.instance.dagger.inject(this)
        tagList = interactor.getTagListFromAPI()
        showProgressBar = interactor.isProgressBarVisible
        productListByTagListAll = interactor.getProductListByTagListAllFromAPI()
    }

    fun refreshTags() {
        Observable.fromArray(interactor.getTagListFromAPI()).flatMap {
            tagList
        }
    }
    fun refreshProductListByTagListAll() {
        productListByTagListAll = interactor.getProductListByTagListAllFromAPI()
    }

}