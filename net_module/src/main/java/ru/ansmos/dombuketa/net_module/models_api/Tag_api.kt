package ru.ansmos.dombuketa.net_module.models_api

import com.google.gson.annotations.SerializedName

data class Tag_api (
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("date_event")
    val dateEvent: String?,
    @SerializedName("order")
    val order: Int
)
