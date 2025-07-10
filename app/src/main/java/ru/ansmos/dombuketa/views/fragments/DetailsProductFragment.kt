package ru.ansmos.dombuketa.views.fragments

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.transition.Fade
import androidx.transition.Slide
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import ru.ansmos.dombuketa.R
import ru.ansmos.dombuketa.databinding.FragmentProductDetailsBinding
import ru.ansmos.dombuketa.helpers.AutoDisposable
import ru.ansmos.dombuketa.helpers.addTo
import ru.ansmos.dombuketa.models_bll.Product
import ru.ansmos.dombuketa.net_module.ApiConstants
import ru.ansmos.dombuketa.viewmodels.DetailsProductViewModel

class DetailsProductFragment : Fragment() {
    private lateinit var binding : FragmentProductDetailsBinding
    private val autoDisposable = AutoDisposable()
    private val  viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(DetailsProductViewModel::class.java)
    }
    lateinit var product: Product
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        enterTransition = Slide(Gravity.END).apply { duration = 500 }
        returnTransition = Fade() //Slide(Gravity.END).apply { duration = 500; mode = Slide.MODE_OUT }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        autoDisposable.bindTo(lifecycle)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        product = arguments?.get("product") as Product
        viewModel.isProductInFavorites(product.id)
            .observeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                product.isInFavorites = it
            },{
                it.printStackTrace()
            })
            .addTo(autoDisposable)

        initFabs()
        binding.detailsToolbar.title = product.name
        Glide.with(this)
            .load(ApiConstants.IMAGES_URL + product.imageCart.path + product.imageCart.fileName)
            .centerCrop()
            .into(binding.detailsPoster)
        binding.detailsDescription.text = product.description
    }


    private fun initFabs() {
        val toolbarlayout = requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator_layout)
        for (i in 0..toolbarlayout.childCount - 1)
        {
            val  v = toolbarlayout.get(i)
            if (v is FloatingActionButton){

                val snackbar = Snackbar.make(requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator_layout),
                    v.accessibilityPaneTitle.toString(), Snackbar.LENGTH_LONG)
                snackbar.setAction("Click"){
                    Toast.makeText(requireContext(), v.accessibilityPaneTitle.toString(), Toast.LENGTH_SHORT).show()
                }
                snackbar.setActionTextColor(
                    ContextCompat.getColor(requireContext(),
                    R.color.purple_500
                ))
                v.setOnClickListener {
                    snackbar.show()
                }
            }
        }

        requireActivity().findViewById<FloatingActionButton>(R.id.fav_fab).apply {
            setImageResource(
                if (product.isInFavorites)
                    R.drawable.ic_favorite_24
                else
                    R.drawable.ic_favorite_border_24
            )
            setOnClickListener {
                if (!product.isInFavorites){
                    (it as FloatingActionButton).setImageResource(R.drawable.ic_favorite_24)
                    product.isInFavorites = true
                } else {
                    (it as FloatingActionButton).setImageResource(R.drawable.ic_favorite_border_24)
                    product.isInFavorites = false
                }
                viewModel.updateVisitedProduct(product)
            }
        }
        requireActivity().findViewById<FloatingActionButton>(R.id.share_fab).setOnClickListener {
            //Создаем интент
            val intent = Intent()
            //Указываем action с которым он запускается
            intent.action = Intent.ACTION_SEND
            //Кладем данные о нашем фильме
            intent.putExtra(Intent.EXTRA_TEXT,"Посмотри [Дом букета] это: ${product.name} \n\n ${product.description}")
            //Указываем MIME тип, чтобы система знала, какое приложения предложить
            intent.type = "text/plain"
            //Запускаем наше активити
            startActivity(Intent.createChooser(intent, "Отправить к:"))
        }
    }
}

private fun String.handleSingleQuote(): String = this.replace("'", "")
