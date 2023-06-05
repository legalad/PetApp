package com.example.petapp.model.util

import android.net.Uri
import androidx.room.TypeConverter
import com.example.petapp.data.MealType
import com.example.petapp.model.Species
import java.time.Instant
import java.time.OffsetTime
import java.util.*

class DateConverter {
    companion object {

        @TypeConverter
        @JvmStatic
        fun fromInstantToTimestamp(instant: Instant): Long {
            return instant.toEpochMilli()
        }

        @TypeConverter
        @JvmStatic
        fun fromTimestampToInstant(timestamp: Long): Instant {
            return Instant.ofEpochMilli(timestamp)
        }

        @TypeConverter
        @JvmStatic
        fun fromOffsetTimeToTime(offsetTime: OffsetTime): String {
            return offsetTime.toString()
        }

        @TypeConverter
        @JvmStatic
        fun fromStringToOffsetTime (sting: String): OffsetTime {
            return OffsetTime.parse(sting)
        }
    }
}

class UUIDConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromUUIDToString(uuid: UUID?): String? {
            return uuid?.toString()
        }

        @TypeConverter
        @JvmStatic
        fun fromStringToUUID(string: String?): UUID? {
            return string?.let { UUID.fromString(it) }
        }
    }
}

class URIConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromUriToString(uri: Uri?): String? {
            return uri?.toString()
        }

        //TODO handle exceptions
        @TypeConverter
        @JvmStatic
        fun fromStringToUri(string: String?): Uri? {
            return string?.let { Uri.parse(string) }
        }

    }
}

class EnumConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromSpeciesToString(species: Species): String {
            return species.name
        }

        @TypeConverter
        @JvmStatic
        fun fromStringToSpecies(string: String): Species {
            return Species.valueOf(string)
        }

        @TypeConverter
        @JvmStatic
        fun fromMealTypeToString(type: MealType): String {
            return type.name
        }

        @TypeConverter
        @JvmStatic
        fun fromStringToMealType(string: String): MealType {
            return MealType.valueOf(string)
        }
    }
}