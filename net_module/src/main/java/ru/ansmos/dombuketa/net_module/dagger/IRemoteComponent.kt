package ru.ansmos.dombuketa.net_module.dagger

import dagger.Component
import ru.ansmos.dombuketa.net_module.IRemoteProvider
import javax.inject.Singleton

@Singleton
@Component(modules = [RemoteModule::class])
interface IRemoteComponent: IRemoteProvider {
}