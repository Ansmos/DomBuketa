package ru.ansmos.dombuketa.db_module.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.ansmos.dombuketa.db_module.dao.INotificationDao
import ru.ansmos.dombuketa.db_module.dao.IProductLiteDao
import ru.ansmos.dombuketa.db_module.entity.NotificationEntity
import ru.ansmos.dombuketa.db_module.entity.ProductLiteEntity

@Database(entities = [ProductLiteEntity::class, NotificationEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase()  {
    abstract fun productLiteDao() : IProductLiteDao
    abstract fun notificationDao(): INotificationDao
}