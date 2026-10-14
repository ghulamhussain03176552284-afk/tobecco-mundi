package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CallBlue
import com.example.ui.theme.MandiGreenPrimary
import com.example.ui.theme.TobaccoGoldDark
import com.example.ui.theme.TobaccoGoldPrimary

@Composable
fun ProfileScreen(
    onNewPostClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentRole by remember { mutableStateOf("زمیندار (Tobacco Farmer)") }
    val roles = listOf("زمیندار (Tobacco Farmer)", "بیوپاری / خریدار (Trader)", "آڑھتی (Commission Agent)")

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp)
            .testTag("profile_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // User Profile Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MandiGreenPrimary.copy(alpha = 0.15f))
                            .border(2.dp, MandiGreenPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Agriculture,
                            contentDescription = null,
                            tint = MandiGreenPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "حاجی فضل رحمان صوابی",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified",
                                tint = MandiGreenPrimary,
                                modifier = Modifier.size(17.dp)
                            )
                        }

                        Text(
                            text = currentRole,
                            fontSize = 12.sp,
                            color = TobaccoGoldDark,
                            fontWeight = FontWeight.SemiBold
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "یار حسین، صوابی (خیبر پختونخوا)",
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(10.dp))

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ProfileStat("4", "فعال پوسٹس")
                    ProfileStat("1,250", "من فروخت شدہ")
                    ProfileStat("100%", "بھروسہ ریٹنگ")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Switch Role
                Text(
                    text = "آپ کا منڈی کردار منتخب کریں:",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    roles.forEach { role ->
                        val isSelected = currentRole == role
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) MandiGreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
                            onClick = { currentRole = role },
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = role.split(" ")[0],
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // Tobacco Mandi Helplines & Rules
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "منڈی ہیلپ لائن اور سرکاری رہنمائی",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MandiGreenPrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                HelplineRow(
                    title = "جام پور منڈی کسان و بیوپاری کمیٹی",
                    subtitle = "دیسی تمباکو ریٹ و نیلامی رہنمائی",
                    phone = "03176552284",
                    onCall = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:03176552284"))
                        context.startActivity(intent)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                HelplineRow(
                    title = "پاکستان ٹوبیکو بورڈ (PTB) صوابی",
                    subtitle = "کم از کم سرکاری قیمت اور کسان شکایات",
                    phone = "0938221144",
                    onCall = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:0938221144"))
                        context.startActivity(intent)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                HelplineRow(
                    title = "منڈی بیوپاری ایسوسی ایشن مردان",
                    subtitle = "نیلامی شیڈول اور باردانہ معلومات",
                    phone = "0937862211",
                    onCall = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:0937862211"))
                        context.startActivity(intent)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                HelplineRow(
                    title = "کسان اتحاد چارسدہ",
                    subtitle = "نسوار و دیسی پٹہ رہنمائی",
                    phone = "0916511223",
                    onCall = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:0916511223"))
                        context.startActivity(intent)
                    }
                )
            }
        }

        // Tobacco Farming Quality Guide
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = TobaccoGoldPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "تمباکو کوالٹی اور بارن کیورنگ ہدایات",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "• ورجینیا تمباکو کے بارن میں درجہ حرارت 160°F سے اوپر نہ جانے دیں تاکہ رنگ سنہرا رہے۔\n• سفید نسوار پٹہ کے لیے دھوپ میں یکساں خشکی لازمی ہے تاکہ پتے میں کڑواہٹ اور خوشبو برقرار رہے۔\n• نمی کی شرح 12% سے 14% کے درمیان ہونے سے وزن اور قیمت بہترین ملتی ہے۔",
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ProfileStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = MandiGreenPrimary)
        Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun HelplineRow(
    title: String,
    subtitle: String,
    phone: String,
    onCall: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                Text(text = subtitle, fontSize = 10.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Button(
                onClick = onCall,
                colors = ButtonDefaults.buttonColors(containerColor = CallBlue),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.height(30.dp)
            ) {
                Icon(Icons.Default.Call, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("کال", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
