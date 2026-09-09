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

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF58A6FF),
    onPrimary = Color(0xFF0D1117),
    primaryContainer = Color(0xFF1F6FEB),
    onPrimaryContainer = Color(0xFFC9E4FF),
    secondary = Color(0xFF79C0FF),
    onSecondary = Color(0xFF0D1117),
    secondaryContainer = Color(0xFF0860CA),
    onSecondaryContainer = Color(0xFFC9E4FF),
    tertiary = Color(0xFF79C0FF),
    onTertiary = Color(0xFF0D1117),
    background = Color(0xFF0D1117),
    onBackground = Color(0xFFE6EDF3),
    surface = Color(0xFF161B22),
    onSurface = Color(0xFFE6EDF3),
    surfaceVariant = Color(0xFF30363D),
    onSurfaceVariant = Color(0xFF8B949E),
    error = Color(0xFFF85149),
    onError = Color(0xFF0D1117),
    errorContainer = Color(0xFF490202),
    onErrorContainer = Color(0xFFFFBBBE)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0969DA),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFDDF4FF),
    onPrimaryContainer = Color(0xFF001C47),
    secondary = Color(0xFF0860CA),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFDDF4FF),
    onSecondaryContainer = Color(0xFF001845),
    tertiary = Color(0xFF0860CA),
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFFFAFBFC),
    onBackground = Color(0xFF24292F),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF24292F),
    surfaceVariant = Color(0xFFEAEDF0),
    onSurfaceVariant = Color(0xFF57606A),
    error = Color(0xFFD1242F),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFEBEE),
    onErrorContainer = Color(0xFF42010C)
)

@Composable
fun Typography() = androidx.compose.material3.Typography()

private val Shape = androidx.compose.foundation.shape.Shapes(
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