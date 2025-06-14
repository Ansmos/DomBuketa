package ru.ansmos.dombuketa.models_bll

data class Price (
    val id: Int,
    val price: Double,
    val priceTotal: Double,
    val discountSumma: Double,
    val discountPercent: Double,
    val type: Boolean
)