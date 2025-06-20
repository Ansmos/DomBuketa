package ru.ansmos.dombuketa.net_module.models_api

import com.google.gson.annotations.SerializedName

data class ProductListByTag_api(
    val page: Int,
    @SerializedName("tag_id")val tagId: Int,
    @SerializedName("tag")val nameTag: String,
    @SerializedName("tag_description")val descriptionTag: String,
    @SerializedName("result") val productList: List<Product_api?>?
)

data class ProductListByTagAll_api(
    val page: Int,
    @SerializedName("result") val ProductListByTag: List<ProductListByTag_api?>?
)