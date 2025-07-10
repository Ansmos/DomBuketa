package ru.dombuketa.database_module.dagger

import android.content.Context

interface IContextProvider {
    fun provideContext() : Context
}