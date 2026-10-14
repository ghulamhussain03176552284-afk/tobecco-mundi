package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.ui.theme.MandiGreenPrimary
import com.example.ui.theme.TobaccoGoldDark
import com.example.ui.theme.TobaccoGoldPrimary
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.viewmodel.TobaccoViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CalculatorScreen(
    viewModel: TobaccoViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val numberFormatter = NumberFormat.getNumberInstance(Locale.US)

    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val weightInput by viewModel.calcWeightInput.collectAsState()
    val ratePerMaund by viewModel.calcRatePerMaund.collectAsState()
    val bagCount by viewModel.calcBagCount.collectAsState()
    val bagTareKg by viewModel.calcBagTareKg.collectAsState()
    val moisturePercent by viewModel.calcMoisturePercent.collectAsState()
    val commissionPercent by viewModel.calcCommissionPercent.collectAsState()

    var showAdvancedDeductions by remember { mutableStateOf(false) }

    val result = viewModel.computeMandiCalculation()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 96.dp)
            .testTag("calculator_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Friendly Banner
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MandiGreenPrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(TobaccoGoldPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (currentLanguage == AppLanguage.URDU) "آسان تمباکو کیلکولیٹر" else "Easy Tobacco Calculator",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = if (currentLanguage == AppLanguage.URDU) "وزن اور ریٹ لکھیں اور کل رقم معلوم کریں" else "Enter maunds and rate to calculate cash",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Easy 2-Box Input Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (currentLanguage == AppLanguage.URDU) "1. وزن اور ریٹ درج کریں" else "1. Enter Weight & Rate",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MandiGreenPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Maunds input
                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { viewModel.calcWeightInput.value = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text(if (currentLanguage == AppLanguage.URDU) "کل تمباکو (کتنے من ہے؟)" else "Total Weight (Maunds - من)") },
                    placeholder = { Text("مثلاً 100") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth().testTag("calc_input_weight"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Rate input
                OutlinedTextField(
                    value = ratePerMaund,
                    onValueChange = { viewModel.calcRatePerMaund.value = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text(if (currentLanguage == AppLanguage.URDU) "ایک من کا ریٹ (روپے)" else "Rate per Maund (PKR)") },
                    placeholder = { Text("مثلاً 9600") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().testTag("calc_input_rate"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Optional Mandi Deductions expander
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showAdvancedDeductions = !showAdvancedDeductions }
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (currentLanguage == AppLanguage.URDU) "باردانہ و منڈی کٹوتی شامل کریں (اختیاری)" else "Include Bag Tare & Deductions (Optional)",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TobaccoGoldDark
                    )
                    Icon(
                        imageVector = if (showAdvancedDeductions) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = TobaccoGoldDark
                    )
                }

                AnimatedVisibility(visible = showAdvancedDeductions) {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = bagCount,
                                onValueChange = { viewModel.calcBagCount.value = it.filter { c -> c.isDigit() } },
                                label = { Text("بوریاں") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f).testTag("calc_input_bags"),
                                shape = RoundedCornerShape(10.dp)
                            )
                            OutlinedTextField(
                                value = bagTareKg,
                                onValueChange = { viewModel.calcBagTareKg.value = it.filter { c -> c.isDigit() || c == '.' } },
                                label = { Text("کٹوتی فی بوری (کلو)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f).testTag("calc_input_tare"),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = moisturePercent,
                                onValueChange = { viewModel.calcMoisturePercent.value = it.filter { c -> c.isDigit() || c == '.' } },
                                label = { Text("نمی کٹ %") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f).testTag("calc_input_moisture"),
                                shape = RoundedCornerShape(10.dp)
                            )
                            OutlinedTextField(
                                value = commissionPercent,
                                onValueChange = { viewModel.calcCommissionPercent.value = it.filter { c -> c.isDigit() || c == '.' } },
                                label = { Text("کمیشن %") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f).testTag("calc_input_commission"),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }
                }
            }
        }

        // Calculation Result Card (Big, Clear Payout Box)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Receipt, contentDescription = null, tint = MandiGreenPrimary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (currentLanguage == AppLanguage.URDU) "حساب کی تفصیل" else "Calculation Result",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Text(
                        text = "${String.format("%.1f", result.netMaunds)} من",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = MandiGreenPrimary
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // BIG Green Cash Display
                Surface(
                    color = MandiGreenPrimary.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, MandiGreenPrimary)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (currentLanguage == AppLanguage.URDU) "کل خالص رقم (Cash in Hand)" else "Net Amount to Receive",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MandiGreenPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Rs ${numberFormatter.format(result.netPayableAmountPkr)}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MandiGreenPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // One-click Share to WhatsApp
                Button(
                    onClick = {
                        val shareText = """
                            📋 تمباکو منڈی حساب پرچی 📋
                            وزن: ${String.format("%.1f", result.netMaunds)} من
                            ریٹ: Rs $ratePerMaund فی من
                            کل رقم: Rs ${numberFormatter.format(result.netPayableAmountPkr)}
                            
                            حساب بذریعہ: Tobacco Mandi ایپ
                        """.trimIndent()

                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "Share Mandi Receipt"))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("share_mandi_receipt_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (currentLanguage == AppLanguage.URDU) "واٹس ایپ پر پرچی بھیجیں" else "Share via WhatsApp",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
