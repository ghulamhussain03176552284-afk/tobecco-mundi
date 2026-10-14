package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.MandiStory
import com.example.data.model.TobaccoPost
import com.example.ui.components.CreatePostPromptCard
import com.example.ui.components.MandiStoryRow
import com.example.ui.components.TobaccoPostCard
import com.example.ui.theme.MandiGreenPrimary
import com.example.ui.theme.TobaccoGoldDark
import com.example.ui.utils.LanguageStrings

@Composable
fun FeedScreen(
    posts: List<TobaccoPost>,
    stories: List<MandiStory>,
    currentLanguage: AppLanguage,
    selectedFilter: String,
    onFilterSelect: (String) -> Unit,
    selectedCity: String,
    onCitySelect: (String) -> Unit,
    onPromptClick: () -> Unit,
    onPhotoClick: () -> Unit,
    onVideoClick: () -> Unit,
    onTextClick: () -> Unit,
    onLikeClick: (String) -> Unit,
    onCommentClick: (TobaccoPost) -> Unit,
    onWhatsAppClick: (TobaccoPost) -> Unit,
    onCallClick: (TobaccoPost) -> Unit,
    onShareClick: (TobaccoPost) -> Unit,
    onPostDetailClick: (TobaccoPost) -> Unit,
    onPlayVideoClick: (TobaccoPost) -> Unit,
    onStoryClick: (MandiStory) -> Unit,
    modifier: Modifier = Modifier
) {
    // Clear, intuitive filter buttons
    val filters = listOf(
        "ALL" to LanguageStrings.filterAll(currentLanguage),
        "JAMPUR" to (if (currentLanguage == AppLanguage.URDU) "📍 جام پور (Jampur)" else "📍 Jampur"),
        "SELL" to LanguageStrings.filterSell(currentLanguage),
        "BUY" to LanguageStrings.filterBuy(currentLanguage),
        "VIRGINIA" to (if (currentLanguage == AppLanguage.URDU) "ورجینیا" else "Virginia"),
        "RUSTICA" to (if (currentLanguage == AppLanguage.URDU) "نسوار پتا" else "Rustica")
    )

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("feed_screen_lazy_column"),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // Mandi Stories Row
            item {
                MandiStoryRow(
                    stories = stories,
                    onStoryClick = onStoryClick
                )
            }

            // Facebook Style "What's on your mind / Create Post" card
            item {
                CreatePostPromptCard(
                    currentLanguage = currentLanguage,
                    onPromptClick = onPromptClick,
                    onPhotoClick = onPhotoClick,
                    onVideoClick = onVideoClick,
                    onTextClick = onTextClick
                )
            }

            // Filter Chips
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("filter_chips_row"),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filters) { (key, label) ->
                        val isSelected = selectedFilter == key
                        FilterChip(
                            selected = isSelected,
                            onClick = { onFilterSelect(key) },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 12.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            shape = RoundedCornerShape(20.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MandiGreenPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Empty state or Posts list
            if (posts.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Agriculture,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (currentLanguage == AppLanguage.URDU)
                                    "اس فلٹر کے تحت فی الحال کوئی پوسٹ نہیں ہے۔\nآپ پہلا اشتہار لگا سکتے ہیں!"
                                else
                                    "No posts found for this filter.\nBe the first to post!",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 13.5.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            } else {
                items(
                    items = posts,
                    key = { it.id }
                ) { post ->
                    TobaccoPostCard(
                        post = post,
                        currentLanguage = currentLanguage,
                        onLikeClick = { onLikeClick(post.id) },
                        onCommentClick = { onCommentClick(post) },
                        onWhatsAppClick = { onWhatsAppClick(post) },
                        onCallClick = { onCallClick(post) },
                        onShareClick = { onShareClick(post) },
                        onCardClick = { onPostDetailClick(post) },
                        onPlayVideoClick = { onPlayVideoClick(post) }
                    )
                }
            }
        }

        // Easy Floating Action Button to Create Post
        ExtendedFloatingActionButton(
            onClick = onPromptClick,
            containerColor = MandiGreenPrimary,
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
                .testTag("fab_create_post")
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (currentLanguage == AppLanguage.URDU) "نیا اشتہار لگائیں" else "New Post",
                fontWeight = FontWeight.Bold,
                fontSize = 13.5.sp
            )
        }
    }
}
