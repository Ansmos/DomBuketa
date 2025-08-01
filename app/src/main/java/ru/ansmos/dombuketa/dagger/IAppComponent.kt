package ru.ansmos.dombuketa.dagger

import dagger.Component
import ru.ansmos.dombuketa.domain.Interactor
import ru.ansmos.dombuketa.net_module.IRemoteProvider
import ru.ansmos.dombuketa.services.NotificationHelper
import ru.ansmos.dombuketa.viewmodels.*
import ru.dombuketa.database_module.dagger.IDatabaseProvider
import javax.inject.Singleton

@Singleton
@Component(dependencies = [IRemoteProvider::class, IDatabaseProvider::class], modules = [DomainModule::class])
interface IAppComponent {
    fun inject(homeViewModel: HomeViewModel)
    fun inject(favoritesViewModel: FavoritesViewModel)
    fun inject(deliversViewModel: DeliveresViewModel)
    fun inject(settingsViewModel: SettingsViewModel)
    fun inject(setViewModel: SetViewModel)
    fun inject(detailsProductViewModel: DetailsProductViewModel)
    fun inject(laterViewModel: LaterViewModel)

    //Для разнообразия таким способом
    fun getNotificationHelper() : NotificationHelper
    fun getInteractor() : Interactor
}