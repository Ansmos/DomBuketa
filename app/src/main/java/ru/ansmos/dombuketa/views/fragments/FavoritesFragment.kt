package ru.ansmos.dombuketa.views.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.ansmos.dombuketa.databinding.FragmentFavoritesBinding
import ru.ansmos.dombuketa.helpers.AutoDisposable
import ru.ansmos.dombuketa.helpers.addTo
import ru.ansmos.dombuketa.models_bll.Product
import ru.ansmos.dombuketa.views.rw.utils.TouchHelper
import ru.ansmos.dombuketa.viewmodels.FavoritesViewModel
import ru.ansmos.dombuketa.views.MainActivity
import ru.ansmos.dombuketa.views.rw.Product_H_Adapter
import ru.ansmos.dombuketa.views.rw.Product_V_Adapter

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private val autoDisposable = AutoDisposable()
    private lateinit var productAdapterFav : Product_V_Adapter  //Избранные товары
    private lateinit var productAdapterVis : Product_H_Adapter  //Посещенные товары

    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(FavoritesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        autoDisposable.bindTo(lifecycle)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRVFav()
        initRVVis()

        viewModel.productListVisited
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                productAdapterVis.addItems(it)
            },{
                Log.i("Frag", "Visited: error ${it.message}")
            },{
                Log.i("FDrag", "Visited: onCompleted")
            })
            .addTo(autoDisposable)

        viewModel.productListfavorites
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                productAdapterFav.addTags(it)
            },{
                Log.i("Frag", "Visited: error ${it.message}")
            },{
                Log.i("FDrag", "Visited: onCompleted")
            })
            .addTo(autoDisposable)

//        viewModel.refreshVisitedFavoritesProducts(true)
//        viewModel.refreshVisitedFavoritesProducts(false)

//        val puul = binding.favoriteFragmentRoot.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)
//        puul.setOnRefreshListener {
//            puul.isRefreshing = false
//        }
    }

    private fun initRVFav() {
        val rv = binding.favoritesRecycler // favoriteFragmentRoot.findViewById<RecyclerView>(R.id.favorites_recycler)
        rv.apply {
            productAdapterFav = Product_V_Adapter(object : Product_V_Adapter.IOnItemClixkListener{
                override fun click(product: Product) {
                    (requireActivity() as MainActivity).launchDetailsFrag(product)
                }
            })
            adapter = productAdapterFav
            layoutManager = LinearLayoutManager(requireContext())
            //Удаление через смахивание
            val callback = TouchHelper(productAdapterFav)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(this)
        }
    }
    private fun initRVVis() {
        val rv = binding.visitedRecycler // findViewById<RecyclerView>(R.id.visited_recycler)
        rv.apply {
            productAdapterVis = Product_H_Adapter(object : Product_H_Adapter.IOnItemClixkListener{
                override fun click(product: Product) {
                    (requireActivity() as MainActivity).launchDetailsFrag(product)
                }
            })
            adapter = productAdapterVis
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        }
    }
}