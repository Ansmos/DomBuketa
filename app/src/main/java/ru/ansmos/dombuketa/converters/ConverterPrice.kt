package ru.ansmos.dombuketa.converters

import ru.ansmos.dombuketa.models_bll.Price
import ru.ansmos.dombuketa.models_bll.Tag
import ru.ansmos.dombuketa.net_module.models_api.Price_api
import ru.ansmos.dombuketa.net_module.models_api.Tag_api

object ConverterPrice {
    fun apiList_DTOList(list: List<Price_api>): List<Price>{
        val result = mutableListOf<Price>()
        list.forEach {
            result.add(api_DTO(it))
        }
        return  result
    }

    fun api_DTO(price: Price_api): Price =
         Price(
            id = price.id,
            price = price.price,
            priceTotal = price.price,
            discountSumma = price.discountSumma,
            discountPercent = price.discountPercent,
            type = price.type
        )

}