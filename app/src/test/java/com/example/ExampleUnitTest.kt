package com.example

import com.example.data.model.AppLanguage
import com.example.data.model.PostType
import com.example.data.model.TobaccoPost
import com.example.ui.utils.LanguageStrings
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testMandiWeightAndTareCalculation() {
        val grossMaunds = 100.0
        val grossWeightKg = grossMaunds * 40.0 // 4000.0 kg
        val bagCount = 20
        val tarePerBagKg = 1.5
        val totalTareKg = bagCount * tarePerBagKg // 30.0 kg

        val netWeightKg = grossWeightKg - totalTareKg // 3970.0 kg
        val netMaunds = netWeightKg / 40.0 // 99.25 Maunds

        assertEquals(4000.0, grossWeightKg, 0.001)
        assertEquals(30.0, totalTareKg, 0.001)
        assertEquals(3970.0, netWeightKg, 0.001)
        assertEquals(99.25, netMaunds, 0.001)
    }

    @Test
    fun testMandiPriceAndPayoutCalculation() {
        val netMaunds = 100.0
        val ratePerMaund = 14000.0
        val grossAmount = (netMaunds * ratePerMaund).toLong() // 1,400,000 PKR

        val moisturePercent = 0.02 // 2%
        val commissionPercent = 0.02 // 2%

        val moistureCut = (grossAmount * moisturePercent).toLong() // 28,000 PKR
        val commissionCut = (grossAmount * commissionPercent).toLong() // 28,000 PKR
        val netPayable = grossAmount - moistureCut - commissionCut // 1,344,000 PKR

        assertEquals(1400000L, grossAmount)
        assertEquals(28000L, moistureCut)
        assertEquals(28000L, commissionCut)
        assertEquals(1344000L, netPayable)
    }

    @Test
    fun testViewsAndSharesIncrement() {
        val post = TobaccoPost(
            id = "test_1",
            authorName = "غلام حسین جتوئی",
            authorRole = "زمیندار",
            authorCity = "جام پور",
            postType = PostType.SELL,
            cropVariety = "Jampur Desi Patta",
            grade = "Grade 1",
            quantityMaunds = 150,
            pricePerMaund = 9600,
            moistureStatus = "خشک",
            description = "ٹیسٹ پوسٹ",
            contactPhone = "03176552284",
            viewsCount = 10,
            sharesCount = 2
        )

        val updatedPost = post.copy(
            viewsCount = post.viewsCount + 1,
            sharesCount = post.sharesCount + 1
        )

        assertEquals(11, updatedPost.viewsCount)
        assertEquals(3, updatedPost.sharesCount)
    }

    @Test
    fun testLanguageStrings() {
        assertEquals("تمباکو منڈی", LanguageStrings.appTitle(AppLanguage.URDU))
        assertEquals("Tobacco Mandi", LanguageStrings.appTitle(AppLanguage.ENGLISH))
        assertEquals("📍 جام پور", LanguageStrings.filterJampur(AppLanguage.URDU))
        assertEquals("Jampur", LanguageStrings.filterJampur(AppLanguage.ENGLISH))
    }
}
