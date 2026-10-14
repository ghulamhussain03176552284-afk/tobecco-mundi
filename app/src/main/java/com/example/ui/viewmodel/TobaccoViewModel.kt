package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppLanguage
import com.example.data.model.MandiRate
import com.example.data.model.MandiStory
import com.example.data.model.PostType
import com.example.data.model.TobaccoPost
import com.example.data.repository.TobaccoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CalculatorResult(
    val grossWeightKg: Double = 0.0,
    val grossMaunds: Double = 0.0,
    val totalTareKg: Double = 0.0,
    val netWeightKg: Double = 0.0,
    val netMaunds: Double = 0.0,
    val grossAmountPkr: Long = 0L,
    val moistureCutAmountPkr: Long = 0L,
    val commissionAmountPkr: Long = 0L,
    val netPayableAmountPkr: Long = 0L
)

class TobaccoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = TobaccoRepository(application)

    // App Language (Urdu by default, switchable to English)
    private val _currentLanguage = MutableStateFlow(AppLanguage.URDU)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    // Current Navigation Tab
    private val _currentTab = MutableStateFlow(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    // Search and Filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow("ALL") // "ALL", "SELL", "BUY", "VIRGINIA", "RUSTICA", "DESI"
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    private val _selectedCity = MutableStateFlow("ALL")
    val selectedCity: StateFlow<String> = _selectedCity.asStateFlow()

    // UI Dialogs
    private val _showCreatePostDialog = MutableStateFlow(false)
    val showCreatePostDialog: StateFlow<Boolean> = _showCreatePostDialog.asStateFlow()

    private val _activeCommentPost = MutableStateFlow<TobaccoPost?>(null)
    val activeCommentPost: StateFlow<TobaccoPost?> = _activeCommentPost.asStateFlow()

    private val _activeDetailPost = MutableStateFlow<TobaccoPost?>(null)
    val activeDetailPost: StateFlow<TobaccoPost?> = _activeDetailPost.asStateFlow()

    // Calculator State
    val calcWeightInput = MutableStateFlow("150")
    val calcIsMaund = MutableStateFlow(true) // true = Maunds, false = Kg
    val calcRatePerMaund = MutableStateFlow("14800")
    val calcBagCount = MutableStateFlow("30")
    val calcBagTareKg = MutableStateFlow("1.5")
    val calcMoisturePercent = MutableStateFlow("2.0")
    val calcCommissionPercent = MutableStateFlow("2.0")

    val mandiRates: List<MandiRate> = repository.getMandiRates()
    val mandiStories: List<MandiStory> = repository.getMandiStories()

    // Filtered Posts
    val filteredPosts: StateFlow<List<TobaccoPost>> = combine(
        repository.postsFlow,
        _searchQuery,
        _selectedFilter,
        _selectedCity
    ) { posts, query, filter, city ->
        posts.filter { post ->
            val matchesQuery = query.isBlank() ||
                post.authorName.contains(query, ignoreCase = true) ||
                post.cropVariety.contains(query, ignoreCase = true) ||
                post.authorCity.contains(query, ignoreCase = true) ||
                post.description.contains(query, ignoreCase = true)

            val matchesFilter = when (filter) {
                "SELL" -> post.postType == PostType.SELL
                "BUY" -> post.postType == PostType.BUY
                "JAMPUR" -> post.authorCity.contains("جام پور", ignoreCase = true) || post.cropVariety.contains("Jampur", ignoreCase = true)
                "VIRGINIA" -> post.cropVariety.contains("Virginia", ignoreCase = true)
                "RUSTICA" -> post.cropVariety.contains("Rustica", ignoreCase = true) || post.cropVariety.contains("Naswar", ignoreCase = true)
                "DESI" -> post.cropVariety.contains("Desi", ignoreCase = true)
                else -> true
            }

            val matchesCity = city == "ALL" || post.authorCity.contains(city, ignoreCase = true)

            matchesQuery && matchesFilter && matchesCity
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.initializeSeedDataIfNeeded()
        }
    }

    fun toggleLanguage() {
        _currentLanguage.value = if (_currentLanguage.value == AppLanguage.URDU) {
            AppLanguage.ENGLISH
        } else {
            AppLanguage.URDU
        }
    }

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
    }

    fun selectTab(index: Int) {
        _currentTab.value = index
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateFilter(filter: String) {
        _selectedFilter.value = filter
    }

    fun updateCityFilter(city: String) {
        _selectedCity.value = city
    }

    fun setCreatePostDialog(show: Boolean) {
        _showCreatePostDialog.value = show
    }

    fun setActiveCommentPost(post: TobaccoPost?) {
        _activeCommentPost.value = post
        if (post != null) {
            incrementViews(post.id)
        }
    }

    fun setActiveDetailPost(post: TobaccoPost?) {
        _activeDetailPost.value = post
        if (post != null) {
            incrementViews(post.id)
        }
    }

    fun incrementViews(postId: String) {
        viewModelScope.launch {
            repository.incrementViews(postId)
        }
    }

    fun sharePost(context: Context, post: TobaccoPost) {
        viewModelScope.launch {
            repository.incrementShares(post.id)
            val shareText = "🌾 تمباکو منڈی (${post.cropVariety})\n" +
                "👤 زمیندار: ${post.authorName} (${post.authorCity})\n" +
                "⚖️ وزن: ${post.quantityMaunds} من | ریٹ: Rs ${post.pricePerMaund}/من\n" +
                "📞 رابطہ: ${post.contactPhone}\n" +
                "تفصیل: ${post.description}"

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            context.startActivity(Intent.createChooser(intent, "تمباکو پوسٹ شیئر کریں"))
        }
    }

    fun toggleLike(postId: String) {
        viewModelScope.launch {
            repository.toggleLike(postId)
            // Refresh detail or comment post if active
            _activeCommentPost.value?.let { current ->
                if (current.id == postId) {
                    val updatedLiked = !current.isLiked
                    val newCount = if (updatedLiked) current.likesCount + 1 else (current.likesCount - 1).coerceAtLeast(0)
                    _activeCommentPost.value = current.copy(isLiked = updatedLiked, likesCount = newCount)
                }
            }
        }
    }

    fun addCommentOrBid(postId: String, authorName: String, authorRole: String, text: String, bidAmount: Int?) {
        viewModelScope.launch {
            repository.addCommentOrBid(postId, authorName, authorRole, text, bidAmount)
        }
    }

    fun createNewPost(
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
        viewModelScope.launch {
            repository.createPost(
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
                imageUrl = imageUrl,
                videoUrl = videoUrl,
                isUrgent = isUrgent
            )
            _showCreatePostDialog.value = false
        }
    }

    fun computeMandiCalculation(): CalculatorResult {
        val rawWeight = calcWeightInput.value.toDoubleOrNull() ?: 0.0
        val isMaund = calcIsMaund.value
        val ratePerMaund = calcRatePerMaund.value.toDoubleOrNull() ?: 0.0
        val bags = calcBagCount.value.toDoubleOrNull() ?: 0.0
        val tarePerBag = calcBagTareKg.value.toDoubleOrNull() ?: 0.0
        val moisturePct = (calcMoisturePercent.value.toDoubleOrNull() ?: 0.0) / 100.0
        val commPct = (calcCommissionPercent.value.toDoubleOrNull() ?: 0.0) / 100.0

        val grossWeightKg = if (isMaund) rawWeight * 40.0 else rawWeight
        val grossMaunds = grossWeightKg / 40.0

        val totalTareKg = bags * tarePerBag
        val netWeightKg = (grossWeightKg - totalTareKg).coerceAtLeast(0.0)
        val netMaunds = netWeightKg / 40.0

        val grossAmountPkr = (netMaunds * ratePerMaund).toLong()
        val moistureCutAmountPkr = (grossAmountPkr * moisturePct).toLong()
        val commissionAmountPkr = (grossAmountPkr * commPct).toLong()
        val netPayableAmountPkr = (grossAmountPkr - moistureCutAmountPkr - commissionAmountPkr).coerceAtLeast(0L)

        return CalculatorResult(
            grossWeightKg = grossWeightKg,
            grossMaunds = grossMaunds,
            totalTareKg = totalTareKg,
            netWeightKg = netWeightKg,
            netMaunds = netMaunds,
            grossAmountPkr = grossAmountPkr,
            moistureCutAmountPkr = moistureCutAmountPkr,
            commissionAmountPkr = commissionAmountPkr,
            netPayableAmountPkr = netPayableAmountPkr
        )
    }

    fun callSeller(context: Context, phone: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phone")
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            Toast.makeText(context, "Cannot open dialer: $phone", Toast.LENGTH_SHORT).show()
        }
    }

    fun openWhatsApp(context: Context, phone: String, message: String) {
        try {
            val formattedPhone = phone.trim().replace("+", "").replace("-", "").replace(" ", "")
            val internationalPhone = if (formattedPhone.startsWith("0")) {
                "92" + formattedPhone.substring(1)
            } else {
                formattedPhone
            }
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$internationalPhone&text=${Uri.encode(message)}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (_: Exception) {
            Toast.makeText(context, "WhatsApp open error: $phone", Toast.LENGTH_SHORT).show()
        }
    }
}
