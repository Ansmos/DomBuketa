package ru.ansmos.dombuketa.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import java.util.*

class PreferenceProvider(context: Context) {
    //Нам нужен контекст приложения
    private val appContext = context.applicationContext
    //Создаем экземпляр SharedPreferences
    private val preference: SharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE)
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener  //Будем прикручивать листенер для обновления набора данных
    var darkMode = MutableLiveData<Boolean>()

    init {
        //Логика для первого запуска приложения, чтобы положить наши настройки,
        //Сюда потом можно добавить и другие настройки
        if (preference.getBoolean(KEY_FIRST_LAUNCH, false)){
            //Установим светлый режим при первом запуске
            preference.edit {
                putBoolean(KEY_DARK_MODE, false)
            }
            preference.edit {
                putBoolean(KEY_FIRST_LAUNCH, false)
            }
        }
        initSharedPreferecncesListener()
    }

    private fun initSharedPreferecncesListener() {
        listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            when (key) {
                // Если меняем текущую категорию
                KEY_DARK_MODE -> darkMode.setValue(getDefNightMode())
            }
        }
        preference.registerOnSharedPreferenceChangeListener(listener)
    }

    //Получаем статус ночного режима
    fun getDefNightMode(): Boolean {
        return preference.getBoolean(KEY_DARK_MODE, false)
    }
    //Сохраняем статус ночного режима
    fun setDefNightMode(mode: Boolean){
        preference.edit {
            putBoolean(KEY_DARK_MODE, mode)
        }
    }

    companion object {
        const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_FIRST_LAUNCH = "first_launch"

        private const val LAST_SUCCESS_UPLOAD = "last_success_upload"
    }

}