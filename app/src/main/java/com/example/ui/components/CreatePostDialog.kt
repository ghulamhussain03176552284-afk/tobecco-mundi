package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.data.model.AppLanguage
import com.example.data.model.PostType
import com.example.ui.theme.CallBlue
import com.example.ui.theme.MandiGreenPrimary
import com.example.ui.theme.TobaccoGoldDark
import com.example.ui.theme.TobaccoGoldPrimary

/**
 * Super Easy Facebook-style Post Creation Dialog
 * Allows farmers to upload Photo, Video, and Text in simple steps.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreatePostDialog(
    initialPostType: PostType = PostType.SELL,
    currentLanguage: AppLanguage = AppLanguage.URDU,
    onDismiss: () -> Unit,
    onSubmit: (
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
        imageUrl: String?,
        videoUrl: String?,
        isUrgent: Boolean
    ) -> Unit
) {
    var postType by remember { mutableStateOf(initialPostType) }
    var authorName by remember { mutableStateOf("غلام حسین") }
    var authorCity by remember { mutableStateOf("جام پور (Jampur)") }
    var selectedVariety by remember { mutableStateOf("جام پور دیسی پتا (Jampur Desi)") }

    val cities = listOf(
        "جام پور (Jampur)",
        "صوابی (Swabi)",
        "مردان (Mardan)",
        "چارسدہ (Charsadda)",
        "یار حسین (Yar Hussain)",
        "ساہیوال (Sahiwal)"
    )

    val varieties = listOf(
        "جام پور دیسی پتا (Jampur Desi)",
        "ورجینیا (Virginia FCV)",
        "نسوار پتا (Rustica)",
        "سفید پٹہ (White Patta)"
    )

    var quantityInput by remember { mutableStateOf("100") }
    var priceInput by remember { mutableStateOf("9600") }
    var contactPhone by remember { mutableStateOf("03176552284") }
    var description by remember { mutableStateOf("جام پور میں بہترین کوالٹی کا خشک تیار تمباکو برائے فروخت ہے۔ موقع پر کیش سودا کریں۔") }

    // Media attachment states (Photo & Video)
    var selectedImageUri by remember { mutableStateOf<String?>("https://images.unsplash.com/photo-1598112972019-91e3162e8893?w=800&q=80") }
    var selectedVideoUri by remember { mutableStateOf<String?>(null) }

    // Android Zero-permission Visual Media Pickers
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri.toString()
        }
    }

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedVideoUri = uri.toString()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp)
                .testTag("create_post_dialog")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Header: Title + Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (currentLanguage == AppLanguage.URDU) "فیس بک کی طرح پوسٹ بنائیں" else "Create Tobacco Post",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp
                            )
                        )
                        Text(
                            text = if (currentLanguage == AppLanguage.URDU) "فوٹو، ویڈیو اور تفصیل شامل کریں" else "Add photo, video & text details",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                // 1. Easy Post Type Switcher: [بیچنا ہے (Sell)] | [خریدنا ہے (Buy)]
                Text(
                    text = if (currentLanguage == AppLanguage.URDU) "1. آپ کیا کرنا چاہتے ہیں؟" else "1. What would you like to do?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { postType = PostType.SELL },
                        color = if (postType == PostType.SELL) MandiGreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (postType == PostType.SELL) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                text = if (currentLanguage == AppLanguage.URDU) "🌾 بیچنا ہے (Sell)" else "🌾 For Sale",
                                color = if (postType == PostType.SELL) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { postType = PostType.BUY },
                        color = if (postType == PostType.BUY) TobaccoGoldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (postType == PostType.BUY) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                text = if (currentLanguage == AppLanguage.URDU) "🛒 خریدنا ہے (Buy)" else "🛒 Wanted to Buy",
                                color = if (postType == PostType.BUY) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 2. Upload Photo & Video Section (Very Clear and Prominent)
                Text(
                    text = if (currentLanguage == AppLanguage.URDU) "2. فوٹو یا ویڈیو لگائیں (Upload Media)" else "2. Upload Photo or Video",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Pick Photo Button
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MandiGreenPrimary.copy(alpha = 0.12f),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, MandiGreenPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                            .testTag("upload_photo_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.Image, contentDescription = null, tint = MandiGreenPrimary, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (currentLanguage == AppLanguage.URDU) "📷 گیلری سے فوٹو" else "📷 Pick Photo",
                                color = MandiGreenPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.5.sp
                            )
                        }
                    }

                    // Pick Video Button
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = CallBlue.copy(alpha = 0.12f),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, CallBlue),
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                videoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                )
                            }
                            .testTag("upload_video_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.Videocam, contentDescription = null, tint = CallBlue, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (currentLanguage == AppLanguage.URDU) "🎥 گیلری سے ویڈیو" else "🎥 Pick Video",
                                color = CallBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.5.sp
                            )
                        }
                    }
                }

                // Quick Presets row
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                selectedImageUri = "https://images.unsplash.com/photo-1598112972019-91e3162e8893?w=800&q=80"
                            }
                    ) {
                        Text(
                            text = "🌿 جام پور دیسی فوٹو",
                            fontSize = 11.sp,
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                selectedVideoUri = "sample_jampur_crop_video"
                            }
                    ) {
                        Text(
                            text = "🎥 منڈی ویڈیو سیمپل",
                            fontSize = 11.sp,
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Preview Card if photo or video is attached
                if (selectedImageUri != null || selectedVideoUri != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1E293B)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedImageUri != null) {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Attached Photo",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        // Video Play Overlay if video attached
                        if (selectedVideoUri != null) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(Color.Black.copy(alpha = 0.45f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Video Attached",
                                        tint = Color.White,
                                        modifier = Modifier.size(44.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "ویڈیو منسلک ہے (Video Attached)",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Remove Button
                        IconButton(
                            onClick = {
                                selectedImageUri = null
                                selectedVideoUri = null
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.65f))
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 3. Post Text & Details (Big Multiline Field)
                Text(
                    text = if (currentLanguage == AppLanguage.URDU) "3. پوسٹ کی تفصیل و پیغام (Write Text)" else "3. Post Text & Details",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("تفصیل لکھیں (مثلاً فصل کی کوالٹی، وزن، ریٹ)") },
                    minLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_post_description"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 4. Quick City & Variety Selection Chips
                Text(
                    text = if (currentLanguage == AppLanguage.URDU) "شہر / منڈی کا انتخاب کریں:" else "Select City / Mandi:",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    cities.forEach { city ->
                        val isSelected = authorCity == city
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) MandiGreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { authorCity = city }
                        ) {
                            Text(
                                text = city,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontSize = 11.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (currentLanguage == AppLanguage.URDU) "تمباکو کی قسم:" else "Crop Variety:",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    varieties.forEach { v ->
                        val isSelected = selectedVariety == v
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) TobaccoGoldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { selectedVariety = v }
                        ) {
                            Text(
                                text = v,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontSize = 11.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 5. Weight & Price per Maund
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = quantityInput,
                        onValueChange = { quantityInput = it.filter { c -> c.isDigit() } },
                        label = { Text("کتنے من (Maunds)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_post_quantity"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = priceInput,
                        onValueChange = { priceInput = it.filter { c -> c.isDigit() } },
                        label = { Text("ریٹ فی من (PKR)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_post_price"),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 6. Name and Phone
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = authorName,
                        onValueChange = { authorName = it },
                        label = { Text("آپ کا نام") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = contactPhone,
                        onValueChange = { contactPhone = it },
                        label = { Text("موبائل نمبر") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_post_phone"),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Submit Button: Large, Bright, Easy to Tap!
                Button(
                    onClick = {
                        val qty = quantityInput.toIntOrNull() ?: 100
                        val prc = priceInput.toIntOrNull() ?: 9600
                        onSubmit(
                            authorName,
                            if (postType == PostType.SELL) "زمیندار" else "خریدار",
                            authorCity,
                            postType,
                            selectedVariety,
                            "Grade 1",
                            qty,
                            prc,
                            "صاف اور خشک",
                            description,
                            contactPhone,
                            selectedImageUri,
                            selectedVideoUri,
                            false
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_new_post_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (postType == PostType.SELL) MandiGreenPrimary else TobaccoGoldPrimary
                    )
                ) {
                    Text(
                        text = if (currentLanguage == AppLanguage.URDU)
                            "🚀 پوسٹ شائع کریں (Publish Post)"
                        else
                            "🚀 Publish Post",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
