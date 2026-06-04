package com.ladystoneco.plangym.domain.model

enum class Priority(val level: Int) {
    LOW(0),
    MEDIUM(1),
    HIGH(2),
    URGENT(3);

    val label: String
        get() = when (this) {
            LOW -> "کم"
            MEDIUM -> "متوسط"
            HIGH -> "زیاد"
            URGENT -> "فوری"
        }
}