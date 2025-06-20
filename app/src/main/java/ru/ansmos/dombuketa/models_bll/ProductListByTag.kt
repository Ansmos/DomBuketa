package ru.ansmos.dombuketa.models_bll

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.ansmos.dombuketa.net_module.models_api.ProductListByTag_api

@Parcelize
data class ProductListByTag(
    val tagId: Int,
    val nameTag: String,
    val descriptionTag: String,
    val page: Int,
    val productList: List<Product>?
) : Parcelable

@Parcelize
data class ProductListByTagAll(
    val page: Int,
    val productListByTag: List<ProductListByTag>?
) : Parcelable
