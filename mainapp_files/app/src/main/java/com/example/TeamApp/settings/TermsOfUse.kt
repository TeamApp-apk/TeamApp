package com.example.TeamApp.settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.TeamApp.R

@Composable
fun TermsOfUse(navController: NavController) {
    val gradientColors = listOf(
        Color(0xFFE8E8E8),
        Color(0xFF007BFF)
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = gradientColors))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .width(360.dp)
                .height(764.dp)
                .background(color = Color(0xFFF2F2F2), shape = RoundedCornerShape(size = 16.dp))
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_icon),
                    contentDescription = "arrow",
                    modifier = Modifier
                        .clickable { navController.popBackStack() }
                        .size(24.dp)
                        .padding(1.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Regulamin",
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 25.sp,
                        fontFamily = FontFamily(Font(R.font.proximanovabold)),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF003366),
                        textAlign = TextAlign.Start,
                    )
                )
            }

            Box(
                modifier = Modifier
                    .width(331.dp)
                    .height(617.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = """
                    REGULAMIN KORZYSTANIA Z APLIKACJI TEAMAPP

                    1. POSTANOWIENIA OGÓLNE
                    Aplikacja TeamApp jest własnością firmy XYZ Sp. z o.o. i służy do zarządzania zespołami i wydarzeniami.

                    2. WARUNKI KORZYSTANIA
                    Korzystanie z aplikacji wymaga zaakceptowania niniejszego regulaminu. Użytkownik zobowiązany jest do korzystania z aplikacji zgodnie z jej przeznaczeniem.

                    3. REJESTRACJA I BEZPIECZEŃSTWO
                    Użytkownik zobowiązany jest do podania prawdziwych danych podczas rejestracji. Zabrania się udostępniania konta osobom trzecim.

                    4. PRAWA I OBOWIĄZKI UŻYTKOWNIKA
                    Użytkownik ma prawo do korzystania z aplikacji w zakresie zgodnym z jej funkcjonalnością oraz obowiązek niepodejmowania działań szkodzących aplikacji lub innym użytkownikom.

                    5. OCHRONA DANYCH OSOBOWYCH
                    Dane osobowe przetwarzane są zgodnie z obowiązującymi przepisami prawa oraz Polityką Prywatności.

                    6. POSTANOWIENIA KOŃCOWE
                    Regulamin może zostać zmieniony w dowolnym czasie. O zmianach użytkownik zostanie poinformowany przy pierwszym uruchomieniu aplikacji po wprowadzeniu zmian.

                    Dziękujemy za korzystanie z TeamApp.
                    """.trimIndent(),
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.proximanovalight)),
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF003366),
                        textAlign = TextAlign.Start,
                    )
                )
            }
        }
    }
}

