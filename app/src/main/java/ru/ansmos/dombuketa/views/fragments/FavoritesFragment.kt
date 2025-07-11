package ru.ansmos.dombuketa.views.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Scene
import androidx.transition.TransitionManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.ansmos.dombuketa.R
import ru.ansmos.dombuketa.databinding.FragmentDeliveresBinding
import ru.ansmos.dombuketa.databinding.FragmentFavoritesBinding
import ru.ansmos.dombuketa.helpers.AutoDisposable
import ru.ansmos.dombuketa.helpers.addTo
import ru.ansmos.dombuketa.viewmodels.DeliveresViewModel
import ru.ansmos.dombuketa.viewmodels.FavoritesViewModel
import ru.ansmos.dombuketa.views.rw.Product_H_Adapter

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private val autoDisposable = AutoDisposable()
    private lateinit var productAdapter : Product_H_Adapter

    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(FavoritesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        autoDisposable.bindTo(lifecycle)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Внедрение merge с прицелом на анимацию при переходе
        TransitionManager.go(
            Scene.getSceneForLayout(requireActivity()
            .findViewById(R.id.favorite_fragment_root),R.layout.catalog_merge, requireContext()))
        initRV()
        viewModel.refreshVisitedProducts()
        viewModel.productListVisited
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                productAdapter.addTags(it)
            },{
                Log.i("FD", "error ${it.message}")
            },{
                Log.i("FD", "onCompleted")
            })
            .addTo(autoDisposable)

    }

    private fun initRV() {
        val rv = binding.favoriteFragmentRoot.findViewById<RecyclerView>(R.id.deliver_recycler)

        rv.apply {
            productAdapter = Product_H_Adapter()
            adapter = productAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

}