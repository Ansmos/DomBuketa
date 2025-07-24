package ru.dombuketa.database_module.dagger

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.ansmos.dombuketa.db_module.dao.INotificationDao
import ru.ansmos.dombuketa.db_module.dao.IProductLiteDao
import ru.ansmos.dombuketa.db_module.db.AppDatabase
import ru.ansmos.dombuketa.db_module.entity.ProductLiteEntity
import ru.ansmos.dombuketa.db_module.repo.MainRepository

import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideProductLiteDao(context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, "dombuketa.db")
        .build().productLiteDao()

    @Singleton
    @Provides
    fun provideRepository(iProductLiteDao: IProductLiteDao, iNotificationDao: INotificationDao)
        = MainRepository(iProductLiteDao, iNotificationDao)

    @Singleton
    @Provides
    fun provideNotificationDao(context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, "dombuketa.db")
        .build().notificationDao()
}

