package ru.ansmos.dombuketa.net_module.api

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.ansmos.dombuketa.net_module.ApiConstants
import ru.ansmos.dombuketa.net_module.models_api.*

interface IDomBuketaApi2 {
    // https://dom-buketa.ru/api/2/tags/list?key=3f86da7ac18eaf9e2906eb579c6be891
    // Выборка тегов по порядку + с фиксированными датами, которые встраиваются в этот порядок
    @GET("2/tags/list/")
    fun getTags(
        @Query("key") apiKey: String,
    ): Observable<List<Tag_api>>

    //https://dom-buketa.ru/api/2/items/tag/29?pageIndex=1&pageSize=10&key=3f86da7ac18eaf9e2906eb579c6be891
    // Выборка всех товаров (тег = 0) или по тегу
    @GET("2/items/tag/")
    fun getItems(
        @Query("tag") tag: Int,
        @Query("pageIndex") pageIndex: Int,
        @Query("pageSize") pageSize: Int,
        @Query("key") apiKey: String,
    ): Observable<ProductList_api>

    //https://dom-buketa.ru/api/2/catalog/all/?key=3f86da7ac18eaf9e2906eb579c6be891
    // Первоначальная загрузка
    @GET("2/catalog/all/")
    fun getProductListByTags(
        @Query("key") apiKey: String
    ): Observable<ProductListByTagAll_api>
}