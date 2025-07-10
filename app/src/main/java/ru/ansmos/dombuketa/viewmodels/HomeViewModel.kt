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
    //Для pagging по категориям нужно хранить, по какой категории какая страница загружена
    var mapPagingByTag = mutableMapOf<Int, Int>()

    init{
        App.instance.dagger.inject(this)
        tagList = interactor.getTagList_API()
        showProgressBar = interactor.isProgressBarVisible
        //Запрос на первый запуск
        productListByTagListAll = interactor.productListByTagListAll
        interactor.getProductListByTagListAll_API()
        productListByTag = interactor.productListByTag
    }

    // Оставлю на будущее работу с тегами
    fun refreshTags() {
        Observable.fromArray(interactor.getTagList_API()).flatMap {
            tagList
        }
    }
    // Загрузка первой пачки данных для вложенных RV, согласно API
    fun refreshProductListByTagListAll() {
        interactor.getProductListByTagListAll_API()
    }
    // Загрузка для постраничного вывода RV по конкретному тегу
    fun refreshProductListByTag(tagId: Int, pageSize: Int) {
        interactor.getProductListByTag_API(tagId, getPageNumberByTag(tagId), pageSize)
        Log.i("intr.refreshProductListByTag", "tagId=${tagId}, page=${mapPagingByTag.get(tagId)}")
    }

    // Процедура ведет карту подгруженных страниц по категориям
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
    fun updateVisitedProduct(product: Product){
        interactor.updataVisitedProduct(product)
    }

}