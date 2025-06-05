package ru.ansmos.dombuketa

import android.app.Application
import ru.ansmos.dombuketa.dagger.DaggerIAppComponent
import ru.ansmos.dombuketa.dagger.DomainModule
import ru.ansmos.dombuketa.dagger.IAppComponent
import ru.ansmos.dombuketa.net_module.dagger.DaggerIRemoteComponent

class App : Application() {
    lateinit var dagger : IAppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        dagger = DaggerIAppComponent.builder()
            .domainModule(DomainModule(this))
            .iRemoteProvider(DaggerIRemoteComponent.create())
            .build()
    }

    companion object{
        lateinit var instance: App
        private set
    }
}