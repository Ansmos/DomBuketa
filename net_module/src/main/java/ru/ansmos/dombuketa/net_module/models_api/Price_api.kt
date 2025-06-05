package ru.ansmos.dombuketa.net_module.models_api

import com.google.gson.annotations.SerializedName

data class Price_api(
    @SerializedName("id")
    val id: Int,
    @SerializedName("price")
    val price: Double,
    @SerializedName("price_total")
    val priceTotal: Double,
    @SerializedName("discount_s")
    val discountSumma: Double,
    @SerializedName("discount_p")
    val discountPercent: Double,
    @SerializedName("type")
    val type: Boolean
)
