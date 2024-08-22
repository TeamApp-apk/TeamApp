package com.example.TeamApp.event
import DescriptionTextField
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
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
import com.example.TeamApp.R
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.util.Calendar
import kotlinx.coroutines.launch

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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .background(color = Color(0xFFF2F2F2), shape = RoundedCornerShape(size = 16.dp))
        ) {
            // Inner Box centered with specific dimensions and background color
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
                    .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
                    .background(color = Color(0xFFF2F2F2), shape = RoundedCornerShape(size = 16.dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically, // Ensures vertical alignment of icon and text
                    horizontalArrangement = Arrangement.SpaceBetween, // Keeps everything aligned to the left
                    modifier = Modifier
                        .fillMaxWidth() // Makes the Row take up full width
                        .padding(bottom = 24.dp)

                ) {
                        IconButton(
                            onClick = { /* Handle back click */ },
                            modifier = Modifier
                            .size(26.dp),// Set size for the icon
                        ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrowleft),
                            contentDescription = "Back Icon"
                        ) }

                    Text(
                        text = "Stwórz wydarzenie",
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.robotobold)),
                            fontWeight = FontWeight(900),
                            color = Color(0xFF003366),
                            textAlign = TextAlign.Start, // Align text to the start
                            lineHeight = 25.sp,
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterVertically) // Ensure text is vertically aligned with icon
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(modifier = Modifier
                    .width(328.dp)
                    .height(20.dp)
                    .padding(horizontal = 16.dp)
                    ,
                    text = "Dodaj opis",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 12.sp,
                        fontFamily = FontFamily(Font(R.font.robotobold)),
                        fontWeight = FontWeight(800),
                        color = Color(0xFF003366),
                    )
                )
                DescriptionTextField(label = "Opisz pokrótce szczegóły wydarzenia...", isEditable = true)
                Column(
                    modifier = Modifier.padding(horizontal = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    SearchStreetField()
                    Row(
                        modifier = Modifier
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchStreetField() {
    var query by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf(listOf<String>()) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Stylizowane TextField przypominające przycisk
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                coroutineScope.launch {
                    suggestions = getPlaceSuggestions(it)
                }
            },
            label = {
                Text(
                    text = "lokalizacja",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center, // Wycentrowanie tekstu
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),  // Zaokrąglone rogi
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue, // Kolor obramowania w trakcie focusa
                unfocusedBorderColor = Color(0xFFFFFFFF), // Kolor obramowania, gdy pole nie jest aktywne
                cursorColor = Color.Black, // Kolor kursora
                containerColor = Color(0xFFFFFFFF)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // LazyColumn do wyświetlania listy sugestii
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(suggestions) { suggestion ->
                SuggestionItem(suggestion)
            }
        }
    }
}

@Composable
fun SuggestionItem(suggestion: String) {
    Text(
        text = suggestion,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                // Działanie na kliknięcie podpowiedzi (możesz tu dodać nawigację lub inny kod)
                println("Wybrano: $suggestion")
            }
    )
}

suspend fun getPlaceSuggestions(query: String): List<String> {
    // Tu można zaimplementować logikę pobierania danych z Google Places API
    // Na razie zwracamy fikcyjne dane
    return if (query.isNotEmpty()) {
        listOf("Warszawa, ul. Piękna", "Kraków, ul. Floriańska", "Gdańsk, ul. Długa")
    } else {
        emptyList()
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun MyDateTimePicker() {
    // Zmienna do przechowywania wybranej daty i godziny
    var selectedDateTime by remember { mutableStateOf("") }

    // Funkcja do otwarcia DatePickerDialog
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    // Zmienna do przechowywania wybranej godziny
    var selectedTime by remember { mutableStateOf("") }

    // TimePickerDialog
    val timePickerDialog = TimePickerDialog(
        LocalContext.current,
        R.style.CustomDatePickerTheme,
        { _, pickedHour, pickedMinute ->
            selectedTime = String.format("%02d:%02d", pickedHour, pickedMinute)
            // Zaktualizuj datę i godzinę razem
            selectedDateTime += " $selectedTime"
        }, hour, minute, true
    )

    // DatePickerDialog
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        R.style.CustomTimePickerTheme,
        { _, pickedYear, pickedMonth, pickedDayOfMonth ->
            // Zaktualizuj zmienną po wyborze daty
            selectedDateTime = "$pickedDayOfMonth/${pickedMonth + 1}/$pickedYear"
            // Po wyborze daty, otwórz TimePickerDialog
            timePickerDialog.show()
        }, year, month, day
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Przycisk z dynamicznie zmienianym tekstem
        Button(
            onClick = { datePickerDialog.show() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,  // Ustawienie koloru przycisku na biały
                contentColor = Color.Black      // Ustawienie koloru tekstu na czarny
            ), modifier = Modifier
                .width(280.dp)
                .height(45.dp)
                .background(color = Color.White, shape = RoundedCornerShape(size = 16.dp))

        ) {
            Text(text = if (selectedDateTime.isEmpty()) "Data i godzina" else selectedDateTime,
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
}




@Preview(showBackground = false)
@Composable
fun CreateEventv2Preview() {
    CreateEventv2()
}



