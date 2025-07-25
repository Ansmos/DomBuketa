package ru.dombuketa.database_module.dagger

import ru.ansmos.dombuketa.db_module.dao.INotificationDao
import ru.ansmos.dombuketa.db_module.dao.IProductLiteDao


interface IDatabaseProvider {
    fun provideDatabase() : IProductLiteDao
    fun provideNotifications() : INotificationDao
}