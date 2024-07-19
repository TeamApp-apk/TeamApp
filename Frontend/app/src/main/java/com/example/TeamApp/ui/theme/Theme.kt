package com.example.compose
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.ui.theme.AppTypography

@Immutable
data class ExtendedColorScheme(
    val mainBackGroundColor: ColorFamily,
    val panel: ColorFamily,
    val accent: ColorFamily,
    val complementary: ColorFamily,
    val text: ColorFamily,
    val textOnTheme: ColorFamily,
    val interactive: ColorFamily,
    val unactive: ColorFamily,
)

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

val extendedLight = ExtendedColorScheme(
  mainBackGroundColor = ColorFamily(
  mainBackGroundColorLight,
  onMainBackGroundColorLight,
  mainBackGroundColorContainerLight,
  onMainBackGroundColorContainerLight,
  ),
  panel = ColorFamily(
  panelLight,
  onPanelLight,
  panelContainerLight,
  onPanelContainerLight,
  ),
  accent = ColorFamily(
  accentLight,
  onAccentLight,
  accentContainerLight,
  onAccentContainerLight,
  ),
  complementary = ColorFamily(
  complementaryLight,
  onComplementaryLight,
  complementaryContainerLight,
  onComplementaryContainerLight,
  ),
  text = ColorFamily(
  textLight,
  onTextLight,
  textContainerLight,
  onTextContainerLight,

  ),
  textOnTheme = ColorFamily(
  textOnThemeLight,
  onTextOnThemeLight,
  textOnThemeContainerLight,
  onTextOnThemeContainerLight,
  ),
  interactive = ColorFamily(
  interactiveLight,
  onInteractiveLight,
  interactiveContainerLight,
  onInteractiveContainerLight,
  ),
  unactive = ColorFamily(
  unactiveLight,
  onUnactiveLight,
  unactiveContainerLight,
  onUnactiveContainerLight,
  ),
)

val extendedDark = ExtendedColorScheme(
  mainBackGroundColor = ColorFamily(
  mainBackGroundColorDark,
  onMainBackGroundColorDark,
  mainBackGroundColorContainerDark,
  onMainBackGroundColorContainerDark,
  ),
  panel = ColorFamily(
  panelDark,
  onPanelDark,
  panelContainerDark,
  onPanelContainerDark,
  ),
  accent = ColorFamily(
  accentDark,
  onAccentDark,
  accentContainerDark,
  onAccentContainerDark,
  ),
  complementary = ColorFamily(
  complementaryDark,
  onComplementaryDark,
  complementaryContainerDark,
  onComplementaryContainerDark,
  ),
  text = ColorFamily(
  textDark,
  onTextDark,
  textContainerDark,
  onTextContainerDark,
  ),
  textOnTheme = ColorFamily(
  textOnThemeDark,
  onTextOnThemeDark,
  textOnThemeContainerDark,
  onTextOnThemeContainerDark,
  ),
  interactive = ColorFamily(
  interactiveDark,
  onInteractiveDark,
  interactiveContainerDark,
  onInteractiveContainerDark,
  ),
  unactive = ColorFamily(
  unactiveDark,
  onUnactiveDark,
  unactiveContainerDark,
  onUnactiveContainerDark,
  ),
)

val extendedLightMediumContrast = ExtendedColorScheme(
  mainBackGroundColor = ColorFamily(
  mainBackGroundColorLightMediumContrast,
  onMainBackGroundColorLightMediumContrast,
  mainBackGroundColorContainerLightMediumContrast,
  onMainBackGroundColorContainerLightMediumContrast,
  ),
  panel = ColorFamily(
  panelLightMediumContrast,
  onPanelLightMediumContrast,
  panelContainerLightMediumContrast,
  onPanelContainerLightMediumContrast,
  ),
  accent = ColorFamily(
  accentLightMediumContrast,
  onAccentLightMediumContrast,
  accentContainerLightMediumContrast,
  onAccentContainerLightMediumContrast,
  ),
  complementary = ColorFamily(
  complementaryLightMediumContrast,
  onComplementaryLightMediumContrast,
  complementaryContainerLightMediumContrast,
  onComplementaryContainerLightMediumContrast,
  ),
  text = ColorFamily(
  textLightMediumContrast,
  onTextLightMediumContrast,
  textContainerLightMediumContrast,
  onTextContainerLightMediumContrast,
  ),
  textOnTheme = ColorFamily(
  textOnThemeLightMediumContrast,
  onTextOnThemeLightMediumContrast,
  textOnThemeContainerLightMediumContrast,
  onTextOnThemeContainerLightMediumContrast,
  ),
  interactive = ColorFamily(
  interactiveLightMediumContrast,
  onInteractiveLightMediumContrast,
  interactiveContainerLightMediumContrast,
  onInteractiveContainerLightMediumContrast,
  ),
  unactive = ColorFamily(
  unactiveLightMediumContrast,
  onUnactiveLightMediumContrast,
  unactiveContainerLightMediumContrast,
  onUnactiveContainerLightMediumContrast,
  ),
)

val extendedLightHighContrast = ExtendedColorScheme(
  mainBackGroundColor = ColorFamily(
  mainBackGroundColorLightHighContrast,
  onMainBackGroundColorLightHighContrast,
  mainBackGroundColorContainerLightHighContrast,
  onMainBackGroundColorContainerLightHighContrast,
  ),
  panel = ColorFamily(
  panelLightHighContrast,
  onPanelLightHighContrast,
  panelContainerLightHighContrast,
  onPanelContainerLightHighContrast,
  ),
  accent = ColorFamily(
  accentLightHighContrast,
  onAccentLightHighContrast,
  accentContainerLightHighContrast,
  onAccentContainerLightHighContrast,
  ),
  complementary = ColorFamily(
  complementaryLightHighContrast,
  onComplementaryLightHighContrast,
  complementaryContainerLightHighContrast,
  onComplementaryContainerLightHighContrast,
  ),
  text = ColorFamily(
  textLightHighContrast,
  onTextLightHighContrast,
  textContainerLightHighContrast,
  onTextContainerLightHighContrast,
  ),
  textOnTheme = ColorFamily(
  textOnThemeLightHighContrast,
  onTextOnThemeLightHighContrast,
  textOnThemeContainerLightHighContrast,
  onTextOnThemeContainerLightHighContrast,
  ),
  interactive = ColorFamily(
  interactiveLightHighContrast,
  onInteractiveLightHighContrast,
  interactiveContainerLightHighContrast,
  onInteractiveContainerLightHighContrast,
  ),
  unactive = ColorFamily(
  unactiveLightHighContrast,
  onUnactiveLightHighContrast,
  unactiveContainerLightHighContrast,
  onUnactiveContainerLightHighContrast,
  ),
)

val extendedDarkMediumContrast = ExtendedColorScheme(
  mainBackGroundColor = ColorFamily(
  mainBackGroundColorDarkMediumContrast,
  onMainBackGroundColorDarkMediumContrast,
  mainBackGroundColorContainerDarkMediumContrast,
  onMainBackGroundColorContainerDarkMediumContrast,
  ),
  panel = ColorFamily(
  panelDarkMediumContrast,
  onPanelDarkMediumContrast,
  panelContainerDarkMediumContrast,
  onPanelContainerDarkMediumContrast,
  ),
  accent = ColorFamily(
  accentDarkMediumContrast,
  onAccentDarkMediumContrast,
  accentContainerDarkMediumContrast,
  onAccentContainerDarkMediumContrast,
  ),
  complementary = ColorFamily(
  complementaryDarkMediumContrast,
  onComplementaryDarkMediumContrast,
  complementaryContainerDarkMediumContrast,
  onComplementaryContainerDarkMediumContrast,
  ),
  text = ColorFamily(
  textDarkMediumContrast,
  onTextDarkMediumContrast,
  textContainerDarkMediumContrast,
  onTextContainerDarkMediumContrast,
  ),
  textOnTheme = ColorFamily(
  textOnThemeDarkMediumContrast,
  onTextOnThemeDarkMediumContrast,
  textOnThemeContainerDarkMediumContrast,
  onTextOnThemeContainerDarkMediumContrast,
  ),
  interactive = ColorFamily(
  interactiveDarkMediumContrast,
  onInteractiveDarkMediumContrast,
  interactiveContainerDarkMediumContrast,
  onInteractiveContainerDarkMediumContrast,
  ),
  unactive = ColorFamily(
  unactiveDarkMediumContrast,
  onUnactiveDarkMediumContrast,
  unactiveContainerDarkMediumContrast,
  onUnactiveContainerDarkMediumContrast,
  ),
)

val extendedDarkHighContrast = ExtendedColorScheme(
  mainBackGroundColor = ColorFamily(
  mainBackGroundColorDarkHighContrast,
  onMainBackGroundColorDarkHighContrast,
  mainBackGroundColorContainerDarkHighContrast,
  onMainBackGroundColorContainerDarkHighContrast,
  ),
  panel = ColorFamily(
  panelDarkHighContrast,
  onPanelDarkHighContrast,
  panelContainerDarkHighContrast,
  onPanelContainerDarkHighContrast,
  ),
  accent = ColorFamily(
  accentDarkHighContrast,
  onAccentDarkHighContrast,
  accentContainerDarkHighContrast,
  onAccentContainerDarkHighContrast,
  ),
  complementary = ColorFamily(
  complementaryDarkHighContrast,
  onComplementaryDarkHighContrast,
  complementaryContainerDarkHighContrast,
  onComplementaryContainerDarkHighContrast,
  ),
  text = ColorFamily(
  textDarkHighContrast,
  onTextDarkHighContrast,
  textContainerDarkHighContrast,
  onTextContainerDarkHighContrast,
  ),
  textOnTheme = ColorFamily(
  textOnThemeDarkHighContrast,
  onTextOnThemeDarkHighContrast,
  textOnThemeContainerDarkHighContrast,
  onTextOnThemeContainerDarkHighContrast,
  ),
  interactive = ColorFamily(
  interactiveDarkHighContrast,
  onInteractiveDarkHighContrast,
  interactiveContainerDarkHighContrast,
  onInteractiveContainerDarkHighContrast,
  ),
  unactive = ColorFamily(
  unactiveDarkHighContrast,
  onUnactiveDarkHighContrast,
  unactiveContainerDarkHighContrast,
  onUnactiveContainerDarkHighContrast,
  ),
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable() () -> Unit
) {
  val colorScheme = when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
          val context = LocalContext.current
          if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      
      darkTheme -> darkScheme
      else -> lightScheme
  }
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      window.statusBarColor = colorScheme.primary.toArgb()
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = AppTypography,
    content = content
  )
}

