package ru.ansmos.dombuketa.net_module.models_api

import com.google.gson.annotations.SerializedName

data class Item_api(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price : Price_api,
    @SerializedName("image_cart")
    val imageCart: Image_api,
    @SerializedName("image_galary")
    val imageGalary: List<Image_api>,
    @SerializedName("description_short")
    val description: String
)
