package com.pixelcolorpicker.web

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.ComposeViewport
import com.pixelcolorpicker.resources.Res
import com.pixelcolorpicker.resources.canvas_size
import com.pixelcolorpicker.resources.generate
import com.pixelcolorpicker.resources.generate_hint
import com.pixelcolorpicker.resources.home_subtitle
import com.pixelcolorpicker.resources.ic_canvas_size
import com.pixelcolorpicker.resources.ic_generate
import com.pixelcolorpicker.resources.old_ic_app_icon
import com.pixelcolorpicker.resources.old_ic_arrow_down
import com.pixelcolorpicker.resources.old_ic_swap
import com.pixelcolorpicker.resources.settings
import com.pixelcolorpicker.resources.swap_size
import com.pixelcolorpicker.ui.theme.AppTypography
import com.pixelcolorpicker.ui.theme.PcpColors
import com.pixelcolorpicker.ui.theme.circleIconBg
import com.pixelcolorpicker.ui.theme.glowCardBorder
import com.pixelcolorpicker.ui.theme.inputBoxBorder
import kotlinx.browser.document
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        PcpWebApp()
    }
}

@Composable
fun PcpWebApp() {
    MaterialTheme(typography = AppTypography) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PcpColors.BgPage),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            ) {
                TopHeader(onOpenSettings = {})
                Spacer(Modifier.height(16.dp))
                CanvasSizeCard(
                    width = 999,
                    height = 999,
                    onWidthClick = {},
                    onHeightClick = {},
                    onSwap = {},
                )
                Spacer(Modifier.height(12.dp))
                GenerateCard(onClick = {})
            }
        }
    }
}

// ==================== 1. 顶部标题 ====================

@Composable
private fun TopHeader(
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Logo（圆形底 + 旧版像素图标）
        Box(
            modifier = Modifier
                .size(44.dp)
                .circleIconBg(),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(Res.drawable.old_ic_app_icon),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            // 标题：单一字体 + 整字渐变（淡紫 → 天依蓝），干净利落
            Text(
                text = "PixelColorPicker",
                style = TextStyle(
                    brush = Brush.linearGradient(
                        listOf(Color(0xFFA99BFF), Color(0xFF66CCFF)),
                    ),
                    fontSize = 23.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.4.sp,
                ),
            )
            Spacer(Modifier.height(1.dp))
            Text(
                text = stringResource(Res.string.home_subtitle),
                color = PcpColors.TextSecondary,
                fontSize = 11.sp,
                maxLines = 1,
                autoSize = TextAutoSize.StepBased(
                    minFontSize = 8.sp,
                    maxFontSize = 11.sp,
                    stepSize = 0.5.sp,
                ),
            )
        }
        // 设置按钮（圆形底 + 齿轮图标；marginStart4dp / marginTop-2dp / marginEnd8dp）
        val interaction = remember { MutableInteractionSource() }
        Box(
            modifier = Modifier
                .offset(y = (-2).dp)
                .padding(start = 4.dp, end = 8.dp)
                .size(32.dp)
                .circleIconBg()
                .clickable(interactionSource = interaction, indication = null, onClick = onOpenSettings),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(Res.string.settings),
                tint = PcpColors.TextIcon,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

// ==================== 2. 画布尺寸卡片 ====================

@Composable
private fun CanvasSizeCard(
    width: Int,
    height: Int,
    onWidthClick: () -> Unit,
    onHeightClick: () -> Unit,
    onSwap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GlowCard(modifier = modifier) {
        // 标题行
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(Res.drawable.ic_canvas_size),
                contentDescription = null,
                tint = PcpColors.TextPrimary,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(Res.string.canvas_size),
                color = PcpColors.TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                autoSize = TextAutoSize.StepBased(
                    minFontSize = 11.sp,
                    maxFontSize = 16.sp,
                    stepSize = 0.5.sp,
                ),
            )
        }
        Spacer(Modifier.height(12.dp))
        // W [32] ⇄ H [16]
        Row(verticalAlignment = Alignment.CenterVertically) {
            SizeInput(
                label = "W",
                value = width,
                onClick = onWidthClick,
                modifier = Modifier.weight(1f),
            )
            // 交换按钮（复刻旧版 ImageButton：白色图标 + borderless 涟漪）
            val swapInteraction = remember { MutableInteractionSource() }
            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .size(16.dp)
                    .clickable(
                        interactionSource = swapInteraction,
                        indication = ripple(bounded = false, color = PcpColors.TextPrimary.copy(alpha = 0.2f)),
                        onClick = onSwap,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.old_ic_swap),
                    contentDescription = stringResource(Res.string.swap_size),
                    tint = PcpColors.TextPrimary,
                    modifier = Modifier.size(16.dp),
                )
            }
            SizeInput(
                label = "H",
                value = height,
                onClick = onHeightClick,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun SizeInput(
    label: String,
    value: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interaction = remember { MutableInteractionSource() }
    Row(
        modifier = modifier
            .height(40.dp)
            .inputBoxBorder(radius = 6.dp)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            color = PcpColors.TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.width(4.dp))
        // 值：30dp 宽居中（支持 3 位数字如 999；两位数字约 14sp）
        Text(
            text = value.toString(),
            color = PcpColors.TextPrimary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            modifier = Modifier.width(30.dp),
        )
        Icon(
            painter = painterResource(Res.drawable.old_ic_arrow_down),
            contentDescription = null,
            tint = PcpColors.TextPrimary,
            modifier = Modifier.size(12.dp),
        )
    }
}

// ==================== 3. 生成卡片 ====================

@Composable
private fun GenerateCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val shape = RoundedCornerShape(18.dp)
    // 强调色：珊瑚橙（与折叠按钮同色系，暖色在蓝紫主调中自然突出）
    val accent = Color(0xFFFF8A65)
    Row(
        modifier = modifier
            .clip(shape)
            .glowCardBorder(
                radius = 18.dp,
                backgroundColor = PcpColors.BgCardPrimary,
                accentStroke = accent.copy(alpha = if (pressed) 0.98f else 0.80f),
            )
            .clickable(
                interactionSource = interaction,
                indication = ripple(color = accent.copy(alpha = 0.18f)),
                onClick = onClick,
            )
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(accent.copy(alpha = 0.16f))
                .border(1.dp, accent.copy(alpha = 0.50f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_generate),
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(21.dp),
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(Res.string.generate),
                color = PcpColors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                autoSize = TextAutoSize.StepBased(
                    minFontSize = 10.sp,
                    maxFontSize = 14.sp,
                    stepSize = 0.5.sp,
                ),
            )
            Text(
                text = stringResource(Res.string.generate_hint),
                color = PcpColors.TextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                maxLines = 2,
                autoSize = TextAutoSize.StepBased(
                    minFontSize = 8.sp,
                    maxFontSize = 11.sp,
                    stepSize = 0.5.sp,
                ),
            )
        }
        Spacer(Modifier.width(6.dp))
        // 小箭头暗示"立即执行"
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = accent,
            modifier = Modifier.size(18.dp),
        )
    }
}

// ==================== 通用发光卡片 ====================

@Composable
private fun GlowCard(
    modifier: Modifier = Modifier,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .glowCardBorder(radius = 16.dp)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        content = content,
    )
}