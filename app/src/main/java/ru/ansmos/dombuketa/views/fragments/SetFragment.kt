package ru.ansmos.dombuketa.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.ansmos.dombuketa.R
import ru.ansmos.dombuketa.databinding.FragmentSetBinding
import ru.ansmos.dombuketa.utils.AnimationHelper
import ru.ansmos.dombuketa.viewmodels.SetViewModel

class SetFragment : Fragment() {

    private lateinit var binding: FragmentSetBinding
    private val  viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(SetViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Подключаем анимации и передаем номер позиции у кнопки в нижнем меню
        AnimationHelper.performFragmentCircularRevealAnimation(binding.settingsFragmentRoot, requireActivity(), 5)
        //Слушаем, какой у нас сейчас выбран вариант в настройках
        val switchDarkContainer = requireActivity().findViewById<ViewGroup>(R.id.switch_dark_mode)
        val switchDark = switchDarkContainer.findViewById<SwitchCompat>(R.id.the_switch)
        val textDark = switchDarkContainer.findViewById<TextView>(R.id.the_switch_text)
        //Установим переключатель в правильное положение
        switchDark.isChecked = viewModel.interactor.getDarkModeFromPreferences()

        viewModel.darkModeLiveData.observe(viewLifecycleOwner, {
            textDark.text = getResources().getString(R.string.fragment_set_text_darkmode)
            when (it){
                true -> {
                    setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                false -> {
                    setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        })
        //Слушатель для отправки нового состояния в настройки
        switchDark.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(button: CompoundButton?, darkMode: Boolean) {
                viewModel.putDarkMode(darkMode)
            }
        })

        //Слушатель на кнопку удаления кеша 39*
        binding.buttonClearVisited.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Очистим список промотренных в
                viewModel.interactor.clearVisited()
                Toast.makeText(requireContext(),
                    getResources().getString(R.string.fragment_set_text_warning),
                    Toast.LENGTH_LONG).show()
            }
        })
    }
}