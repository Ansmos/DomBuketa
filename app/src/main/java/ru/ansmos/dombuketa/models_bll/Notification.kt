package ru.ansmos.dombuketa.models_bll

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.ansmos.dombuketa.db_module.entity.NotificationEntity
import java.time.LocalDateTime

@Parcelize
class Notification(
    var id: Int = 0,
    val productId: Int,
    val name: String,
    val image: String,
    val description: String,
    var notificationTime: LocalDateTime,
    var isActive: Boolean = true
) : Parcelable {
    // ДЛя нотификаций
    fun toProduct() : Product{
        return Product(
            id = this.productId,
            price = Price(0,0.0,0.0,0.0,0.0,false),
            name = this.name,
            imageCart = Image(0,0, "", this.image),  //Хитрю, чтобы отлельно путь и имя файла не разбирать, все равно они сложатся)
            imageGalary = null,
            description = this.description,
            isInFavorites = false
        )
    }
    fun toEntity(): NotificationEntity{
        return NotificationEntity(
            id = this.id,
            productId = this.productId,
            name = this.name,
            image = this.image,
            description = this.description,
            startYear = this.notificationTime.year,
            startMonth = this.notificationTime.monthValue,
            startDay = this.notificationTime.dayOfMonth,
            startHour = this.notificationTime.hour,
            startMinute = this.notificationTime.minute,
            isActive = this.isActive
        )
    }
}