package ru.ansmos.dombuketa.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.Group
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.viewbinding.BindableItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.ansmos.dombuketa.R
import ru.ansmos.dombuketa.converters.ConverterProductListByTag
import ru.ansmos.dombuketa.databinding.FragmentSettingsBinding
import ru.ansmos.dombuketa.helpers.AutoDisposable
import ru.ansmos.dombuketa.helpers.addTo
import ru.ansmos.dombuketa.models_bll.Product
import ru.ansmos.dombuketa.viewmodels.SettingsViewModel
import ru.ansmos.dombuketa.views.rw.groupie.*

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val autoDisposable = AutoDisposable()
    lateinit var mainAdapterGroupie : GroupieAdapter
    //private val mainAdapterGroupie = GroupieAdapter()
    private var defaultCarouselItem2: CarouselItem2? = null

    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(SettingsViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        autoDisposable.bindTo(lifecycle)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel.refreshProductListByTagListAll()
        subscribeToProductListByTagListAll()

        // Horizontal carousel
        val carouselSection = Section(HeaderItem2(HeaderItem2.CarouselCaption(0,"Заголовок", "Краткое примечание"), ::onCarouselCardClick))
 //TODO       carouselSection.setHideWhenEmpty(true)
        subscribeToProductListByTag()
        //subscribeToProductListByTag((mainAdapterGroupie.getGroupAtAdapterPosition(1)) as Section)

        initRV()
    }

    private fun subscribeToProductListByTagListAll(){
        viewModel.productListByTagListAll
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                //Передаем в ItemCarousel обработчики нажатий
                ConverterProductListByTag.DTOList__SectionList(
                    it, ::onCarouselCardClick, ::onCarouselCardScroll, ::onProductItemClick)
            }
            .subscribe({
                mainAdapterGroupie.addAll(it)
            })
            .addTo(autoDisposable)
    }


    private fun subscribeToProductListByTag() {
        viewModel.productListByTag
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                ConverterProductListByTag.addProductListToCarousel(it, ::onProductItemClick)
            }
            .subscribe({
                defaultCarouselItem2
            //                ((mainAdapterGroupie.getGroupAtAdapterPosition(1) as Section)
            //                    .groups[0].getItem(0) as CarouselItem2)
                    ?.addProducts(it)
            },{
                it.printStackTrace()
            })
            .addTo(autoDisposable)
    }

    private fun initRV() {
        val rv = binding.settingsFragmentRoot.findViewById<RecyclerView>(R.id.settings_recycler)
        rv.apply {
            mainAdapterGroupie = GroupieAdapter()
            adapter = mainAdapterGroupie
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    //Click по шапке гооризонтального блока (carousel)
    fun onCarouselCardClick(name: String, id: Int) {
        println("onCarouselCardClick " + name + " posM=" + id)
    }
    fun onProductItemClick(product: Product, pos: Int) {
        println("onProductItemClick: Id=${product.id} - ${product.name}, price=${product.price.price}")
    }
    fun onCarouselCardScroll(state: CarouselItem2.CarouselRVState, tagId: Int) {
        println("Fragm.Scroll: visPos=${state.visibleItemPos} (visCount=${state.visibleItemsCount}/total=${state.totalItemCount}), tagId=${tagId}")
        //Если до конца списка осталось 4 элемента и если кольчество элементов >= страницы, значит есть, что загружать
        if (((state.visibleItemsCount + state.visibleItemPos) > (state.totalItemCount - PAGING_ITEMS_TO_END))
                && state.totalItemCount > PAGE_SIZE - 1 ){
            //Установим recyclerView, куда будет добавлять продукты подписчик
            defaultCarouselItem2 = state.carouselItem
            viewModel.refreshProductListByTag(tagId,  PAGE_SIZE)
        }
    }

    companion object{
        val PAGING_ITEMS_TO_END = 5
        val PAGE_SIZE = 10
    }

}