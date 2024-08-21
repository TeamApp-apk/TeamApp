package com.example.TeamApp.event
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.TeamApp.R
import com.example.TeamApp.excludedUI.ActivityCard
import com.example.TeamApp.event.CreateEventViewModel
import kotlinx.coroutines.delay
@Composable
fun CreateEventv2() {
    val gradientColors = listOf(
        Color(0xFFE8E8E8),
        Color(0xFF007BFF)
    )

    // Outer Box filling the whole screen with gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = gradientColors))
            .padding(horizontal = 12.dp, vertical = 36.dp),
        contentAlignment = Alignment.Center // Centering the content
    ) {
        // Inner Box centered with specific dimensions and background color
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
                .background(color = Color(0xFFF2F2F2), shape = RoundedCornerShape(size = 16.dp))
        ){Column(modifier = Modifier.padding(10.dp)){
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                verticalAlignment = Alignment.Top,modifier = Modifier
                    .width(332.dp)
                    .height(45.dp)
                    .padding(10.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_icon),
                    contentDescription = "arrow",
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {

                        }
                        .size(24.dp)

                )
                Text(modifier = Modifier
                    .width(272.dp)
                    .height(25.dp),
                    text = "Stwórz wydarzenie",
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 25.sp,
                        fontFamily = FontFamily(Font(R.font.robotobold)),
                        fontWeight = FontWeight(900),
                        color = Color(0xFF003366),
                        textAlign = TextAlign.Right,
                    )
                )

            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(modifier = Modifier
                .width(328.dp)
                .height(20.dp)
                .padding(horizontal = 16.dp)
                ,
                text = "Dodaj opis",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 12.sp,
                    fontFamily = FontFamily(Font(R.font.robotoregular)),
                    fontWeight = FontWeight(900),
                    color = Color(0xFF003366),
                )
            )
            TextFieldForEventDescription()
            Box(modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center) ){
                LocationButton()
            }

            Box(contentAlignment=Alignment.Center, modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                ) {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    ActivityList()
                    ParticipantsNumber()

                }
            }
        }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun CreateEventv2Preview() {
    CreateEventv2()
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldForEventDescription() {
    var textState by remember { mutableStateOf("") }

    // Styl dla tekstu w polu tekstowym
    val textFieldTextStyle = TextStyle(
        fontSize = 16.sp,
        color = Color.Black,
        fontFamily = FontFamily(Font(R.font.robotoregular)),
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Start // Wyrównanie tekstu do lewej
    )

    // Styl dla placeholdera
    val placeholderTextStyle = TextStyle(
        fontSize = 12.sp,
        lineHeight = 20.sp,
        fontFamily = FontFamily(Font(R.font.robotoregular)),
        fontWeight = FontWeight.Light,
        fontStyle = FontStyle.Italic,
        color = Color.Gray,
        textAlign = TextAlign.Left // Wyrównanie placeholdera do lewej
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        // OutlinedTextField z placeholderem
        TextField(
            value = textState,
            onValueChange = { textState = it },
            placeholder = {
                Text(
                    text = "Opisz pokrótce szczegóły wydarzenia...",
                    style = placeholderTextStyle
                )
            },
            textStyle = textFieldTextStyle, // Styl dla wpisanego tekstu
            modifier = Modifier
                .fillMaxWidth()
                .width(328.dp)
                .height(224.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent, // Przezroczyste tło
                focusedIndicatorColor = Color.Transparent, // Przezroczysta ramka, gdy aktywny
                unfocusedIndicatorColor = Color.Transparent, // Przezroczysta ramka, gdy nieaktywny
                disabledIndicatorColor = Color.Transparent // Przezroczysta ramka, gdy wyłączony
            )
        )
    }
}
@Composable
fun LocationButton() {
    Button(
        onClick = {},
        modifier = Modifier
            .width(280.dp)
            .height(45.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White, // Tło przycisku
            contentColor = Color.Black // Kolor tekstu na przycisku
        ),
        shape = RoundedCornerShape(16.dp), // Kształt zaokrąglonych rogów
        // Opcjonalnie, możesz dostosować cień
    ) {
        Text(
            text = "lokalizacja",
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 25.sp,
                fontFamily = FontFamily(Font(R.font.robotoregular)),
                fontWeight = FontWeight(400),
                color = Color.Black,

                )
        )
    }
}
@Composable
fun ActivityList() {
    Button(
        onClick = {},
        modifier = Modifier
            .width(136.dp)
            .height(45.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White, // Tło przycisku
            contentColor = Color.Black // Kolor tekstu na przycisku
        ),
        shape = RoundedCornerShape(16.dp) // Kształt zaokrąglonych rogów
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), // Zapełnia całą szerokość
            horizontalArrangement = Arrangement.SpaceBetween, // Rozdziel tekst i obrazek
            verticalAlignment = Alignment.CenterVertically // Wyrównanie w pionie
        ) {
            Text(
                text = "aktywnosc",
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 25.sp,
                    fontFamily = FontFamily(Font(R.font.robotoregular)),
                    fontWeight = FontWeight(400),
                    color = Color.Black,
                )
            )
            Image(
                painter = painterResource(id = R.drawable.chevron_down),
                contentDescription = "image description",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParticipantsNumber() {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        placeholder = {
            Text(
                text = "liczba osób",
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 25.sp,
                    fontFamily = FontFamily(Font(R.font.robotoregular)),
                    fontWeight = FontWeight(400),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )
        },
        modifier = Modifier
            .width(136.dp)
            .height(45.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.White, // Tło pola tekstowego
            focusedBorderColor = Color.Transparent, // Usunięcie ramki przy skupieniu
            unfocusedBorderColor = Color.Transparent, // Usunięcie ramki gdy nieaktywne

        ),
        shape = RoundedCornerShape(16.dp), // Zaokrąglenie rogów
        singleLine = true // Pole będzie jednowierszowe
    )
}


