package com.example.TeamApp.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.TeamApp.R

@Composable
fun PrivacyPolicyScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    val gradientColors = listOf(Color(0xFFE8E8E8), Color(0xFF007BFF))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = gradientColors))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(16.dp)
                .background(color = Color(0xFFF2F2F2), shape = RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nagłówek
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(horizontal = 10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_icon),
                    contentDescription = "arrow",
                    modifier = Modifier
                        .clickable { navController.popBackStack() }
                        .size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Polityka prywatności",
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.proximanovabold)),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF003366),
                        textAlign = TextAlign.Center
                    )
                )
            }

            // Treść
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = """
Twoja prywatność jest dla nas ważna. Korzystając z aplikacji TeamApp, akceptujesz zasady opisane w niniejszej Polityce Prywatności.

1. Dane, które zbieramy:
- dane podstawowe (np. imię, lokalizacja, adres e-mail),
- informacje o Twojej aktywności sportowej,
- dane techniczne (model urządzenia, system, IP).

2. Cel przetwarzania danych:
- umawianie gier sportowych,
- poprawa działania aplikacji,
- bezpieczeństwo użytkowników.

3. Udostępnianie danych:
- wyłącznie partnerom technicznym i organom prawnym w razie potrzeby.

4. Analityka:
- anonimowe dane zbierane dla ulepszania działania aplikacji.

5. Twoje prawa:
- dostęp, poprawa, usunięcie danych.

6. Automatyczna akceptacja:
- korzystając z aplikacji, akceptujesz niniejszą politykę.

7. Kontakt: teamapp@support.com
""".trimIndent(),
                    style = TextStyle(
                        fontSize = 13.sp,
                        lineHeight = 22.sp,
                        fontFamily = FontFamily(Font(R.font.proximanovalight)),
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF003366),
                        textAlign = TextAlign.Justify
                    )
                )
            }

            // Przycisk
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(0.7f)
                    .height(48.dp)
            ) {
                Text(
                    text = "Rozumiem",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
