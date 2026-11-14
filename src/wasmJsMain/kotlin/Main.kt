import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

/* ===== 复刻 PcpStyle.kt 的色值 ===== */
val BgPage = Color(0xFF08090F)
val BgCardPrimary = Color(0xFF0E1016)
val GlowStart = Color(0xFF525777)
val GlowEnd = Color(0xFF474E68)
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFF9FA0A5)
val AccentPurple = Color(0xFF66CCFF)
val CircleIconBg = Color(0xFF1A1A22)
val CircleIconStroke = Color(0xFF2E2E3A)

/* ===== 复刻 PcpStyle.kt 的 glowCardBorder ===== */
fun Modifier.glowCardBorder(
    radius: androidx.compose.ui.unit.Dp = 16.dp,
    inset: androidx.compose.ui.unit.Dp = 1.dp,
    backgroundColor: Color = BgCardPrimary,
    accentStroke: Color? = null,
): Modifier = this.drawWithCache {
    val r = radius.toPx()
    val i = inset.toPx()
    val brush = Brush.linearGradient(
        colors = listOf(GlowStart, GlowEnd),
        start = Offset.Zero,
        end = Offset(size.width, size.height),
    )
    val accentW = 1.5.dp.toPx()
    onDrawWithContent {
        drawRoundRect(brush, Offset.Zero, size, CornerRadius(r))
        drawRoundRect(
            backgroundColor,
            Offset(i, i),
            Size(size.width - i * 2, size.height - i * 2),
            CornerRadius(r - i),
        )
        if (accentStroke != null) {
            val gap = i + accentW / 2f
            drawRoundRect(
                accentStroke,
                Offset(gap, gap),
                Size(size.width - gap * 2, size.height - gap * 2),
                CornerRadius((r - gap).coerceAtLeast(0f)),
                style = Stroke(width = accentW),
            )
        }
        drawContent()
    }
}

/* ===== 复刻 circleIconBg ===== */
fun Modifier.circleIconBg(): Modifier = this.drawWithContent {
    val r = size.minDimension / 2f
    val w = 1.dp.toPx()
    drawCircle(CircleIconBg, radius = r)
    drawCircle(CircleIconStroke, radius = r - w / 2, style = Stroke(w))
    drawContent()
}

@Composable
fun Demo() {
    Box(
        modifier = Modifier.fillMaxSize().background(BgPage).padding(16.dp),
    ) {
        Column {
            /* 1. 渐变标题（复刻 TopHeader） */
            Text(
                text = "PixelColorPicker",
                style = TextStyle(
                    brush = Brush.linearGradient(listOf(Color(0xFFA99BFF), Color(0xFF66CCFF))),
                    fontSize = 23.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.4.sp,
                ),
            )
            Spacer(Modifier.height(4.dp))
            Text("图片像素化 / 色板码转换器", color = TextSecondary, fontSize = 11.sp)

            Spacer(Modifier.height(20.dp))

            /* 2. 发光边框卡片（复刻 GlowCard） */
            Box(
                Modifier
                    .fillMaxWidth()
                    .glowCardBorder(radius = 16.dp)
                    .padding(14.dp),
            ) {
                Column {
                    Text("画布尺寸", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SizeBox("W", "999", Modifier.weight(1f))
                        SizeBox("H", "999", Modifier.weight(1f))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            /* 3. 带强调描边的卡片（复刻 GenerateCard） */
            Box(
                Modifier
                    .fillMaxWidth()
                    .glowCardBorder(radius = 18.dp, accentStroke = Color(0xFFFF8A65).copy(alpha = 0.8f))
                    .padding(14.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(40.dp).clip(CircleShape)
                            .background(Color(0xFFFF8A65).copy(alpha = 0.16f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("→", color = Color(0xFFFF8A65), fontSize = 18.sp)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("生成像素画", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("按当前参数生成", color = TextSecondary, fontSize = 11.sp)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            /* 4. 圆形图标底（复刻 circleIconBg） */
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(44.dp).circleIconBg(), contentAlignment = Alignment.Center) {
                    Text("◆", color = AccentPurple, fontSize = 18.sp)
                }
                Spacer(Modifier.width(12.dp))
                Text("circleIconBg 44dp 验证", color = TextSecondary, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun SizeBox(label: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(40.dp)
            .glowCardBorder(radius = 6.dp, backgroundColor = Color(0xFF0A0C12))
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(6.dp))
        Text(value, color = TextPrimary, fontSize = 14.sp)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val body = document.body ?: return
    ComposeViewport(body) {
        Demo()
    }
}