package com.example.data.model

data class MandiRate(
    val id: String,
    val mandiName: String, // e.g. "صوابی منڈی (Swabi)", "مردان منڈی (Mardan)"
    val variety: String, // e.g. "Virginia FCV (اعلیٰ ورجینیا)"
    val grade: String, // "Grade 1", "Grade 2"
    val averageRate: Int, // PKR per Maund (40 kg)
    val minRate: Int,
    val maxRate: Int,
    val changeTrend: Int, // e.g. +350 (Positive green), -150 (Negative red), 0
    val totalVolumeMaunds: Int, // volume traded today
    val status: String, // "نیلامی جاری ہے", "آج کی نیلامی مکمل"
    val updatedTime: String
)

data class MandiStory(
    val id: String,
    val mandiName: String,
    val headline: String,
    val rateText: String,
    val isLive: Boolean = true,
    val authorName: String
)
