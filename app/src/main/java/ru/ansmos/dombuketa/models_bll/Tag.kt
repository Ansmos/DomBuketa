package ru.ansmos.dombuketa.models_bll

data class Tag (
    val id: Int,
    val name: String,
    val description: String,
    val dateEvent: String?,
    val order: Int
)
