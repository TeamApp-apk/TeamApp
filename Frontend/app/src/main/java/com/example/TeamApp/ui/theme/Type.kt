package com.example.ui.theme

import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import kotlin.math.ceil
import kotlin.math.floor

val metrics = Resources.getSystem().displayMetrics
val density = metrics.density / 2

val AppTypography = Typography()

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val fontName = GoogleFont("Roboto")

val fontFamily = FontFamily(
    Font(
        googleFont = fontName,
        fontProvider = provider,
        weight = FontWeight.Bold,
        style = FontStyle.Italic
    )
)

val textInUpperBoxForgotPassword = TextStyle(
    fontSize = 25.sp * com.example.TeamApp.auth.density,
    fontFamily = fontFamily,
    fontWeight = FontWeight(500),
    color = Color(0xFF003366),
)

val textInLowerBoxForgotPassword = TextStyle(
    fontSize = 15.sp,
    lineHeight = 25.sp,
    fontFamily = FontFamily(Font(R.font.proximanovabold)),
    fontWeight = FontWeight(400),
    color = Color.Black
)

val sendButton = TextStyle (
    fontSize = 18.sp,
    fontFamily = FontFamily(Font(R.font.proximanovabold)),
    fontWeight = FontWeight(600),
    color = Color(0xFF003366),
    textAlign = TextAlign.Center,
    letterSpacing = 1.sp
)

val googleAuthStyle = TextStyle (
    fontSize = 18.sp,
    fontFamily = FontFamily(Font(R.font.proximanovabold)),
    fontWeight = FontWeight(500),
    color = Color(0xFF003366),
    textAlign = TextAlign.Center,
    letterSpacing = 1.sp
)

val forgotPasswordLogin = TextStyle(
    fontSize = 14.sp,
    lineHeight = 23.sp,
    fontFamily = FontFamily(Font(R.font.proximanovabold)),
    fontWeight = FontWeight(400),
    color = Color(0xFF003366),
    textAlign = TextAlign.Right,
)

val buttonLogIn = TextStyle(
    fontSize = 16.sp,
    fontFamily = FontFamily(Font(R.font.proximanovaregular)),
    fontWeight = FontWeight.Normal,
    color = Color(0xffe0e0e0),
    textAlign = TextAlign.Center,
    letterSpacing = 1.sp
)