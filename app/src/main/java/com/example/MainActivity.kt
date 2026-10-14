package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.AppLanguage
import com.example.data.model.PostType
import com.example.data.model.TobaccoPost
import com.example.ui.components.CommentBidBottomSheet
import com.example.ui.components.CreatePostDialog
import com.example.ui.components.MandiVideoPlayerDialog
import com.example.ui.components.PostDetailDialog
import com.example.ui.components.TobaccoTopBar
import com.example.ui.screens.CalculatorScreen
import com.example.ui.screens.FeedScreen
import com.example.ui.screens.MandiRatesScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.MandiGreenPrimary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.utils.LanguageStrings
import com.example.ui.viewmodel.TobaccoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TobaccoMandiApp()
            }
        }
    }
}

@Composable
fun TobaccoMandiApp(
    viewModel: TobaccoViewModel = viewModel()
) {
    val context = LocalContext.current

    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()
    val filteredPosts by viewModel.filteredPosts.collectAsState()

    val showCreatePostDialog by viewModel.showCreatePostDialog.collectAsState()
    val activeCommentPost by viewModel.activeCommentPost.collectAsState()
    val activeDetailPost by viewModel.activeDetailPost.collectAsState()

    var showNotificationDialog by remember { mutableStateOf(false) }
    var activeVideoPost by remember { mutableStateOf<TobaccoPost?>(null) }

    // Android back handling: Return to Home if on another tab
    BackHandler(enabled = currentTab != 0) {
        viewModel.selectTab(0)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TobaccoTopBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                currentLanguage = currentLanguage,
                onLanguageToggle = { viewModel.toggleLanguage() },
                onNotificationClick = { showNotificationDialog = true }
            )
        },
        bottomBar = {
            MandiNavigationBar(
                selectedTab = currentTab,
                currentLanguage = currentLanguage,
                onTabSelect = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)

        when (currentTab) {
            0 -> FeedScreen(
                posts = filteredPosts,
                stories = viewModel.mandiStories,
                currentLanguage = currentLanguage,
                selectedFilter = selectedFilter,
                onFilterSelect = { viewModel.updateFilter(it) },
                selectedCity = selectedCity,
                onCitySelect = { viewModel.updateCityFilter(it) },
                onPromptClick = { viewModel.setCreatePostDialog(true) },
                onPhotoClick = { viewModel.setCreatePostDialog(true) },
                onVideoClick = { viewModel.setCreatePostDialog(true) },
                onTextClick = { viewModel.setCreatePostDialog(true) },
                onLikeClick = { viewModel.toggleLike(it) },
                onCommentClick = { viewModel.setActiveCommentPost(it) },
                onWhatsAppClick = { post ->
                    val msg = if (currentLanguage == AppLanguage.URDU)
                        "السلام علیکم! میں نے تمباکو منڈی پر آپ کی پوسٹ دیکھی (${post.cropVariety} - ${post.quantityMaunds} من، ریٹ: Rs ${post.pricePerMaund})۔ اس بارے میں بات کرنی ہے۔"
                    else
                        "Hello! I saw your post on Tobacco Mandi (${post.cropVariety} - ${post.quantityMaunds} Maunds at Rs ${post.pricePerMaund}). Interested in discussing."
                    viewModel.openWhatsApp(context, post.contactPhone, msg)
                },
                onCallClick = { post ->
                    viewModel.callSeller(context, post.contactPhone)
                },
                onShareClick = { post ->
                    viewModel.sharePost(context, post)
                },
                onPostDetailClick = { post ->
                    viewModel.setActiveDetailPost(post)
                },
                onPlayVideoClick = { post ->
                    activeVideoPost = post
                    viewModel.incrementViews(post.id)
                },
                onStoryClick = { story ->
                    Toast.makeText(context, "${story.mandiName}: ${story.headline} (${story.rateText})", Toast.LENGTH_SHORT).show()
                },
                modifier = contentModifier
            )

            1 -> MandiRatesScreen(
                rates = viewModel.mandiRates,
                currentLanguage = currentLanguage,
                modifier = contentModifier
            )

            2 -> CalculatorScreen(
                viewModel = viewModel,
                modifier = contentModifier
            )

            3 -> ProfileScreen(
                onNewPostClick = { viewModel.setCreatePostDialog(true) },
                modifier = contentModifier
            )
        }
    }

    // Video Player Dialog (Interactive crop video inspection)
    activeVideoPost?.let { post ->
        MandiVideoPlayerDialog(
            post = post,
            currentLanguage = currentLanguage,
            onDismiss = { activeVideoPost = null },
            onCallClick = { viewModel.callSeller(context, post.contactPhone) },
            onWhatsAppClick = {
                val msg = if (currentLanguage == AppLanguage.URDU)
                    "السلام علیکم! میں نے تمباکو منڈی پر آپ کی ویڈیو دیکھی (${post.cropVariety} - ${post.quantityMaunds} من)"
                else
                    "Hello! I saw your video on Tobacco Mandi (${post.cropVariety})"
                viewModel.openWhatsApp(context, post.contactPhone, msg)
            }
        )
    }

    // Create Post Dialog
    if (showCreatePostDialog) {
        CreatePostDialog(
            initialPostType = PostType.SELL,
            currentLanguage = currentLanguage,
            onDismiss = { viewModel.setCreatePostDialog(false) },
            onSubmit = { authorName, authorRole, authorCity, postType, cropVariety, grade, qty, prc, moisture, desc, phone, imageUrl, videoUrl, isUrgent ->
                viewModel.createNewPost(
                    authorName = authorName,
                    authorRole = authorRole,
                    authorCity = authorCity,
                    postType = postType,
                    cropVariety = cropVariety,
                    grade = grade,
                    quantityMaunds = qty,
                    pricePerMaund = prc,
                    moistureStatus = moisture,
                    description = desc,
                    contactPhone = phone,
                    imageUrl = imageUrl,
                    videoUrl = videoUrl,
                    isUrgent = isUrgent
                )
                viewModel.setCreatePostDialog(false)
                Toast.makeText(
                    context,
                    if (currentLanguage == AppLanguage.URDU) "فیس بک کی طرح پوسٹ شائع ہو گئی!" else "Post published successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    // Comment & Bidding Bottom Sheet
    activeCommentPost?.let { post ->
        CommentBidBottomSheet(
            post = post,
            onDismiss = { viewModel.setActiveCommentPost(null) },
            onSubmitCommentOrBid = { text, bidAmount ->
                viewModel.addCommentOrBid(
                    postId = post.id,
                    authorName = if (currentLanguage == AppLanguage.URDU) "غلام حسین جتوئی" else "Ghulam Hussain",
                    authorRole = if (currentLanguage == AppLanguage.URDU) "زمیندار" else "Farmer",
                    text = text,
                    bidAmount = bidAmount
                )
                Toast.makeText(
                    context,
                    if (bidAmount != null) {
                        if (currentLanguage == AppLanguage.URDU) "بولی درج ہو گئی: Rs $bidAmount/من" else "Bid placed: Rs $bidAmount/Maund"
                    } else {
                        if (currentLanguage == AppLanguage.URDU) "تبصرہ شامل کر دیا گیا" else "Comment added"
                    },
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    // Post Detail Dialog
    activeDetailPost?.let { post ->
        PostDetailDialog(
            post = post,
            currentLanguage = currentLanguage,
            onDismiss = { viewModel.setActiveDetailPost(null) },
            onWhatsAppClick = {
                val msg = if (currentLanguage == AppLanguage.URDU)
                    "السلام علیکم! تمباکو لاٹ ${post.cropVariety} برائے فروخت کے متعلق معلومات درکار ہیں۔"
                else
                    "Hello! Inquiring about tobacco lot ${post.cropVariety}."
                viewModel.openWhatsApp(context, post.contactPhone, msg)
            },
            onCallClick = {
                viewModel.callSeller(context, post.contactPhone)
            },
            onBidClick = {
                viewModel.setActiveDetailPost(null)
                viewModel.setActiveCommentPost(post)
            },
            onShareClick = {
                viewModel.sharePost(context, post)
            }
        )
    }

    // Notification Dialog
    if (showNotificationDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationDialog = false },
            title = {
                Text(
                    if (currentLanguage == AppLanguage.URDU) "منڈی الرٹس اور بولیاں" else "Mandi Alerts & Bids",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = if (currentLanguage == AppLanguage.URDU)
                        "• جام پور منڈی میں دیسی کالا پٹہ تمباکو ریٹ میں 450 روپے اضافہ۔\n• صوابی منڈی میں ورجینیا گریڈ 1 کا ریٹ Rs 14,800 تک پہنچ گیا۔\n• آپ کی پوسٹ پر ملک فرید (جام پور) نے 9,300 روپے کی بولی لگائی ہے۔"
                    else
                        "• Jampur Mandi: Desi tobacco price up by Rs 450/Maund.\n• Swabi Mandi: Virginia Grade 1 reached Rs 14,800/Maund.\n• New Bid: Malik Fareed (Jampur) placed Rs 9,300 offer on your post.",
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { showNotificationDialog = false }) {
                    Text(
                        if (currentLanguage == AppLanguage.URDU) "ٹھیک ہے (OK)" else "OK",
                        color = MandiGreenPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )
    }
}

@Composable
fun MandiNavigationBar(
    selectedTab: Int,
    currentLanguage: AppLanguage,
    onTabSelect: (Int) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        modifier = Modifier.testTag("bottom_nav_bar")
    ) {
        // Tab 0: Home (ہوم)
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelect(0) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.padding(2.dp)) },
            label = {
                Text(
                    text = LanguageStrings.tabFeed(currentLanguage),
                    fontSize = 11.5.sp,
                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MandiGreenPrimary,
                selectedTextColor = MandiGreenPrimary,
                indicatorColor = MandiGreenPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_feed_tab")
        )

        // Tab 1: Rates (آج کے ریٹ)
        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelect(1) },
            icon = { Icon(Icons.Default.TrendingUp, contentDescription = "Rates", modifier = Modifier.padding(2.dp)) },
            label = {
                Text(
                    text = LanguageStrings.tabRates(currentLanguage),
                    fontSize = 11.5.sp,
                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MandiGreenPrimary,
                selectedTextColor = MandiGreenPrimary,
                indicatorColor = MandiGreenPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_rates_tab")
        )

        // Tab 2: Calculator (حساب کتاب)
        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelect(2) },
            icon = { Icon(Icons.Default.Calculate, contentDescription = "Calc", modifier = Modifier.padding(2.dp)) },
            label = {
                Text(
                    text = LanguageStrings.tabCalculator(currentLanguage),
                    fontSize = 11.5.sp,
                    fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MandiGreenPrimary,
                selectedTextColor = MandiGreenPrimary,
                indicatorColor = MandiGreenPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_calculator_tab")
        )

        // Tab 3: Profile & Helpline (رابطہ و مدد)
        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = { onTabSelect(3) },
            icon = { Icon(Icons.Default.Call, contentDescription = "Help", modifier = Modifier.padding(2.dp)) },
            label = {
                Text(
                    text = LanguageStrings.tabProfile(currentLanguage),
                    fontSize = 11.5.sp,
                    fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MandiGreenPrimary,
                selectedTextColor = MandiGreenPrimary,
                indicatorColor = MandiGreenPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_profile_tab")
        )
    }
}
