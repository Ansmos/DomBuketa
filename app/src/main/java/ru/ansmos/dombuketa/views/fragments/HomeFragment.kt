package ru.ansmos.dombuketa.views.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.transition.*
import androidx.transition.Fade.IN
import androidx.transition.Fade.OUT
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.ansmos.dombuketa.App
import ru.ansmos.dombuketa.R
import ru.ansmos.dombuketa.converters.ConverterProductListByTag
import ru.ansmos.dombuketa.databinding.FragmentHomeBinding
import ru.ansmos.dombuketa.helpers.AutoDisposable
import ru.ansmos.dombuketa.helpers.addTo
import ru.ansmos.dombuketa.models_bll.Product
import ru.ansmos.dombuketa.viewmodels.HomeViewModel
import ru.ansmos.dombuketa.views.MainActivity
import ru.ansmos.dombuketa.views.rw.groupie.CarouselItem2
import ru.ansmos.dombuketa.views.rw.groupie.ItemCarousel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val autoDisposable = AutoDisposable()
    private lateinit var mainAdapterGroupie : GroupieAdapter
    // Переменная для понимания, в какой горизонтальный RV класть следущую страницу
    private var defaultCarouselItem2: CarouselItem2? = null

    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        autoDisposable.bindTo(lifecycle)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if( App.instance.dagger.getInteractor().getDarkModeFromPreferences()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        initAnimationEnter()
        initRV()
        initPullToRefresh()
        subscribeToProductListByTagListAll()
        subscribeToProductListByTag()
        subscribeToProgressBar()
        // Для опытов TODO
        binding.root.findViewById<ImageButton>(R.id.test_button).setOnClickListener {
            Log.i("onClick","Click")
        }

    }
    // Загрузка первой пачки данных для вложенных RV, согласно API
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
                defaultCarouselItem2?.addProducts(it)
            },{
                it.printStackTrace()
            })
            .addTo(autoDisposable)
    }

    private fun subscribeToProgressBar() {
        viewModel.showProgressBar
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.root.findViewById<ProgressBar>(R.id.progress_bar).isVisible = it
            },{
                it.printStackTrace()
            })
            .addTo(autoDisposable)
    }

    private fun initPullToRefresh() {
        val pull = binding.homeFragmentRoot.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)
        pull.setOnRefreshListener {
            mainAdapterGroupie.clear()
            viewModel.refreshProductListByTagListAll()
            subscribeToProductListByTagListAll()
            pull.isRefreshing = false
        }
    }

    private fun initAnimationEnter() {
        val scene = Scene.getSceneForLayout(requireActivity().findViewById(R.id.home_fragment_root),
            R.layout.home_merge, requireContext())
        //Также запускаем через TransitionManager, но вторым параметром передаем нашу кастомную анимацию
        //если это первый запуск
        val anim1 = AlphaAnimation(0.2F, 1F).apply {
            duration = 2000
            startOffset = 5000
            fillAfter = true
        }

        val a1 = Fade(OUT)
        val a2 = Fade(IN)
        val transition = TransitionSet().apply{
            //addTransition(a2)
            //duration = 5000
            //addTransition(a2)
        }

        if ((requireActivity() as MainActivity).firstStart) {
            TransitionManager.go(scene, transition)
            (requireActivity() as MainActivity).firstStart = false
        } else{
            TransitionManager.go(scene)
        }
    }

    private fun initRV() {
        val rv = binding.homeFragmentRoot.findViewById<RecyclerView>(R.id.main_recycler)
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
        viewModel.updateVisitedProduct(product)
        (requireActivity() as MainActivity).launchDetailsFrag(product)
    }

    fun onCarouselCardScroll(state: CarouselItem2.CarouselRVState, tagId: Int) {
        println("Fragm.Scroll: visPos=${state.visibleItemPos} (visCount=${state.visibleItemsCount}/total=${state.totalItemCount}), tagId=${tagId}")
        //Если до конца списка осталось 4 элемента и если кольчество элементов >= страницы, значит есть, что загружать
        if (((state.visibleItemsCount + state.visibleItemPos) > (state.totalItemCount - SettingsFragment.PAGING_ITEMS_TO_END))
            && state.totalItemCount > PAGE_SIZE - 1
            && state.totalItemCount % PAGE_SIZE == 0 ){     //Чтобы в последнем запросе, который возвращает меньше страницы не пытаться еще
            //Установим recyclerView, куда будет добавлять продукты подписчик
            defaultCarouselItem2 = state.carouselItem
            viewModel.refreshProductListByTag(tagId, SettingsFragment.PAGE_SIZE)
        }
    }

   companion object{
        val PAGING_ITEMS_TO_END = 5  //Сколько осталось Итемов до подгрузки следущей страницы
        val PAGE_SIZE = 10
   }
}