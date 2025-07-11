package ru.ansmos.dombuketa.converters

import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.viewbinding.BindableItem
import ru.ansmos.dombuketa.models_bll.Product
import ru.ansmos.dombuketa.models_bll.ProductListByTag
import ru.ansmos.dombuketa.net_module.models_api.ProductListByTag_api
import ru.ansmos.dombuketa.net_module.models_api.Product_api
import ru.ansmos.dombuketa.views.rw.groupie.*

object ConverterProductListByTag {
    fun apiList_DTOList(list: List<ProductListByTag_api?>?): List<ProductListByTag>{
        val result = mutableListOf<ProductListByTag>()
        if (list != null) {
            list.forEach {
                it?.let { it1 -> api_DTO(it1) }
                    ?.let { it2 -> result.add(it2) }
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
            productList = ConverterProduct.api_DTO_List(item.productList as List<Product_api>?)
        )
    }

    // Ниже три конвертера для Groupie c подходом habr
    // Этот - список горизонтальных полос
    fun DTOList__SectionList(productList: List<ProductListByTag>?,
                                 mainClickListener: (str: String, pos: Int) -> Unit,
                                 mainScrollListener: (state: CarouselItem2.CarouselRVState, tagId: Int) -> Unit,
                                 itemClickListener: (product: Product, pos: Int) -> Unit ): List<Section>{
        val result = mutableListOf<Section>()
        var index : Int = 0
        if (productList != null) {
            productList.forEach {
                result.add(
                    DTO__Section(it,
                    mainClickListener,
                    mainScrollListener,
                    itemClickListener, index++))

            }
        }
        return result
    }
    fun DTO__Section(product: ProductListByTag,
                         mainClickListener: (str: String, pos: Int) -> Unit,
                         mainScrollListener: (state: CarouselItem2.CarouselRVState, tagId: Int) -> Unit,
                         itemClickListener: (product: Product, pos: Int) -> Unit, index : Int ): Section {

        val section = Section(
            HeaderItem2(
                HeaderItem2.CarouselCaption(
                    id = product.tagId,
                    title = product.nameTag,
                    description = product.descriptionTag
                ),
                mainClickListener//, mainScrollListener
            )
        )
        val carouselAdapter = object : GroupieAdapter(){
            var bebe: Int = index
                get() {return  field}
                set(value) { field = value }
        }
        // Заполним группу Продуктами
        val aa = addProductListToCarousel(product.productList, itemClickListener)
        carouselAdapter.addAll(aa)
        //Оформим это все в группу
        //section.add(CarouselGroup2(null, carouselAdapter, mainScrollListener, product.tagId))
        section.add(CarouselItem2(null, carouselAdapter, mainScrollListener, product.tagId))
        section.setHideWhenEmpty(true)
        return section
    }



    // Ниже три конвертера для Groupie c подходом habr
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

    fun addProductListToCarousel(list: List<Product>?,
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

