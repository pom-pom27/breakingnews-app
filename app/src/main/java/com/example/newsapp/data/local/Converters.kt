package com.example.newsapp.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.newsapp.data.model.Source
import com.example.newsapp.utils.JsonParser
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@ProvidedTypeConverter
class Converters(val jsonParser: JsonParser? = null) {

    @TypeConverter
    fun fromTimestampToDate(value: Long?): LocalDateTime? {
        return value?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
        }
    }

    @TypeConverter
    fun toTimestampFromDate(localDateTime: LocalDateTime?): Long? {
        return localDateTime?.toEpochSecond(ZoneOffset.UTC)
    }

    @TypeConverter
    fun fromSourceToString(source: Source): String {
        return source.name ?: "-"
    }

    @TypeConverter
    fun toSourceFromString(name: String): Source {
        return Source(name, name)
    }
}
