package ru.ansmos.dombuketa.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ru.ansmos.dombuketa.App
import ru.ansmos.dombuketa.AppConstants
import ru.ansmos.dombuketa.domain.Interactor
import ru.ansmos.dombuketa.models_bll.Product
import ru.ansmos.dombuketa.utils.SingleLiveEvent
import java.io.IOException
import java.net.URL
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DetailsProductViewModel : ViewModel() {
    @Inject
    lateinit var interactor: Interactor
    val isNetworkError = SingleLiveEvent<String>()


    init {
        App.instance.dagger.inject(this)
    }

    fun updateVisitedProduct(product: Product){
        interactor.updataVisitedProduct(product)
    }

    fun isProductInFavorites(productId: Int) : Single<Boolean> = interactor.isProductInFavorites(productId)

    fun getProductById(productId: Int): Maybe<Product> = interactor.getProductById_API(productId)

    suspend fun loadWallpaper(url: String) : Bitmap?{
        return suspendCoroutine{
            val url = URL(url)
            var bitmap : Bitmap? = null
            try {
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            } catch (e : IOException) { //42*
                isNetworkError.postValue(ERROR_CONNECTION) //42*
            }
            it.resume(bitmap)
        }
    }

    fun clearError() {
        isNetworkError.postValue("")
    }



    companion object{
        const val ERROR_CONNECTION = "Ошибка соединения с сервером."
    }

}