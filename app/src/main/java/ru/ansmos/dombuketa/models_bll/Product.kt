package ru.ansmos.dombuketa.models_bll

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product (
    val id: Int,
    val name: String,
    val price : Price,
    val imageCart: Image,
    val imageGalary: List<Image>?,
    val description: String,
    var isInFavorites: Boolean = false,
) : Parcelable