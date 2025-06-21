package ru.ansmos.dombuketa.converters

import com.xwray.groupie.viewbinding.BindableItem
import ru.ansmos.dombuketa.models_bll.Product
import ru.ansmos.dombuketa.models_bll.ProductListByTag
import ru.ansmos.dombuketa.net_module.models_api.ProductListByTag_api
import ru.ansmos.dombuketa.net_module.models_api.Product_api
import ru.ansmos.dombuketa.views.rw.groupie.ItemCarousel
import ru.ansmos.dombuketa.views.rw.groupie.ProductItem

object ConverterProductListByTag {
    fun apiList_DTOList(list: List<ProductListByTag_api>?): List<ProductListByTag>{
        val result = mutableListOf<ProductListByTag>()
        if (list != null) {
            list.forEach {
                result.add(ConverterProductListByTag.api_DTO(it))
            }
        }
        return  result
    }

    fun api_DTO(item: ProductListByTag_api): ProductListByTag {
        return ProductListByTag(
            page = 1,
            tagId = item.tagId,
            nameTag = item.nameTag,
            descriptionTag = item.descriptionTag,
            productList = ConverterProduct.apiList_DTOList(item.productList as List<Product_api>?)
        )
    }

    // Ниже три конвертера для Groupie
    // Этот - список горизонтальных полос
    fun DTOList_ItemCarouselList(productList: List<ProductListByTag>?,
                                 mainClickListener: (str: String, pos: Int) -> Unit,
                                 mainScrollListener: (state: ItemCarousel.CarouselRVState, tagId: Int) -> Unit,
                                 itemClickListener: (product: Product, pos: Int) -> Unit ): List<ItemCarousel>{
        val result = mutableListOf<ItemCarousel>()
        if (productList != null) {
            productList.forEach {
                result.add(DTO_ItemCarousel(it,
                    mainClickListener,
                    mainScrollListener,
                    itemClickListener))
            }
        }
        return result
    }

    // Это горизонтальная полоса с элементами, в нее передаем слушатель для щелчка по ней,
    // и слушатель для каждого итема внутри
    fun DTO_ItemCarousel(product: ProductListByTag,
                         mainClickListener: (str: String, pos: Int) -> Unit,
                         mainScrollListener: (state: ItemCarousel.CarouselRVState, tagId: Int) -> Unit,
                         itemClickListener: (product: Product, pos: Int) -> Unit ): ItemCarousel{
        return ItemCarousel(
            ItemCarousel.CarouselContent(
                id = product.tagId,
                title = product.nameTag,
                description = product.descriptionTag
            ),
            mainClickListener,
            mainScrollListener,
            addProductListToCarousel(product.productList, itemClickListener)
        )
    }

    private fun addProductListToCarousel(list: List<Product>?,
            itemClicklistener: (product: Product, position: Int) -> Unit): MutableList<BindableItem<*>> {
        val result = mutableListOf<BindableItem<*>>()
        if (list != null) {
            list.forEach {
                result.add(ProductItem(it, itemClicklistener))
            }
        }
        return result
    }
}

