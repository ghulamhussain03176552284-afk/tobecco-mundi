package com.example.ui.utils

import com.example.data.model.AppLanguage

object LanguageStrings {
    fun appTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.URDU -> "تمباکو منڈی"
        AppLanguage.ENGLISH -> "Tobacco Mandi"
    }

    fun appSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.URDU -> "جام پور، صوابی اور مردان کی آسان منڈی"
        AppLanguage.ENGLISH -> "Easy Marketplace for Farmers & Traders"
    }

    // Tabs - Simple 4 tabs
    fun tabFeed(lang: AppLanguage): String = if (lang == AppLanguage.URDU) "ہوم (Home)" else "Home"
    fun tabRates(lang: AppLanguage): String = if (lang == AppLanguage.URDU) "آج کے ریٹ" else "Rates"
    fun tabCalculator(lang: AppLanguage): String = if (lang == AppLanguage.URDU) "حساب کتاب" else "Calculator"
    fun tabProfile(lang: AppLanguage): String = if (lang == AppLanguage.URDU) "رابطہ و مدد" else "Help & Profile"

    // Prompt Card
    fun promptTitle(lang: AppLanguage): String = if (lang == AppLanguage.URDU)
        "تمباکو بیچنا یا خریدنا ہے؟"
    else
        "Want to buy or sell tobacco?"

    fun promptButton(lang: AppLanguage): String = if (lang == AppLanguage.URDU)
        "➕ نیا اشتہار لگائیں (Post Now)"
    else
        "➕ Post New Ad"

    fun promptSell(lang: AppLanguage): String = if (lang == AppLanguage.URDU) "🌾 بیچنا ہے (Sell)" else "Sell"
    fun promptBuy(lang: AppLanguage): String = if (lang == AppLanguage.URDU) "🛒 خریدنا ہے (Buy)" else "Buy"

    // Card Details
    fun cropVariety(lang: AppLanguage): String = if (lang == AppLanguage.URDU) "فصل" else "Crop"
    fun lotSize(lang: AppLanguage): String = if (lang == AppLanguage.URDU) "وزن" else "Quantity"
    fun ratePerMaund(lang: AppLanguage): String = if (lang == AppLanguage.URDU) "فی من ریٹ" else "Rate/Maund"
    fun totalValue(lang: AppLanguage): String = if (lang == AppLanguage.URDU) "کل رقم" else "Total Price"
    fun viewsLabel(lang: AppLanguage): String = if (lang == AppLanguage.URDU) "دیکھا گیا" else "views"
    fun sharesLabel(lang: AppLanguage): String = if (lang == AppLanguage.URDU) "شیئر" else "shares"

    // Actions
    fun actionLike(lang: AppLanguage, isLiked: Boolean): String = if (lang == AppLanguage.URDU) {
        if (isLiked) "پسند ہے" else "پسند"
    } else {
        if (isLiked) "Liked" else "Like"
    }

    fun actionBid(lang: AppLanguage): String = if (lang == AppLanguage.URDU) "بولی لگائیں" else "Make Offer"
    fun actionCall(lang: AppLanguage): String = if (lang == AppLanguage.URDU) "کال کریں" else "Call"
    fun actionWhatsApp(lang: AppLanguage): String = "واٹس ایپ"
    fun actionShare(lang: AppLanguage): String = if (lang == AppLanguage.URDU) "شیئر" else "Share"

    // Filters
    fun filterAll(lang: AppLanguage): String = if (lang == AppLanguage.URDU) "تمام فصلیں" else "All"
    fun filterSell(lang: AppLanguage): String = if (lang == AppLanguage.URDU) "🌾 برائے فروخت" else "For Sale"
    fun filterBuy(lang: AppLanguage): String = if (lang == AppLanguage.URDU) "🛒 خریدار درکار" else "Wanted"
    fun filterJampur(lang: AppLanguage): String = if (lang == AppLanguage.URDU) "📍 جام پور" else "Jampur"
    fun filterSwabi(lang: AppLanguage): String = if (lang == AppLanguage.URDU) "📍 صوابی" else "Swabi"
    fun filterMardan(lang: AppLanguage): String = if (lang == AppLanguage.URDU) "📍 مردان" else "Mardan"

    // Search
    fun searchPlaceholder(lang: AppLanguage): String = if (lang == AppLanguage.URDU)
        "جام پور، صوابی، دیسی، ورجینیا تلاش کریں..."
    else
        "Search Jampur, Swabi, Desi, Virginia..."
}
