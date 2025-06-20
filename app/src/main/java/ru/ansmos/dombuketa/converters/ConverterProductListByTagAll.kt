package ru.ansmos.dombuketa.converters

import ru.ansmos.dombuketa.models_bll.ProductListByTagAll
import ru.ansmos.dombuketa.net_module.models_api.ProductListByTagAll_api
import ru.ansmos.dombuketa.net_module.models_api.ProductListByTag_api

object ConverterProductListByTagAll {
    fun api_DTO(item: ProductListByTagAll_api): ProductListByTagAll {
        return ProductListByTagAll(
            page = 1,
            productListByTag = ConverterProductListByTag.apiList_DTOList(item.ProductListByTag as List<ProductListByTag_api>?)
        )
    }
}