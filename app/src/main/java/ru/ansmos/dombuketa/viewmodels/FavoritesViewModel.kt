package ru.ansmos.dombuketa.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ru.ansmos.dombuketa.App
import ru.ansmos.dombuketa.domain.Interactor
import ru.ansmos.dombuketa.models_bll.Product
import javax.inject.Inject

class FavoritesViewModel : ViewModel() {
    @Inject
    lateinit var interactor: Interactor
    val productListVisited: Maybe<List<Product>>
    val productListfavorites: Maybe<List<Product>>

    init{
        App.instance.dagger.inject(this)
        productListVisited = interactor.getVisitedProductList_DB(false,0, 100)
        //productListfavorites = interactor.productListFavorites
        productListfavorites = interactor.getVisitedProductList_DB(true,0, 100)
        //refreshVisitedFavoritesProducts(true)
        //refreshVisitedFavoritesProducts(false)
    }

    //fun refreshVisitedFavoritesProducts(onlyFavorites: Boolean) = interactor.getVisitedProductList(onlyFavorites,0,100)
}