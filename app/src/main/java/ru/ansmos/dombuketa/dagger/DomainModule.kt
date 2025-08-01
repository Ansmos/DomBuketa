package ru.ansmos.dombuketa.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.ansmos.dombuketa.db_module.repo.MainRepository
import ru.ansmos.dombuketa.domain.Interactor
import ru.ansmos.dombuketa.net_module.api.IDomBuketaApi2
import ru.ansmos.dombuketa.services.NotificationHelper
import ru.ansmos.dombuketa.utils.PreferenceProvider
import javax.inject.Singleton

@Module
class DomainModule(val context: Context) {
    @Provides
    fun provideContext() = context

    @Singleton
    @Provides
    fun provideInteractor(domBuketaApi2: IDomBuketaApi2,
                          repo: MainRepository,
                          preferenceProvider: PreferenceProvider) = Interactor(domBuketaApi2, repo, preferenceProvider)

    @Singleton
    @Provides
    fun provideNotificationHelper() = NotificationHelper

    @Singleton
    @Provides
    fun providePreferenceProvider(context: Context) = PreferenceProvider(context)
}