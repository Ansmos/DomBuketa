package ru.ansmos.dombuketa.dagger

import dagger.Component
import ru.ansmos.dombuketa.net_module.IRemoteProvider
import ru.ansmos.dombuketa.viewmodels.HomeViewModel
import javax.inject.Singleton

@Singleton
@Component(dependencies = [IRemoteProvider::class], modules = [DomainModule::class])
interface IAppComponent {
    fun inject(homeViewModel: HomeViewModel)
}