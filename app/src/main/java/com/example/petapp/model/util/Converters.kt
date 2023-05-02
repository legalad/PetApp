package com.example.petapp.model.util

import androidx.room.TypeConverter
import com.example.petapp.model.Species
import java.time.Instant
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