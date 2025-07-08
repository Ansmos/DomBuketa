package ru.ansmos.dombuketa.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ru.ansmos.dombuketa.App
import ru.ansmos.dombuketa.domain.Interactor
import ru.ansmos.dombuketa.models_bll.Product
import ru.ansmos.dombuketa.models_bll.ProductListByTag
import ru.ansmos.dombuketa.models_bll.Tag
import javax.inject.Inject

class HomeViewModel : ViewModel() {
    @Inject
    lateinit var interactor: Interactor
    val tagList : Observable<List<Tag>>
    val showProgressBar : BehaviorSubject<Boolean>
    var productListByTagListAll: Observable<List<ProductListByTag>>
    val productListByTag: BehaviorSubject<List<Product>>

    init{
        App.instance.dagger.inject(this)
        tagList = interactor.getTagList_API()
        showProgressBar = interactor.isProgressBarVisible
        //Запрос на первый запуск
        productListByTagListAll = interactor.productListByTagListAll
        interactor.getProductListByTagListAll_API()
        //TODO
        //productListByTag = interactor.getProductListByTag_API(29, 2, 10)
        productListByTag = interactor.productListByTag
    }

    fun refreshTags() {
        Observable.fromArray(interactor.getTagList_API()).flatMap {
            tagList
        }
    }
    fun refreshProductListByTagListAll() {
        interactor.getProductListByTagListAll_API()
    }

    fun refreshProductListByTag(tagId: Int, pageIndex:Int, pageSize: Int) {
        interactor.getProductListByTag_API(tagId, pageIndex, pageSize)
        Log.i("interactor", "refreshProductListByTag")
    }

}