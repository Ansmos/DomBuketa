package ru.ansmos.dombuketa.converters

import ru.ansmos.dombuketa.db_module.entity.ProductLiteEntity
import ru.ansmos.dombuketa.models_bll.Product
import ru.ansmos.dombuketa.net_module.models_api.Product_api
import java.text.SimpleDateFormat
import java.util.*

object ConverterProduct {
    fun apiList_DTOList(list: List<Product_api>?): List<Product>{
        val result = mutableListOf<Product>()
        if (list != null) {
            list.forEach {
                result.add(api_DTO(it))
            }
        }
        return  result
    }

    fun api_DTO(item: Product_api): Product {
        return Product(
            id = item.id,
            name = item.name,
            description = item.description,
            price = ConverterPrice.api_DTO(item.price),
            imageCart = ConverterImage.api_DTO(item.imageCart),
            imageGalary = ConverterImage.apiList_DTOList(item.imageGalary)
        )
    }

    fun to_ProductLiteEntity(product: Product): ProductLiteEntity{
        return ProductLiteEntity(
            id = product.id,
            //id_remote = product.id,
            name = product.name,
            price = product.price.priceTotal,
            image = product.imageCart.path + product.imageCart.fileName,
            description = product.description,
            view_date = SimpleDateFormat("dd.mm.yyyy").format(Date()),
            isInFavorites = product.isInFavorites
        )
    }
}
