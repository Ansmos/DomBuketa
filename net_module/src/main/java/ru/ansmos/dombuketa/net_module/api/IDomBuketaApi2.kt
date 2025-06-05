package ru.ansmos.dombuketa.net_module.api

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.ansmos.dombuketa.net_module.models_api.Item_api
import ru.ansmos.dombuketa.net_module.models_api.Tag_api

interface IDomBuketaApi2 {
    // https://dom-buketa.ru/api/2/tags/list?key=3f86da7ac18eaf9e2906eb579c6be891
    // Выборка тегов по порядку + с фиксированными датами, которые встраиваются в этот порядок
    @GET("2/tags/list/")
    fun getTags(
        @Query("key") apiKey: String,
    ): Observable<List<Tag_api>>


    //http://localhost:52682/api/2/items/tag/29?pageIndex=1&pageSize=10&key=3f86da7ac18eaf9e2906eb579c6be891
    // Выборка товаров по тегу
    @GET("2/items/tag/")
    fun getItems(
        @Query("key") apiKey: String,
    ): Observable<List<Item_api>>

}