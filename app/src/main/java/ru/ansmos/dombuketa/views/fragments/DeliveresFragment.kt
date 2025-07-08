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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.transition.Scene
import androidx.transition.TransitionManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.ansmos.dombuketa.R
import ru.ansmos.dombuketa.databinding.FragmentDeliveresBinding
import ru.ansmos.dombuketa.helpers.AutoDisposable
import ru.ansmos.dombuketa.helpers.addTo
import ru.ansmos.dombuketa.viewmodels.DeliveresViewModel
import ru.ansmos.dombuketa.views.rw.Product_H_Adapter

class DeliveresFragment : Fragment() {
    private lateinit var binding: FragmentDeliveresBinding
    private val autoDisposable = AutoDisposable()
    private lateinit var productAdapter : Product_H_Adapter

    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(DeliveresViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDeliveresBinding.inflate(inflater, container, false)
        autoDisposable.bindTo(lifecycle)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Внедрение merge с прицелом на анимацию при переходе
        TransitionManager.go(Scene.getSceneForLayout(requireActivity()
            .findViewById(R.id.delivers_fragment_root),R.layout.catalog_merge, requireContext()))
        initRV()
        initPullToRefresh()
        viewModel.productListByTag
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

    private fun initPullToRefresh() {
        val pull = binding.deliversFragmentRoot.findViewById<SwipeRefreshLayout>(R.id.deliver_swipe_refresh)
        pull.setOnRefreshListener {
            productAdapter.clearItems()
            viewModel.refreshProductss()
            pull.isRefreshing = false
        }
    }

    private fun initRV() {
        val rv = binding.deliversFragmentRoot.findViewById<RecyclerView>(R.id.deliver_recycler)

        rv.apply {
            productAdapter = Product_H_Adapter()
            adapter = productAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}