package ru.ansmos.dombuketa.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ansmos.dombuketa.App
import ru.ansmos.dombuketa.domain.Interactor
import javax.inject.Inject

class SetViewModel : ViewModel() {
    //Инжектим интерактор
    @Inject
    lateinit var interactor: Interactor
    val darkModeLiveData: MutableLiveData<Boolean> = MutableLiveData()

    init {
        App.instance.dagger.inject(this)
        //Получаем категорию при инициализации, чтобы у нас сразу подтягивалась категория
        getDarkMode()
    }

    private fun getDarkMode() {
        //Кладем ночной режит в LiveData
        darkModeLiveData.value = interactor.getDarkModeFromPreferences()
    }

    fun putDarkMode(darkMode: Boolean){
        //Сохраняем в настройки
        interactor.saveDarkModeToPreferences(darkMode)
        //И сразу забираем, чтобы сохранить состояние в модели
        getDarkMode()
    }}