package jianmoweather.data.db

import androidx.room.TypeConverter
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class TipInfoTypeConverters {

    @TypeConverter
    fun stringToStringArray(input: String): ArrayList<String> {
        val result = ArrayList<String>()
        val tokenizer = StringTokenizer(input, ",")
        while (tokenizer.hasMoreElements()) {
            val item = tokenizer.nextToken()
            try {
                result.add(item)
            } catch (ex: NumberFormatException) {
                Timber.e("ROOM Malformed String list $ex")
            }
        }
        return result
    }

    @TypeConverter
    fun stringArrayToString(input: ArrayList<String>): String {
        val size = input.size
        if (size == 0) return ""
        val sb = StringBuilder()
        for (i in 0 until size) {
            sb.append(input[i])
            if (i < size - 1) sb.append(",")
        }
        return sb.toString()
    }
}
