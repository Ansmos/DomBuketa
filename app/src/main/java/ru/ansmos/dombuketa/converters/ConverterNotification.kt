package ru.ansmos.dombuketa.converters

import io.reactivex.rxjava3.core.Observable
import ru.ansmos.dombuketa.db_module.entity.NotificationEntity
import ru.ansmos.dombuketa.models_bll.Notification
import java.time.LocalDateTime

object ConverterNotification {
    fun Entity_DTO(entity: NotificationEntity?) : Notification? {
        if (entity != null) {
            return Notification(
                id = entity.id,
                productId = entity.productId,
                name = entity.name,
                image = entity.image,
                description = entity.description,
                isActive = entity.isActive,
                notificationTime = LocalDateTime.of(entity.startYear, entity.startMonth,
                    entity.startDay, entity.startHour, entity.startMinute)
            )
        } else return null
    }

    fun Entity_DTO_ListRx(list: Observable<List<NotificationEntity>>?): Observable<List<Notification>> {
        if (list != null) {
            val result = list.map { notificationsEntityList ->
                val notificationsList = arrayListOf<Notification>()
                notificationsEntityList.forEach {
                    Entity_DTO(it)?.let { it1 -> notificationsList.add(it1) }
                }
                return@map notificationsList.toList()
            }
            return result
        } else return Observable.just(null)
    }

}