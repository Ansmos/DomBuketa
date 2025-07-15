package ru.ansmos.dombuketa.db_module.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.ansmos.dombuketa.db_module.entity.ProductLiteEntity


@Dao
interface IProductLiteDao {
    //Записываем посещенный продукт
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateVisitedProducts(productLiteEntity: ProductLiteEntity)

    //Узнаем, в избранных ли прозукт
    @Query("SELECT isInFavorites FROM visited_products WHERE id = (:productId)")
    fun isProductInFavorite(productId: Int) : Single<Boolean>

    //Запрос на всю таблицу постранично
    @Query("SELECT * FROM visited_products WHERE isInFavorites = (:onlyFavorites) LIMIT (:pageSize) OFFSET (:pageIndex * 10)")
    //fun getVisitedProductsByPage(onlyFavorites: Boolean, pageIndex : Int, pageSize: Int): Observable<List<ProductLiteEntity>>
    fun getVisitedProductsByPage(onlyFavorites: Boolean, pageIndex : Int, pageSize: Int): Maybe<List<ProductLiteEntity>>

//    @Query("SELECT * FROM visited_products LIMIT (:pageSize) OFFSET (:pageIndex * 10)")
//    fun getVisitedProducts_Paging(pageIndex : Int, pageSize: Int): DataSource.Factory<Int, FilmEntity>
//
//    @Query("SELECT * FROM cached_films")
//    fun getFilms_Paging(): DataSource.Factory<Int, FilmEntity>
//
    //Удаление из избранного
    @Query("UPDATE visited_products SET isInFavorites = 0 WHERE id = (:id)")
    fun deleteProductFavorite(id: Int) : Int

    //Удаление посещенного
    @Query("DELETE FROM visited_products WHERE id = (:id)")
    fun deleteProduct(id: Int) : Int


    //Кладём списком в БД, в случае конфликта перезаписываем
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<ProductLiteEntity>)

    @Query("DELETE FROM visited_products")
    fun clearAll() : Int
}