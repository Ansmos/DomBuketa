package ru.ansmos.dombuketa.models_bll

data class Product (
    val id: Int,
    val name: String,
    val price : Price,
    val imageCart: Image,
    val imageGalary: List<Image>,
    val description: String
)