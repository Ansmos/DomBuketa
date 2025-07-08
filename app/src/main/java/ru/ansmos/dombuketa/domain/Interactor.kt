package ru.ansmos.dombuketa.domain

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ru.ansmos.dombuketa.converters.ConverterProduct
import ru.ansmos.dombuketa.converters.ConverterProductListByTag
import ru.ansmos.dombuketa.converters.ConverterTag
import ru.ansmos.dombuketa.models_bll.Product
import ru.ansmos.dombuketa.models_bll.ProductListByTag
import ru.ansmos.dombuketa.models_bll.Tag
import ru.ansmos.dombuketa.net_module.ApiKey
import ru.ansmos.dombuketa.net_module.api.IDomBuketaApi2

class Interactor(private val retrofitService: IDomBuketaApi2) {
    val isProgressBarVisible: BehaviorSubject<Boolean> = BehaviorSubject.create()
    val productListByTagListAll: BehaviorSubject<List<ProductListByTag>> = BehaviorSubject.create()
    val productListByTag: BehaviorSubject<List<Product>> = BehaviorSubject.create()

    init {
        isProgressBarVisible.onNext(false)
    }

    fun getProductListByTagListAll_API() {
        isProgressBarVisible.onNext(true)
        retrofitService.getProductListByTags(ApiKey.KEY)
            .subscribeOn(Schedulers.io())
            .map {
                isProgressBarVisible.onNext(false)
                ConverterProductListByTag.apiList_DTOList(it.ProductListByTag)
            }
            .subscribe({
                productListByTagListAll.onNext(it)
                isProgressBarVisible.onNext(false)
            },{
                isProgressBarVisible.onNext(false)
            })
    }

    fun getTagList_API() : Observable<List<Tag>> {
        isProgressBarVisible.onNext(true)
        return retrofitService.getTags(ApiKey.KEY)
            .subscribeOn(Schedulers.io())
            .map {
                isProgressBarVisible.onNext(false)
                ConverterTag.apiList_DTOList(it)
            }
            .doOnError { isProgressBarVisible.onNext(false) }
    }

//    fun getProductListByTag_API(tag: Int, pageIndex: Int, pageSize: Int) : Observable<List<Product>> {
//        isProgressBarVisible.onNext(true)
//        return retrofitService.getItemsByTag(tag, pageIndex, pageSize, ApiKey.KEY)
//            .subscribeOn(Schedulers.io())
//            .map {
//                isProgressBarVisible.onNext(false)
//                ConverterProduct.apiList_DTOList(it.productList)
//            }
//            .doOnError { isProgressBarVisible.onNext(false) }
//    }
    fun getProductListByTag_API(tag: Int, pageIndex: Int, pageSize: Int) {
        isProgressBarVisible.onNext(true)
        retrofitService.getItemsByTag(tag, pageIndex, pageSize, ApiKey.KEY)
            .subscribeOn(Schedulers.io())
            .map {
                isProgressBarVisible.onNext(false)
                ConverterProduct.apiList_DTOList(it.productList)
            }
            .subscribe({
                productListByTag.onNext(it)
                isProgressBarVisible.onNext(false)
            }, {
                isProgressBarVisible.onNext(false)
            })

    }

}