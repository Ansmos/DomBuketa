package ru.ansmos.dombuketa.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.ansmos.dombuketa.R
import ru.ansmos.dombuketa.databinding.ActivityMainBinding
import ru.ansmos.dombuketa.models_bll.Product
import ru.ansmos.dombuketa.views.fragments.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var firstStart: Boolean = true
    var defaultFragmentTag: String = ""
    var previoustFragmentTag: String = ""  //Для фракмента с деталями, неизвестно, из какого фрагмента он вызван

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Вызываем главный фрагмент
        initNavMenu()



        // Проверяем, если есть Товар в интенте от нотификации
        val productFromNotification = intent.getParcelableExtra<Product>("product")
        // Если есть, запускаем детальный фрагмент, если нет, - основной
        if (productFromNotification != null){
            launchDetailsFrag(productFromNotification)
        } else {
            // запускаем фрагмент при окончании анимации
            val tag = "home"
            val fragment = checkFragExist(tag)
            changeFrag(fragment?: HomeFragment(), tag)
        }
        Toast.makeText(this,
            Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID).toString(),
            Toast.LENGTH_LONG).show()
    }

    fun initNavMenu(){
        binding.bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.nav_nemu_home -> {
                    val tag = "home"
                    val fragment = checkFragExist(tag)
                    changeFrag(fragment?: HomeFragment(), tag)
                    true
                }
                R.id.nav_nemu_favorites ->{
                    val tag = "fav"
                    val fragment = checkFragExist(tag)
                    changeFrag(fragment?: FavoritesFragment(), tag)
                    true
                }
                R.id.nav_nemu_shopping_cart ->{
                    val tag = "shoppingcart"
                    val fragment = checkFragExist(tag)
                    changeFrag(fragment?: ShoppingCartFragment(), tag)
                    true
                }
                R.id.nav_nemu_deliver ->{
                    val tag = "deliver"
                    val fragment = checkFragExist(tag)
                    changeFrag(fragment?: DeliveresFragment(), tag)
                    true
                }
                R.id.nav_nemu_settings ->{
                    val tag = "settings"
                    val fragment = checkFragExist(tag)
                    changeFrag(fragment?: SettingsFragment(), tag)
                    true
                }
                else -> false
            }
        }
    }

    private fun checkFragExist(tag: String): Fragment? {
        return supportFragmentManager.findFragmentByTag(tag)
    }

    private fun changeFrag(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_placeholder, fragment, tag)
            .addToBackStack(null).commit()
        defaultFragmentTag = tag
    }

    fun launchDetailsFrag(product: Product) {
        //Создаем "посылку"
        val bundle = Bundle()
        bundle.putParcelable("product", product)
        val fragment = DetailsProductFragment()
        //Прикрепляем нашу "посылку" к фрагменту
        fragment.arguments = bundle
        //Запускаем фрагмент
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment)
            .addToBackStack(null).commit()
        previoustFragmentTag = defaultFragmentTag
        defaultFragmentTag = "details_product"

    }

}