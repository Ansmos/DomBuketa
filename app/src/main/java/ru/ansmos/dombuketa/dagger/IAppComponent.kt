package ru.ansmos.dombuketa.dagger

import dagger.Component
import ru.ansmos.dombuketa.net_module.IRemoteProvider
import ru.ansmos.dombuketa.viewmodels.DeliveresViewModel
import ru.ansmos.dombuketa.viewmodels.DetailsProductViewModel
import ru.ansmos.dombuketa.viewmodels.HomeViewModel
import ru.ansmos.dombuketa.viewmodels.SettingsViewModel
import ru.dombuketa.database_module.dagger.IDatabaseProvider
import javax.inject.Singleton

@Singleton
@Component(dependencies = [IRemoteProvider::class, IDatabaseProvider::class], modules = [DomainModule::class])
interface IAppComponent {
    fun inject(homeViewModel: HomeViewModel)
    fun inject(deliversViewModel: DeliveresViewModel)
    fun inject(settingsViewModel: SettingsViewModel)
    fun inject(detailsProductViewModel: DetailsProductViewModel)
}