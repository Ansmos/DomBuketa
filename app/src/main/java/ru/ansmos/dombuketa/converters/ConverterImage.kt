package ru.ansmos.dombuketa.converters

import ru.ansmos.dombuketa.models_bll.Image
import ru.ansmos.dombuketa.models_bll.Price
import ru.ansmos.dombuketa.net_module.models_api.Image_api
import ru.ansmos.dombuketa.net_module.models_api.Price_api

object ConverterImage {
    fun apiList_DTOList(list: List<Image_api>?): List<Image>{
        val result = mutableListOf<Image>()
        if (list != null) {
            list.forEach {
                result.add(api_DTO(it))
            }
        }
        return  result
    }

    fun api_DTO(image: Image_api): Image =
        Image(
            id = image.id,
            order = image.order,
            path =  image.path.replace("\\","/") + "/",
            fileName = image.fileName
        )
}