package ru.ansmos.dombuketa.converters

import io.reactivex.rxjava3.core.Observable
import ru.ansmos.dombuketa.R
import ru.ansmos.dombuketa.db_module.entity.NotificationEntity
import ru.ansmos.dombuketa.db_module.entity.ProductLiteEntity
import ru.ansmos.dombuketa.models_bll.Image
import ru.ansmos.dombuketa.models_bll.Notification
import ru.ansmos.dombuketa.models_bll.Price
import ru.ansmos.dombuketa.models_bll.Product
import ru.ansmos.dombuketa.net_module.models_api.Product_api
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object ConverterProduct {
    fun api_DTO_List(list: List<Product_api>?): List<Product>{
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

    fun dto_ProductLiteEntity(product: Product): ProductLiteEntity{
        return ProductLiteEntity(
            id = product.id,
            //id_remote = product.id,
            name = product.name,
            price = product.price.priceTotal,
            image = (product.imageCart?.path ?: "") + (product.imageCart?.fileName ?: ""),
            description = product.description,
            view_date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
            isInFavorites = product.isInFavorites
        )
    }

    fun ProductLiteEntity_DTO_List(list: List<ProductLiteEntity>): List<Product>{
        val result = mutableListOf<Product>()
        if (list != null) {
            list.forEach {
                result.add(ProductLiteEntity_DTO(it))
            }
        }
        return  result
    }

    fun ProductLiteEntity_DTO(item: ProductLiteEntity): Product{
        return Product(
            id = item.id,
            name = item.name,
            description = item.description,
            price = Price(
                id = 0,
                price = item.price,
                priceTotal = item.price,
                discountSumma = 0.0,
                discountPercent = 0.0,
                type = false
            ),
            imageCart = Image(
                id = 0,
                order = 1,
                path = "",
                fileName = item.image  //Хитрю, чтобы отлельно путь и имя файла не разбирать, все равно они сложатся
            ),
            imageGalary = null,
            isInFavorites = item.isInFavorites
        )
    }

    fun empty() = Product(0, R.string.fragment_details_no_product.toString(),
        Price(0,0.0,0.0,0.0,0.0,false),
        null, null, R.string.fragment_details_no_product.toString(), false)

// Конвертеры для Notifications
}
