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

    init{
        App.instance.dagger.inject(this)
        tagList = interactor.getTagList_API()
        showProgressBar = interactor.isProgressBarVisible
        //Запрос на первый запуск
        productListByTagListAll = interactor.productListByTagListAll // interactor.getProductListByTagListAll_API()
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
    // Пестая процедура потому что пример с habr не позволяет менять уже загруженную пачку,
    // хорош только для статического ьпримера или для написания статьи
    fun refreshProductListByTag(tagId: Int, pageSize: Int) {
    }

}