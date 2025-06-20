package ru.ansmos.dombuketa.models_bll

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val id: Int,
    val order: Int,
    val path: String,
    val fileName: String
): Parcelable