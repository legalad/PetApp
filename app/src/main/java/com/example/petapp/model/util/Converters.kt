package com.example.petapp.model.util

import androidx.room.TypeConverter
import com.example.petapp.model.Species
import java.util.*

class DateConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromTimestampToDate(timestamp: Long): Date {
            return Date(timestamp)
        }
        @TypeConverter
        @JvmStatic
        fun fromDateToTimestamp(date: Date): Long {
            return date.time
        }
    }
}

class UUIDConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromUIDToString(uuid: UUID): String {
            return uuid.toString()
        }

        @TypeConverter
        @JvmStatic
        fun fromStringToUUID(string: String): UUID {
            return UUID.fromString(string)
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
    }
}