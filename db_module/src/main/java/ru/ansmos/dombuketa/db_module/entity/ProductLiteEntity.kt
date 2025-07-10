package ru.ansmos.dombuketa.db_module.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "visited_products")
data class ProductLiteEntity (
    @PrimaryKey() val id: Int,
    //@ColumnInfo(name = "id_remote") val id_remote: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "price") val price : Double = 0.0,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "view_date") val view_date: String = "",
    var isInFavorites: Boolean = false
) : Parcelable
