package ru.ansmos.dombuketa.converters

import ru.ansmos.dombuketa.models_bll.Tag
import ru.ansmos.dombuketa.net_module.models_api.Tag_api

object ConverterTag {

    fun apiList_DTOList(list: List<Tag_api>): List<Tag>{
        val result = mutableListOf<Tag>()
        list.forEach {
            result.add(api_DTO(it))
        }
        return  result
    }

    fun api_DTO(tag: Tag_api): Tag {
        return Tag(
            id = tag.id,
            name = tag.name,
            description = tag.description,
            dateEvent = tag.dateEvent,
            order = tag.order
        )}
}

