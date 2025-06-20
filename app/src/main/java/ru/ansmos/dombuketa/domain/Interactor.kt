package ru.ansmos.dombuketa.domain

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ru.ansmos.dombuketa.converters.ConverterProduct
import ru.ansmos.dombuketa.converters.ConverterTag
import ru.ansmos.dombuketa.converters.ConverterProductListByTagAll
import ru.ansmos.dombuketa.models_bll.Product
import ru.ansmos.dombuketa.models_bll.ProductListByTagAll
import ru.ansmos.dombuketa.models_bll.Tag
import ru.ansmos.dombuketa.net_module.ApiKey
import ru.ansmos.dombuketa.net_module.api.IDomBuketaApi2

class Interactor(private val retrofitService: IDomBuketaApi2) {
    val isProgressBarVisible: BehaviorSubject<Boolean> = BehaviorSubject.create()

    init {
        isProgressBarVisible.onNext(false)
    }

    fun getProductListByTagListAllFromAPI() : Observable<ProductListByTagAll> {
        isProgressBarVisible.onNext(true)
        return retrofitService.getProductListByTags(ApiKey.KEY)
            .subscribeOn(Schedulers.io())
            .map {
                isProgressBarVisible.onNext(false)
                ConverterProductListByTagAll.api_DTO(it)
            }
            .doOnError { isProgressBarVisible.onNext(false) }
    }

    fun getTagListFromAPI() : Observable<List<Tag>> {
        isProgressBarVisible.onNext(true)
        return retrofitService.getTags(ApiKey.KEY)
            .subscribeOn(Schedulers.io())
            .map {
                isProgressBarVisible.onNext(false)
                ConverterTag.apiList_DTOList(it)
            }
            .doOnError { isProgressBarVisible.onNext(false) }
    }

    fun getProductListFromAPI(tag: Int, pageIndex: Int, pageSize: Int) : Observable<List<Product>> {
        isProgressBarVisible.onNext(true)
        return retrofitService.getItems(tag, pageIndex, pageSize, ApiKey.KEY)
            .subscribeOn(Schedulers.io())
            .map {
                isProgressBarVisible.onNext(false)
                ConverterProduct.apiList_DTOList(it.productList)
            }
            .doOnError { isProgressBarVisible.onNext(false) }
    }
}