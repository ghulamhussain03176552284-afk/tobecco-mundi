package com.example.data.model

enum class AppLanguage(val code: String, val displayName: String, val urduName: String) {
    URDU("ur", "اردو", "اردو"),
    ENGLISH("en", "English", "انگریزی")
}

enum class PostType(val labelEn: String, val labelUrdu: String) {
    SELL("For Sale", "برائے فروخت"),
    BUY("Wanted to Buy", "ضرورت برائے خریداری"),
    UPDATE("Mandi Report", "منڈی رپورٹ")
}

data class CommentItem(
    val id: String,
    val authorName: String,
    val authorRole: String,
    val text: String,
    val bidAmount: Int? = null, // In PKR per Maund if offer
    val timestamp: Long = System.currentTimeMillis()
)

data class TobaccoPost(
    val id: String,
    val authorName: String,
    val authorRole: String, // "زمیندار (Farmer)", "بیوپاری (Trader)", "آڑھتی (Commission Agent)"
    val authorCity: String, // "جام پور", "صوابی", "مردان", "چارسدہ", "یار حسین", "مانسہرہ", "ساہیوال"
    val postType: PostType,
    val cropVariety: String, // "Jampur Desi Patta", "Virginia FCV", "Rustica (Naswar)", "White Patta"
    val grade: String, // "Grade 1 (Top / اعلیٰ)", "Grade 2 (Medium)", "Grade 3 (Standard)"
    val quantityMaunds: Int, // e.g. 150 Maunds (من)
    val pricePerMaund: Int, // e.g. 14,500 PKR
    val moistureStatus: String, // "خشک اور صاف (Super Dry)", "معتدل (Normal)", "کچا مال"
    val description: String,
    val contactPhone: String,
    val timestamp: Long = System.currentTimeMillis(),
    val likesCount: Int = 0,
    val isLiked: Boolean = false,
    val comments: List<CommentItem> = emptyList(),
    val viewsCount: Int = 0,
    val sharesCount: Int = 0,
    val imageUrl: String? = null,
    val videoUrl: String? = null,
    val isVerifiedFarmer: Boolean = true,
    val isUrgent: Boolean = false
) {
    val totalEstimatedValue: Long
        get() = quantityMaunds.toLong() * pricePerMaund.toLong()
}
