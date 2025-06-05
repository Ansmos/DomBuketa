package ru.ansmos.dombuketa.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import ru.ansmos.dombuketa.App
import ru.ansmos.dombuketa.domain.Interactor
import ru.ansmos.dombuketa.models_bll.Tag
import javax.inject.Inject

class HomeViewModel : ViewModel() {

    @Inject
    lateinit var interactor: Interactor
    val tagList : Observable<List<Tag>>

    init{
        App.instance.dagger.inject(this)
        tagList = interactor.getTagListFromAPI()
    }

    fun refreshTags() {
        Observable.fromArray(interactor.getTagListFromAPI()).flatMap {
            tagList
        }

    }
}