package com.e2e.memopad.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

// ダークネイビー背景に最適化した色スキーム
private val DarkColorScheme = darkColorScheme(
    primary = ButtonBlue,           // #007AFF - ボタン・ハイライト用（高コントラスト）
    onPrimary = White,              // プライマリー上のテキスト
    primaryContainer = DarkNavy,    // プライマリーコンテナ背景
    onPrimaryContainer = LightBlue, // コンテナ内テキスト
    
    secondary = LightBlue,          // #ADD8E6 - セカンダリーアクション
    onSecondary = DarkNavy,         // セカンダリー上のテキスト
    secondaryContainer = MemoCardDarkGray, // セカンダリーコンテナ
    onSecondaryContainer = LightGray,      // コンテナ内テキスト
    
    tertiary = LightGreen,          // #90EE90 - サードアクセント
    onTertiary = DarkNavy,          // 上のテキスト
    tertiaryContainer = MemoCardDarkGray,  // コンテナ
    onTertiaryContainer = LightGray,       // コンテナ内テキスト
    
    background = DarkNavy,          // #001F3F - 背景
    onBackground = White,           // 背景上のテキスト
    
    surface = MemoCardDarkGray,     // #3A3A3A - カード・サーフェス
    onSurface = White,              // サーフェス上のテキスト
    
    error = Color(0xFFFF6B6B),      // エラー色
    onError = DarkNavy              // エラー上のテキスト
)

// ライトテーマ（標準的なライト背景用）
private val LightColorScheme = lightColorScheme(
    primary = ButtonBlue,           // #007AFF - ボタン・ハイライト用
    onPrimary = White,              // 上のテキスト
    primaryContainer = Color(0xFFE3F2FD), // ライト背景のコンテナ
    onPrimaryContainer = Color(0xFF003DA6), // コンテナ内テキスト
    
    secondary = Color(0xFF0860CA),  // セカンダリー
    onSecondary = White,            // 上のテキスト
    secondaryContainer = Color(0xFFD4E4FF), // ライトコンテナ
    onSecondaryContainer = Color(0xFF003DA6), // コンテナ内テキスト
    
    tertiary = Color(0xFF0860CA),   // サード
    onTertiary = White,             // 上のテキスト
    tertiaryContainer = Color(0xFFD4E4FF), // ライトコンテナ
    onTertiaryContainer = Color(0xFF003DA6), // コンテナ内テキスト
    
    background = Color(0xFFFAFAFA), // ライト背景
    onBackground = Color(0xFF1C1C1C), // 背景上のテキスト
    
    surface = Color(0xFFFFFFFF),    // ライトサーフェス
    onSurface = Color(0xFF1C1C1C),  // 上のテキスト
    
    error = Color(0xFFB3261E),      // エラー色
    onError = Color(0xFFFFFFFF)     // エラー上のテキスト
)

@Composable
fun Typography() = androidx.compose.material3.Typography()

private val Shape = androidx.compose.material3.Shapes(
    small = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    large = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
)

@Composable
fun MemopadTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        shapes = Shape,
        content = content
    )
}
