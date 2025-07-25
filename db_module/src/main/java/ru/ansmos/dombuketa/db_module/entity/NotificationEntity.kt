package ru.ansmos.dombuketa.db_module.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "notifications", indices = [Index(
    value = ["product_id"],
    unique = true
)])
data class NotificationEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "product_id") val productId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "start_year") val startYear: Int,
    @ColumnInfo(name = "start_month") val startMonth: Int,
    @ColumnInfo(name = "start_day") val startDay: Int,
    @ColumnInfo(name = "start_hour") val startHour: Int,
    @ColumnInfo(name = "start_minute") val startMinute: Int,
    @ColumnInfo(name = "is_active", defaultValue = "1") var isActive: Boolean = true
) : Parcelable