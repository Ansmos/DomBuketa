package ru.ansmos.dombuketa.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupieAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.ansmos.dombuketa.R
import ru.ansmos.dombuketa.converters.ConverterProductListByTag
import ru.ansmos.dombuketa.databinding.FragmentSettingsBinding
import ru.ansmos.dombuketa.helpers.AutoDisposable
import ru.ansmos.dombuketa.helpers.addTo
import ru.ansmos.dombuketa.models_bll.Product
import ru.ansmos.dombuketa.viewmodels.SettingsViewModel
import ru.ansmos.dombuketa.views.MainActivity
import ru.ansmos.dombuketa.views.rw.groupie.*

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val autoDisposable = AutoDisposable()
    lateinit var mainAdapterGroupie : GroupieAdapter

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
        subscribeToProductListByTag()
        initRV()
    }


    private fun subscribeToProductListByTagListAll(){
        viewModel.productListByTagListAll
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                //Передаем в ItemCarousel обработчики нажатий
                ConverterProductListByTag.DTOList_ItemCarouselList(
                    it, ::onCarouselCardClick, ::onCarouselCardScroll, ::onProductItemClick)
            }
            .subscribe({
                mainAdapterGroupie.addAll(it)
            })
            .addTo(autoDisposable)
    }

    // Пустая процедура потому что пример с habr не позволяет менять уже загруженную пачку,
    // хорош только для статического ьпримера или для написания статьи
    private fun subscribeToProductListByTag() {
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
    //Click по товару
    fun onProductItemClick(product: Product, pos: Int) {
        println("onProductItemClick: Id=${product.id} - ${product.name}, price=${product.price.price}")
        (requireActivity() as MainActivity).launchDetailsFrag(product)
    }
    //Прокрутка
    fun onCarouselCardScroll(state: ItemCarousel.CarouselRVState, tagId: Int) {
        println("onCarouselCardScroll ${state.visibleItemPos} (${state.visibleItemsCount}/${state.totalItemCount}), tagId=${tagId}")
        if ((state.visibleItemsCount + state.visibleItemPos) > (state.totalItemCount - HomeFragment.PAGING_ITEMS_TO_END)){
            //TODO Сделать проверку на страницы? увязку с количеством элементов, загружаемых api
            viewModel.refreshProductListByTag(tagId, 10)
        }
    }

    companion object{
        val PAGING_ITEMS_TO_END = 5
        val PAGE_SIZE = 10
    }

}