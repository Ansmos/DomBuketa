package ru.ansmos.dombuketa.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.ansmos.dombuketa.domain.Interactor
import ru.ansmos.dombuketa.net_module.api.IDomBuketaApi2
import javax.inject.Inject
import javax.inject.Singleton

@Module
class DomainModule(val context: Context) {
    @Provides
    fun provideContext() = context

    @Singleton
    @Provides
    fun provideInteractor(domBuketaApi2: IDomBuketaApi2) = Interactor(domBuketaApi2)
}