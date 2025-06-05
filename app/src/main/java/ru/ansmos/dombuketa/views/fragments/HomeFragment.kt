package ru.ansmos.dombuketa.views.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.transition.*
import androidx.transition.Fade.IN
import androidx.transition.Fade.OUT
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.ansmos.dombuketa.R
import ru.ansmos.dombuketa.databinding.FragmentHomeBinding
import ru.ansmos.dombuketa.helpers.AutoDisposable
import ru.ansmos.dombuketa.helpers.addTo
import ru.ansmos.dombuketa.viewmodels.HomeViewModel
import ru.ansmos.dombuketa.views.MainActivity
import ru.ansmos.dombuketa.views.rw.TagAdapter

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val autoDisposable = AutoDisposable()
    private lateinit var tagAdapter : TagAdapter

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
        initAnimationEnter()
        initRV()
        initPullToRefresh()
        viewModel.tagList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                tagAdapter.addTags(it)
            },{
                Log.i("FH", "error ${it.message}")
            },{
                Log.i("FH", "onCompleted")
            })
            .addTo(autoDisposable)
    }

    private fun initPullToRefresh() {
        val pull = binding.homeFragmentRoot.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)
        pull.setOnRefreshListener {
            tagAdapter.clearItems()
            viewModel.refreshTags()
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
            tagAdapter = TagAdapter()
            adapter = tagAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}