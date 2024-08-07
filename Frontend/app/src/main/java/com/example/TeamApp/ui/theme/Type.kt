package com.example.ui.theme

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.isAvailableOnDevice
import androidx.compose.ui.unit.sp
import com.example.TeamApp.R
import kotlinx.coroutines.CoroutineExceptionHandler
import androidx.compose.ui.text.googlefonts.Font



val AppTypography = Typography()

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val fontName = GoogleFont("Lobster Two")

val fontFamily = FontFamily(
    Font(
        googleFont = fontName,
        fontProvider = provider,
        weight = FontWeight.Bold,
        style = FontStyle.Italic
    )
)