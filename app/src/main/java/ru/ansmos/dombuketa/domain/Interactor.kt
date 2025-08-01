package ru.ansmos.dombuketa.domain

import android.util.Log
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ru.ansmos.dombuketa.converters.ConverterNotification
import ru.ansmos.dombuketa.converters.ConverterProduct
import ru.ansmos.dombuketa.converters.ConverterProductListByTag
import ru.ansmos.dombuketa.converters.ConverterTag
import ru.ansmos.dombuketa.db_module.repo.MainRepository
import ru.ansmos.dombuketa.models_bll.Notification
import ru.ansmos.dombuketa.models_bll.Product
import ru.ansmos.dombuketa.models_bll.ProductListByTag
import ru.ansmos.dombuketa.models_bll.Tag
import ru.ansmos.dombuketa.net_module.ApiKey
import ru.ansmos.dombuketa.net_module.api.IDomBuketaApi2
import ru.ansmos.dombuketa.utils.PreferenceProvider

class Interactor(private val retrofitService: IDomBuketaApi2,
                 private val repo: MainRepository,
                 private val preferences: PreferenceProvider
) {
    val isProgressBarVisible: BehaviorSubject<Boolean> = BehaviorSubject.create()
    val productListByTagListAll: BehaviorSubject<List<ProductListByTag>> = BehaviorSubject.create()
    val productListByTag: BehaviorSubject<List<Product>> = BehaviorSubject.create()
    val product: BehaviorSubject<List<Product>> = BehaviorSubject.create()

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

    fun getProductById_API(id: Int) : Maybe<Product> {
        isProgressBarVisible.onNext(true)
        return retrofitService.getProductById(id, ApiKey.KEY)
            .subscribeOn(Schedulers.io())
            .map {
                if (it.id == 0){
                    ConverterProduct.empty()
                } else {
                    ConverterProduct.api_DTO(it)
                }
            }
            .doOnError {
                isProgressBarVisible.onNext(false)
            }
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

    fun getProductListByTag_API(tag: Int, pageIndex: Int, pageSize: Int) {
        isProgressBarVisible.onNext(true)
        retrofitService.getItemsByTag(tag, pageIndex, pageSize, ApiKey.KEY)
            .subscribeOn(Schedulers.io())
            .map {
                isProgressBarVisible.onNext(false)
                ConverterProduct.api_DTO_List(it.productList)
            }
            .subscribe({
                productListByTag.onNext(it)
                isProgressBarVisible.onNext(false)
            }, {
                isProgressBarVisible.onNext(false)
            })
    }

    fun updataVisitedProduct(product: Product) {
        Single.just(product)
            .observeOn(Schedulers.io())
            .map {
                ConverterProduct.dto_ProductLiteEntity(product)
            }
            .subscribe( {
                repo.updataProductLite(it)
                Log.i("intr.updataVisitedProduct","Просмотренный продукт добавлен/обновлен в БД")
            },{
                Log.e("intr.updataVisitedProduct","Ошибка. Просмотренный продукт не добавлен/обновлен в БД" + it.message)
            })
    }
    // Из БД
    fun isProductInFavorites(productId: Int) : Single<Boolean> = repo.isProductInFavorites(productId)

    // Из БД
    fun getVisitedProductList_DB(onlyFavorites: Boolean, pageIndex: Int, pageSize: Int) : Maybe<List<Product>> {
        return repo.getVisitedProducts(onlyFavorites, pageIndex, pageSize)
            .subscribeOn(Schedulers.io())
            .map {
                ConverterProduct.ProductLiteEntity_DTO_List(it)
            }
    }

    fun removeFavorite(productId: Int) =
        Single.just(productId)
            .observeOn(Schedulers.io())
            .subscribe({
                repo.deleteProductFavorite(it)
            },{
                it.printStackTrace()
            })

    fun removeVisited(productId: Int) =
        Single.just(productId)
            .observeOn(Schedulers.io())
            .subscribe({
                repo.deleteProductVisited(it)
            },{
                it.printStackTrace()
            })

    fun clearVisited() = repo.clearProductVisited()

// Нотификации **************************************************

    fun getNotifications(): Observable<List<Notification>> {
        return ConverterNotification.Entity_DTO_ListRx(repo.getAllNotifications())
    }

    fun getNotificationById(id: Int) : Single<Notification>? {
        return repo.getNotificationById(id)
            ?.subscribeOn(Schedulers.io())
            ?.map {
                ConverterNotification.Entity_DTO(it)
            }
    }

    fun updateNotification(notification: Notification) {
        Single.just(notification)
            .observeOn(Schedulers.io())
            .map {
                it.toEntity()
            }
            .subscribe( {
                repo.updateNotification(it)
                println("!!! Нотификация Обновлена в БД")
            },{
                println("!!! ОШИБКА: Нотификация не обновлена в БД" + it.message)
            })
    }

    fun cancelNotification(notification_id: Int){
        Single.just(true)
            .observeOn(Schedulers.io())
            .subscribe( {
                repo.cancelNotification(notification_id)
                println("!!! Нотификация отмененав в БД")
            },{
                println("!!! ОШИБКА: Нотификация не отменена БД" + it.message)
            })
    }

// Preferences ********************************************************

    fun getDarkModeFromPreferences() = preferences.getDefNightMode()

    fun saveDarkModeToPreferences(darkMode: Boolean) = preferences.setDefNightMode(darkMode)

}