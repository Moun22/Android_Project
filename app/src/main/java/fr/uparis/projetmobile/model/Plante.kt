package fr.uparis.projetmobile.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.android.parcel.Parcelize
import java.sql.Date

@Parcelize
@Entity(tableName = "info_plante")
data class Plante (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nomCommun: String,
    val nomLatin : String,
    val frequance1: Int,
    val frequance2: Int,
    val dateprochain: Date,
    val photo: String
    ): Parcelable

