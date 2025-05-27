package ru.ansmos.dombuketa.views.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.ansmos.dombuketa.R
import ru.ansmos.dombuketa.viewmodels.DeliveresViewModel

class DeliveresFragment : Fragment() {

    companion object {
        fun newInstance() = DeliveresFragment()
    }

    private lateinit var viewModel: DeliveresViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_deliveres, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DeliveresViewModel::class.java)
        // TODO: Use the ViewModel
    }

}