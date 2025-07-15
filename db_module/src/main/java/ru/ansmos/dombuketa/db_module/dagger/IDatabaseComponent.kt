package ru.dombuketa.database_module.dagger

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [IContextProvider::class], modules = [DatabaseModule::class])
interface IDatabaseComponent : IDatabaseProvider {
}