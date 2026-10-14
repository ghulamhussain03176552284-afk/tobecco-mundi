package com.example.data.repository

import android.content.Context
import com.example.data.local.TobaccoStorage
import com.example.data.model.CommentItem
import com.example.data.model.MandiRate
import com.example.data.model.MandiStory
import com.example.data.model.PostType
import com.example.data.model.TobaccoPost
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class TobaccoRepository(context: Context) {
    private val storage = TobaccoStorage(context)

    val postsFlow: Flow<List<TobaccoPost>> = storage.postsFlow

    suspend fun initializeSeedDataIfNeeded() {
        storage.initialize()
        if (storage.getCount() == 0) {
            storage.insertAll(getSeedPosts())
        }
    }

    suspend fun createPost(
        authorName: String,
        authorRole: String,
        authorCity: String,
        postType: PostType,
        cropVariety: String,
        grade: String,
        quantityMaunds: Int,
        pricePerMaund: Int,
        moistureStatus: String,
        description: String,
        contactPhone: String,
        imageUrl: String? = null,
        videoUrl: String? = null,
        isUrgent: Boolean = false
    ) {
        val newPost = TobaccoPost(
            id = UUID.randomUUID().toString(),
            authorName = authorName,
            authorRole = authorRole,
            authorCity = authorCity,
            postType = postType,
            cropVariety = cropVariety,
            grade = grade,
            quantityMaunds = quantityMaunds,
            pricePerMaund = pricePerMaund,
            moistureStatus = moistureStatus,
            description = description,
            contactPhone = contactPhone,
            timestamp = System.currentTimeMillis(),
            likesCount = 0,
            isLiked = false,
            comments = emptyList(),
            viewsCount = 1,
            sharesCount = 0,
            imageUrl = imageUrl,
            videoUrl = videoUrl,
            isVerifiedFarmer = true,
            isUrgent = isUrgent
        )
        storage.insertPost(newPost)
    }

    suspend fun toggleLike(postId: String) {
        val currentPost = storage.getPostById(postId) ?: return
        val newLiked = !currentPost.isLiked
        val newLikesCount = if (newLiked) currentPost.likesCount + 1 else (currentPost.likesCount - 1).coerceAtLeast(0)
        val updated = currentPost.copy(isLiked = newLiked, likesCount = newLikesCount)
        storage.updatePost(updated)
    }

    suspend fun incrementViews(postId: String) {
        val currentPost = storage.getPostById(postId) ?: return
        val updated = currentPost.copy(viewsCount = currentPost.viewsCount + 1)
        storage.updatePost(updated)
    }

    suspend fun incrementShares(postId: String) {
        val currentPost = storage.getPostById(postId) ?: return
        val updated = currentPost.copy(sharesCount = currentPost.sharesCount + 1)
        storage.updatePost(updated)
    }

    suspend fun addCommentOrBid(postId: String, authorName: String, authorRole: String, text: String, bidAmount: Int?) {
        val currentPost = storage.getPostById(postId) ?: return
        val newComment = CommentItem(
            id = UUID.randomUUID().toString(),
            authorName = authorName,
            authorRole = authorRole,
            text = text,
            bidAmount = bidAmount,
            timestamp = System.currentTimeMillis()
        )
        val updatedComments = currentPost.comments + newComment
        val updated = currentPost.copy(comments = updatedComments)
        storage.updatePost(updated)
    }

    fun getMandiRates(): List<MandiRate> {
        return listOf(
            MandiRate(
                id = "rate_jampur",
                mandiName = "جام پور منڈی (Jampur)",
                variety = "Desi / Rustica Patta (دیسی جام پوری)",
                grade = "Special A Grade (اعلیٰ پتا)",
                averageRate = 9600,
                minRate = 9000,
                maxRate = 10200,
                changeTrend = 450,
                totalVolumeMaunds = 3200,
                status = "نیلامی جاری ہے (Active)",
                updatedTime = "آج صبح 11:15 بجے"
            ),
            MandiRate(
                id = "rate_1",
                mandiName = "صوابی منڈی (Swabi)",
                variety = "Virginia FCV (اعلیٰ ورجینیا)",
                grade = "Grade 1 (Top / اعلیٰ)",
                averageRate = 14800,
                minRate = 14200,
                maxRate = 15300,
                changeTrend = 350,
                totalVolumeMaunds = 4200,
                status = "نیلامی جاری ہے (Active)",
                updatedTime = "آج صبح 10:30 بجے"
            ),
            MandiRate(
                id = "rate_2",
                mandiName = "مردان منڈی (Mardan)",
                variety = "Virginia FCV (ورجینیا)",
                grade = "Grade 2 (درمیانہ)",
                averageRate = 12600,
                minRate = 12100,
                maxRate = 13000,
                changeTrend = 150,
                totalVolumeMaunds = 3100,
                status = "نیلامی جاری ہے (Active)",
                updatedTime = "آج صبح 11:00 بجے"
            ),
            MandiRate(
                id = "rate_3",
                mandiName = "چارسدہ منڈی (Charsadda)",
                variety = "Rustica / Naswar Patta (نسوار)",
                grade = "Super White (سفید پٹہ)",
                averageRate = 11400,
                minRate = 10800,
                maxRate = 11900,
                changeTrend = -100,
                totalVolumeMaunds = 1850,
                status = "آج کی نیلامی مکمل",
                updatedTime = "آج دوپہر 01:15 بجے"
            ),
            MandiRate(
                id = "rate_4",
                mandiName = "یار حسین منڈی (Yar Hussain)",
                variety = "Virginia FCV (بارن کیورڈ)",
                grade = "Grade 1 (اعلیٰ پتا)",
                averageRate = 15100,
                minRate = 14600,
                maxRate = 15500,
                changeTrend = 400,
                totalVolumeMaunds = 2600,
                status = "نیلامی جاری ہے (Active)",
                updatedTime = "آج صبح 09:45 بجے"
            ),
            MandiRate(
                id = "rate_5",
                mandiName = "ساہیوال منڈی (Sahiwal)",
                variety = "Desi Hookah Patta (دیسی حقہ)",
                grade = "Thick Leaf (موٹا پٹہ)",
                averageRate = 8700,
                minRate = 8200,
                maxRate = 9100,
                changeTrend = 200,
                totalVolumeMaunds = 1400,
                status = "نیلامی جاری ہے (Active)",
                updatedTime = "آج صبح 10:15 بجے"
            ),
            MandiRate(
                id = "rate_6",
                mandiName = "مانسہرہ منڈی (Mansehra)",
                variety = "Sun Cured (دھوپ خشک پٹہ)",
                grade = "Standard Grade",
                averageRate = 9800,
                minRate = 9200,
                maxRate = 10400,
                changeTrend = 0,
                totalVolumeMaunds = 950,
                status = "نیلامی جاری ہے (Active)",
                updatedTime = "آج دوپہر 12:00 بجے"
            )
        )
    }

    fun getMandiStories(): List<MandiStory> {
        return listOf(
            MandiStory("s_jampur", "جام پور منڈی", "دیسی تمباکو نیلامی تیز", "9,600/من", true, "غلام حسین جتوئی"),
            MandiStory("s1", "صوابی منڈی", "ورجینیا گریڈ 1 نیلامی شروع", "14,800/من", true, "حاجی گلزار"),
            MandiStory("s2", "مردان منڈی", "ملٹی نیشنل کمپنیوں کی خریداری", "13,500/من", true, "بیوپاری ایسوسی ایشن"),
            MandiStory("s3", "چارسدہ", "نسوار پٹہ کی تیز ڈیمانڈ", "11,400/من", true, "خان لالا"),
            MandiStory("s4", "یار حسین", "تازہ بارن کیورڈ لاٹ تیار", "15,100/من", true, "زمیندار اتحاد"),
            MandiStory("s5", "ساہیوال", "دیسی حقہ تمباکو ریٹ میں اضافہ", "8,700/من", false, "چودھری اصغر")
        )
    }

    private fun getSeedPosts(): List<TobaccoPost> {
        val now = System.currentTimeMillis()
        val oneHour = 3600 * 1000L
        return listOf(
            TobaccoPost(
                id = "seed_jampur_1",
                authorName = "غلام حسین جتوئی (Ghulam Hussain)",
                authorRole = "زمیندار (Farmer)",
                authorCity = "جام پور (Jampur)",
                postType = PostType.SELL,
                cropVariety = "Jampur Desi Patta (جام پور دیسی)",
                grade = "Grade 1 (Top / اعلیٰ کالا پتا)",
                quantityMaunds = 220,
                pricePerMaund = 9600,
                moistureStatus = "خشک اور چھانٹی شدہ (Super Dry)",
                description = "جام پور، راجن پور روڈ سے بہترین دیسی اور کالا پٹہ تمباکو تیار ہے۔ خالص خوشبودار حقہ اور نسوار گریڈ مال، 220 من لاٹ باردانہ بند تیار ہے۔ منڈی ریٹ سے مناسب رعایت پر سودا ہو سکتا ہے۔",
                contactPhone = "03176552284",
                timestamp = now - (30 * 60 * 1000L),
                likesCount = 42,
                isLiked = false,
                isVerifiedFarmer = true,
                isUrgent = false,
                viewsCount = 318,
                sharesCount = 14,
                imageUrl = "https://images.unsplash.com/photo-1598112972019-91e3162e8893?w=800&q=80",
                videoUrl = "sample_jampur_lot_video",
                comments = listOf(
                    CommentItem(
                        id = "cj1",
                        authorName = "ملک فرید احمد (Jampur Traders)",
                        authorRole = "بیوپاری (Trader)",
                        text = "جتوئی صاحب! 9,300 کے حساب سے مکمل 220 من کیش پر ڈن کریں۔ کل ٹرک بھیجتا ہوں۔",
                        bidAmount = 9300,
                        timestamp = now - (20 * 60 * 1000L)
                    )
                )
            ),
            TobaccoPost(
                id = "seed_1",
                authorName = "حاجی گلزار خان (Haji Gulzar)",
                authorRole = "زمیندار (Farmer)",
                authorCity = "صوابی (Swabi)",
                postType = PostType.SELL,
                cropVariety = "Virginia FCV (ورجینیا)",
                grade = "Grade 1 (Top / اعلیٰ)",
                quantityMaunds = 180,
                pricePerMaund = 14800,
                moistureStatus = "خشک اور صاف (Super Dry)",
                description = "ماشاءاللہ یار حسین بیلٹ سے بہترین ورجینیا تمباکو گریڈ 1 کی تیار لاٹ موجود ہے۔ مکمل بارن کیورڈ، سنہرا رنگ، بغیر ڈنڈی۔ موقع پر چیک کر کے نقد سودا کریں۔ رابطہ کریں۔",
                contactPhone = "03001234567",
                timestamp = now - (2 * oneHour),
                likesCount = 28,
                isLiked = false,
                isVerifiedFarmer = true,
                isUrgent = false,
                viewsCount = 245,
                sharesCount = 9,
                imageUrl = "https://images.unsplash.com/photo-1589923188900-85dae523342b?w=800&q=80",
                videoUrl = "sample_swabi_mandi_auction_video",
                comments = listOf(
                    CommentItem(
                        id = "c1",
                        authorName = "ملک طارق (Malik Tariq)",
                        authorRole = "بیوپاری (Trader)",
                        text = "لالا پوری لاٹ کا اکٹھا سودا کرنا ہے۔ 14,500 روپے نقد موقع پر پیشکش ہے۔",
                        bidAmount = 14500,
                        timestamp = now - (1 * oneHour)
                    ),
                    CommentItem(
                        id = "c2",
                        authorName = "نعیم خان مروت",
                        authorRole = "آڑھتی (Commission Agent)",
                        text = "ماشاءاللہ بہت صاف مال ہے، کل گاڑی بھیج کر سیمپل چیک کرواتے ہیں۔",
                        bidAmount = null,
                        timestamp = now - (45 * 60 * 1000L)
                    )
                )
            ),
            TobaccoPost(
                id = "seed_2",
                authorName = "ملک طارق محمود (Mardan Traders)",
                authorRole = "بیوپاری / خریدار (Trader)",
                authorCity = "مردان (Mardan)",
                postType = PostType.BUY,
                cropVariety = "Virginia FCV (ورجینیا)",
                grade = "Grade 2 (درمیانہ)",
                quantityMaunds = 400,
                pricePerMaund = 12600,
                moistureStatus = "معتدل (Normal)",
                description = "فیکٹری اور گودام کے لیے فوری 400 من ورجینیا گریڈ 2 تمباکو کی ضرورت ہے۔ گاڑی ہماری اپنی ہوگی، نقد ادائیگیاں موقع پر منڈی وزن کے مطابق ہوں گی۔ زمیندار بھائی رابطہ کریں۔",
                contactPhone = "03129876543",
                timestamp = now - (4 * oneHour),
                likesCount = 19,
                isLiked = false,
                isVerifiedFarmer = true,
                isUrgent = true,
                viewsCount = 189,
                sharesCount = 6,
                comments = listOf(
                    CommentItem(
                        id = "c3",
                        authorName = "ارباب فیاض",
                        authorRole = "زمیندار (Farmer)",
                        text = "میرے پاس ڈاگئی صوابی میں 150 من موجود ہے، کل وزٹ کر لیں۔",
                        bidAmount = 12800,
                        timestamp = now - (3 * oneHour)
                    )
                )
            ),
            TobaccoPost(
                id = "seed_3",
                authorName = "خان لالا چارسدوی (Charsadda)",
                authorRole = "زمیندار (Farmer)",
                authorCity = "چارسدہ (Charsadda)",
                postType = PostType.SELL,
                cropVariety = "Rustica / Naswar Patta (نسوار)",
                grade = "Super White (سفید اعلیٰ)",
                quantityMaunds = 95,
                pricePerMaund = 11400,
                moistureStatus = "تیار اور خشک (Crisp Dry)",
                description = "چارسدہ کا اصلی سفید نسوار پٹہ، تیز خوشبو اور بغیر ملاوٹ۔ 95 من پیکٹ بوریوں میں بند تیار ہے۔ بیوپاری حضرات فوری رابطہ کریں۔ ریٹ میں گنجائش موجود ہے۔",
                contactPhone = "03335557890",
                timestamp = now - (6 * oneHour),
                likesCount = 34,
                isLiked = true,
                isVerifiedFarmer = true,
                isUrgent = false,
                viewsCount = 412,
                sharesCount = 18,
                imageUrl = "https://images.unsplash.com/photo-1500937386664-56d1dfef3854?w=800&q=80",
                videoUrl = "sample_charsadda_mandi_video",
                comments = listOf(
                    CommentItem(
                        id = "c4",
                        authorName = "شیر علی خان",
                        authorRole = "نسوار ڈیلر (Dealer)",
                        text = "خان صاحب 11,000 کے حساب سے ڈن کریں تو ابھی لوڈنگ کرواتا ہوں۔",
                        bidAmount = 11000,
                        timestamp = now - (5 * oneHour)
                    )
                )
            ),
            TobaccoPost(
                id = "seed_4",
                authorName = "چودھری اصغر گجر (Sahiwal)",
                authorRole = "زمیندار و آڑھتی (Agent)",
                authorCity = "ساہیوال (Sahiwal)",
                postType = PostType.SELL,
                cropVariety = "Desi Patta (دیسی تمباکو)",
                grade = "Thick Leaf (موٹا پٹہ)",
                quantityMaunds = 120,
                pricePerMaund = 8700,
                moistureStatus = "خشک (Dry)",
                description = "پنجاب کا دیسی حقہ تمباکو موٹا پتا، بہترین رنگت اور ذائقہ۔ 120 من مال منڈی گودام میں رکھا ہے۔ نقد کی صورت میں رعایت ممکن ہے۔",
                contactPhone = "03014443322",
                timestamp = now - (9 * oneHour),
                likesCount = 14,
                isLiked = false,
                isVerifiedFarmer = true,
                isUrgent = false,
                viewsCount = 160,
                sharesCount = 4,
                imageUrl = "https://images.unsplash.com/photo-1530595467537-0b5996c41f2d?w=800&q=80",
                comments = emptyList()
            )
        )
    }
}
