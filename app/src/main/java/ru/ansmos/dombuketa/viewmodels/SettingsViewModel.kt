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

class SettingsViewModel : ViewModel() {
    @Inject
    lateinit var interactor: Interactor
    val tagList : Observable<List<Tag>>
    val showProgressBar : BehaviorSubject<Boolean>
    val productListByTagListAll: Observable<List<ProductListByTag>>
    val productListByTag: BehaviorSubject<List<Product>>
    //Для pagging по категориям нужно хранить, по какой категории какая страница загружена
    var mapPagingByTag = mutableMapOf<Int, Int>()


    init{
        App.instance.dagger.inject(this)
        tagList = interactor.getTagList_API()
        showProgressBar = interactor.isProgressBarVisible
        //Запрос на первый запуск
        productListByTagListAll = interactor.productListByTagListAll // interactor.getProductListByTagListAll_API()
        //
        productListByTag = interactor.productListByTag
        //productListByTag = interactor.getProductListByTag_API(29, 2, 10)

    }

    fun refreshTags() {
        Observable.fromArray(interactor.getTagList_API()).flatMap {
            tagList
        }
    }
    fun refreshProductListByTagListAll() {
        interactor.getProductListByTagListAll_API()
    }

    fun refreshProductListByTag(tagId: Int, pageSize: Int) {
        interactor.getProductListByTag_API(tagId, getPageNumberByTag(tagId), pageSize)
        Log.i("intr.refreshProductListByTag", "tagId=${tagId}, page=${mapPagingByTag.get(tagId)}")
    }

    fun getPageNumberByTag(tagId : Int) : Int {
        var pageNumberDefault = mapPagingByTag.get(tagId) ?: 0

        if (pageNumberDefault == 0){
            // Первая страница уже загружена, выдаем вторую
            pageNumberDefault = 2
            mapPagingByTag?.putIfAbsent(tagId, pageNumberDefault)
        } else {
            mapPagingByTag.replace(tagId, ++pageNumberDefault)
        }
        return  pageNumberDefault
    }
}