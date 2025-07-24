package ru.ansmos.dombuketa.db_module.repo

import androidx.room.Query
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.ansmos.dombuketa.db_module.dao.INotificationDao
import ru.ansmos.dombuketa.db_module.dao.IProductLiteDao
import ru.ansmos.dombuketa.db_module.entity.NotificationEntity
import ru.ansmos.dombuketa.db_module.entity.ProductLiteEntity
import java.util.concurrent.Executors
import javax.inject.Inject

class MainRepository @Inject constructor(private val productLiteDao: IProductLiteDao, private val notificationDao: INotificationDao)  {
    // Добавление/обновление просмотренного продукта
    fun updataProductLite(productLiteEntity: ProductLiteEntity){
        productLiteDao.updateVisitedProducts(productLiteEntity)
    }

    fun getVisitedProducts(onlyFavorites: Boolean, pageIndex: Int, pageSize: Int): Maybe<List<ProductLiteEntity>> {
        return productLiteDao.getVisitedProductsByPage(onlyFavorites, pageIndex, pageSize)
    }

    //Запрос в Избранных ли продукт
    fun isProductInFavorites(productId: Int) : Single<Boolean> = productLiteDao.isProductInFavorite(productId)

    //Удаление из избранного
    fun deleteProductFavorite(id: Int) : Int = productLiteDao.deleteProductFavorite(id)

    //Удаление посещенного
    fun deleteProduct(id: Int) : Int = deleteProduct(id)


    fun putFilms(productList: List<ProductLiteEntity>) {
        //Запросы в БД должны быть в отдельном потоке
        Executors.newSingleThreadExecutor().execute {
            productLiteDao.insertAll(productList)
        }
    }


//    fun getFilmsPaging(): androidx.paging.DataSource.Factory<Int, ProductLiteEntity> {
//        return productLiteDao.getFilms_Paging()
//    }


    fun clearAllFilms() : Int {
        var deletedItemsCount : Int = 0
        Executors.newSingleThreadExecutor().execute {
             deletedItemsCount = productLiteDao.clearAll()
        }
        //Омновной поток не ждет другого, поэтому возвращает 0, если через дебаг, правильно. Как сделать возврат?
        return deletedItemsCount
    }

// Нотификации ********************************************

    fun getAllNotifications(): Observable<List<NotificationEntity>> = notificationDao.getAllNotifications()

    fun getNotificationById(id: Int) : Single<NotificationEntity>? {
        return notificationDao.getNotificationById(id)
    }

    fun insertNotification(notification: NotificationEntity) {
        notificationDao.insertNotification(notification)
    }

    fun updateNotification(notification: NotificationEntity) {
        // Для упрощения деактивирую старый и вставляю новый
        notificationDao.cancelNotification(notification.productId)
        notificationDao.insertNotification(notification)
    }

    fun cancelNotification(film_id: Int) = notificationDao.cancelNotification(film_id)

}