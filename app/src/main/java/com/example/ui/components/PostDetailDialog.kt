package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AppLanguage
import com.example.data.model.PostType
import com.example.data.model.TobaccoPost
import com.example.ui.theme.CallBlue
import com.example.ui.theme.MandiGreenPrimary
import com.example.ui.theme.TobaccoGoldDark
import com.example.ui.theme.TobaccoGoldPrimary
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.utils.LanguageStrings
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PostDetailDialog(
    post: TobaccoPost,
    currentLanguage: AppLanguage = AppLanguage.URDU,
    onDismiss: () -> Unit,
    onWhatsAppClick: () -> Unit,
    onCallClick: () -> Unit,
    onBidClick: () -> Unit,
    onShareClick: () -> Unit
) {
    val numberFormatter = NumberFormat.getNumberInstance(Locale.US)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 24.dp)
                .testTag("post_detail_dialog")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = if (post.postType == PostType.SELL) MandiGreenPrimary.copy(alpha = 0.15f) else TobaccoGoldPrimary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (currentLanguage == AppLanguage.URDU) post.postType.labelUrdu else post.postType.labelEn,
                            color = if (post.postType == PostType.SELL) MandiGreenPrimary else TobaccoGoldDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "${post.cropVariety} - ${post.grade}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 19.sp,
                    color = MandiGreenPrimary
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${post.authorCity} • ${post.authorName} (${post.authorRole})",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Views & Engagement stats badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Visibility, contentDescription = "Views", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${post.viewsCount} ${LanguageStrings.viewsLabel(currentLanguage)}", fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Share, contentDescription = "Shares", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${post.sharesCount} ${LanguageStrings.sharesLabel(currentLanguage)}", fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                // Photo or Video Display
                if (post.videoUrl != null || post.imageUrl != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1E293B)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (post.imageUrl != null) {
                            coil.compose.AsyncImage(
                                model = post.imageUrl,
                                contentDescription = "Tobacco Crop",
                                modifier = Modifier.fillMaxWidth().height(200.dp),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                        }

                        if (post.videoUrl != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .background(Color.Black.copy(alpha = 0.4f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Play Video",
                                        tint = Color.White,
                                        modifier = Modifier.size(52.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "ویڈیو پلے کریں (Play Video)",
                                        color = Color.White,
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                // Detail Specs Matrix
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        DetailRow(
                            label = if (currentLanguage == AppLanguage.URDU) "مقدار لاٹ:" else "Lot Quantity:",
                            value = "${post.quantityMaunds} ${if (currentLanguage == AppLanguage.URDU) "من" else "Maunds"} (${post.quantityMaunds * 40} kg)"
                        )
                        DetailRow(
                            label = if (currentLanguage == AppLanguage.URDU) "ریٹ فی من:" else "Rate / Maund:",
                            value = "Rs ${numberFormatter.format(post.pricePerMaund)}"
                        )
                        DetailRow(
                            label = if (currentLanguage == AppLanguage.URDU) "کل تخمینی رقم:" else "Total Value:",
                            value = "Rs ${numberFormatter.format(post.totalEstimatedValue)}",
                            isBold = true
                        )
                        DetailRow(
                            label = if (currentLanguage == AppLanguage.URDU) "کوالٹی / گریڈ:" else "Quality / Grade:",
                            value = post.grade
                        )
                        DetailRow(
                            label = if (currentLanguage == AppLanguage.URDU) "نمی کی کیفیت:" else "Moisture:",
                            value = post.moistureStatus
                        )
                        DetailRow(
                            label = if (currentLanguage == AppLanguage.URDU) "رابطہ نمبر:" else "Contact Phone:",
                            value = post.contactPhone
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (currentLanguage == AppLanguage.URDU) "تفصیل:" else "Description:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = post.description,
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Bids summary
                val bids = post.comments.filter { it.bidAmount != null }
                if (bids.isNotEmpty()) {
                    Text(
                        text = if (currentLanguage == AppLanguage.URDU) "موصولہ بولیاں (${bids.size}):" else "Current Bids (${bids.size}):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TobaccoGoldDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    bids.forEach { bid ->
                        Surface(
                            color = TobaccoGoldPrimary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(bid.authorName, fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                                Text("Rs ${numberFormatter.format(bid.bidAmount ?: 0)}/${if (currentLanguage == AppLanguage.URDU) "من" else "Maund"}", fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold, color = TobaccoGoldDark)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onWhatsAppClick,
                        colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                        modifier = Modifier.weight(1f).height(42.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("WhatsApp", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Button(
                        onClick = onCallClick,
                        colors = ButtonDefaults.buttonColors(containerColor = CallBlue),
                        modifier = Modifier.weight(1f).height(42.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (currentLanguage == AppLanguage.URDU) "کال کریں" else "Call", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onBidClick,
                        colors = ButtonDefaults.buttonColors(containerColor = TobaccoGoldPrimary),
                        modifier = Modifier.weight(1f).height(40.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Gavel, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(LanguageStrings.actionBid(currentLanguage), fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                    }

                    Button(
                        onClick = onShareClick,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.weight(1f).height(40.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(LanguageStrings.actionShare(currentLanguage), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = 12.5.sp, fontWeight = if (isBold) FontWeight.Bold else FontWeight.SemiBold, color = if (isBold) MandiGreenPrimary else MaterialTheme.colorScheme.onSurface)
    }
}
