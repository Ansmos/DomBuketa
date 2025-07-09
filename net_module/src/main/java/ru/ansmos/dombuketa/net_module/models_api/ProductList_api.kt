package ru.ansmos.dombuketa.net_module.models_api

import com.google.gson.annotations.SerializedName

data class ProductList_api(
    @SerializedName("page")
    val page: Int,
    @SerializedName("result")
    val productList: List<Product_api>,
)
