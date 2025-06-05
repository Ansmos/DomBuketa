package ru.ansmos.dombuketa.domain

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ru.ansmos.dombuketa.converters.ConverterItem
import ru.ansmos.dombuketa.converters.ConverterTag
import ru.ansmos.dombuketa.models_bll.Tag
import ru.ansmos.dombuketa.net_module.ApiKey
import ru.ansmos.dombuketa.net_module.api.IDomBuketaApi2
import ru.ansmos.dombuketa.net_module.models_api.Tag_api

class Interactor(private val retrofitService: IDomBuketaApi2) {
    val isProgressBarVisibl: BehaviorSubject<Boolean> = BehaviorSubject.create()

    init {
        isProgressBarVisibl.onNext(false)
    }

    fun getTagListFromAPI() : Observable<List<Tag>> {
        isProgressBarVisibl.onNext(true)
        return retrofitService.getTags(ApiKey.KEY)
            .subscribeOn(Schedulers.io())
            .map {
                isProgressBarVisibl.onNext(false)
                ConverterTag.apiList_DTOList(it)
                //progressBarStateRx.onNext(true)
            }
            .doOnError { isProgressBarVisibl.onNext(false) }
    }

}