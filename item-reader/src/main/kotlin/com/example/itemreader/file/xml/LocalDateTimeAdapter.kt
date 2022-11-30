package com.example.itemreader.file.xml

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.xml.bind.annotation.adapters.XmlAdapter

/**
 * 일시 문자열 <-> LocalDateTime 컨버터
 */
class LocalDateTimeAdapter : XmlAdapter<String, LocalDateTime>() {
    companion object {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }

    override fun unmarshal(v: String?): LocalDateTime? {
        return v?.run { LocalDateTime.parse(v, formatter) }
    }

    override fun marshal(v: LocalDateTime?): String {
        return v?.format(formatter) ?: ""
    }
}
