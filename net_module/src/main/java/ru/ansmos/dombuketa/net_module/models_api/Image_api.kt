package ru.ansmos.dombuketa.net_module.models_api

import com.google.gson.annotations.SerializedName

data class Image_api(
    @SerializedName("id")
    val id: Int,
    @SerializedName("order")
    val order: Int,
    @SerializedName("path")
    val path: String,
    @SerializedName("file_name")
    val fileName: String
)
