package ru.ansmos.dombuketa

import android.app.Application
import android.content.Context
import ru.ansmos.dombuketa.dagger.DaggerIAppComponent
import ru.ansmos.dombuketa.dagger.DomainModule
import ru.ansmos.dombuketa.dagger.IAppComponent
import ru.ansmos.dombuketa.net_module.dagger.DaggerIRemoteComponent
import ru.ansmos.dombuketa.services.NotificationHelper
import ru.dombuketa.database_module.dagger.DaggerIDatabaseComponent
import ru.dombuketa.database_module.dagger.IContextProvider

class App : Application(), IContextProvider {
    lateinit var dagger : IAppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        val databaseProvider = DaggerIDatabaseComponent.builder().iContextProvider(provideContext() as IContextProvider).build()
        dagger = DaggerIAppComponent.builder()
            .domainModule(DomainModule(this))
            .iRemoteProvider(DaggerIRemoteComponent.create())
            .iDatabaseProvider(databaseProvider)
            .build()
        //Создаем канал
        NotificationHelper.createChannel(this)
    }

    companion object{
        lateinit var instance: App
        private set
    }

    override fun provideContext(): Context = this
}