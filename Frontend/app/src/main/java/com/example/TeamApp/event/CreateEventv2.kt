package com.example.TeamApp.event
import DescriptionTextField
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.inputmethod.InputMethodManager
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.TeamApp.R
import com.example.TeamApp.data.Event
import com.example.TeamApp.excludedUI.EventButton
import com.example.TeamApp.excludedUI.getPlaceSuggestions
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
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    SearchStreetField()
                    ButtonsRow()
                    MyDateTimePicker()
                    EventButton(text = "Stwórz", onClick = { /*TODO*/ })
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
    var expanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            // Pole tekstowe
            TextField(
                value = query,
                onValueChange = { newText ->
                    query = newText
                    coroutineScope.launch {
                        suggestions = getPlaceSuggestions(newText)
                        expanded = suggestions.isNotEmpty()
                    }
                },
                placeholder = {
                    Text(
                        text = "Lokalizacja",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.robotoregular)),
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray,
                            textAlign = TextAlign.Start
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(56.dp),  // Ustawienie jednakowej wysokości
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black,
                    containerColor = Color.White
                ),
                textStyle = TextStyle(fontSize = 16.sp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                )
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                properties = PopupProperties(focusable = false)
            ) {
                suggestions.forEach { suggestion ->
                    DropdownMenuItem(
                        text = { Text(suggestion) },
                        onClick = {
                            query = suggestion
                            expanded = false
                            suggestions = emptyList()

                            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                                ?.hideSoftInputFromWindow((context as Activity).currentFocus?.windowToken, 0)

                            focusManager.clearFocus()
                        }
                    )
                }
            }
        }
    }
}



@Composable
fun SuggestionItem(suggestion: String, onSuggestionClick: (String) -> Unit) {
    Text(
        text = suggestion,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onSuggestionClick(suggestion)
            },
        style = TextStyle(
            fontSize = 14.sp,
            color = Color.Black
        )
    )
}



@SuppressLint("DefaultLocale")
@Composable
fun MyDateTimePicker() {
    var selectedDateTime by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    var selectedTime by remember { mutableStateOf("") }

    val timePickerDialog = TimePickerDialog(
        LocalContext.current,
        R.style.CustomDatePickerTheme,
        { _, pickedHour, pickedMinute ->
            selectedTime = String.format("%02d:%02d", pickedHour, pickedMinute)
            selectedDateTime += " $selectedTime"
        }, hour, minute, true
    )

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        R.style.CustomTimePickerTheme,
        { _, pickedYear, pickedMonth, pickedDayOfMonth ->
            selectedDateTime = "$pickedDayOfMonth/${pickedMonth + 1}/$pickedYear"
            timePickerDialog.show()
        }, year, month, day
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Przycisk z dynamicznie zmienianym tekstem
        Button(
            onClick = { datePickerDialog.show() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ), modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(56.dp)  // Taka sama wysokość jak w SearchStreetField
                .background(color = Color.White, shape = RoundedCornerShape(size = 16.dp))

        ) {
            Text(
                text = if (selectedDateTime.isEmpty()) "Data i godzina" else selectedDateTime,
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 25.sp,
                    fontFamily = FontFamily(Font(R.font.robotoregular)),
                    fontWeight = FontWeight(400),
                    color = Color.Gray,
                )
            )
        }
    }
}


@Composable
fun ButtonsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Sport Dropdown Button
        SportDropdownButton(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp) // Dystans między przyciskami
        )

        // Participants Dropdown Button
        ParticipantsDropdownButton(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp) // Dystans między przyciskami
        )
    }
}

@Composable
fun SportDropdownButton(modifier: Modifier = Modifier) {
    val viewModel: CreateEventViewModel = viewModel()
    val availableSports = viewModel.getAvailableSports()
    var expanded by remember { mutableStateOf(false) }
    val sport by viewModel.sport.observeAsState("")

    Box(
        modifier = modifier
            .background(
                Color.White,
                shape = RoundedCornerShape(16.dp)
            ) // Białe tło i zaokrąglone rogi
            .clickable { expanded = !expanded }
            .padding(16.dp)
            .heightIn(min = 28.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if(sport.isEmpty())"Aktynowść" else sport,
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.robotoregular)),
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            )

            Image(
                painter = painterResource(id = R.drawable.chevron_down),
                contentDescription = "Activity Icon",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(24.dp)
            )
        }
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.fillMaxWidth()
    ) {
        availableSports.forEach { sport ->
            DropdownMenuItem(
                text = { Text(sport) },
                onClick = {
                    viewModel.onSportChange(sport)
                    expanded = false
                }
            )
        }
    }
}

@Composable
fun ParticipantsDropdownButton(modifier: Modifier = Modifier) {
    var selectedPeople by remember { mutableStateOf<Int?>(null) }
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .background(
                Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { expanded = !expanded }
            .padding(16.dp)
            .heightIn(min = 28.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selectedPeople?.toString() ?: "liczba osób",
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.robotoregular)),
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            )

            Image(
                painter = painterResource(id = R.drawable.chevron_down),
                contentDescription = "Chevron Icon",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(24.dp)
            )
        }
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.fillMaxWidth()
    ) {
        (1..22).forEach { peopleCount ->
            DropdownMenuItem(
                onClick = {
                    selectedPeople = peopleCount
                    expanded = false
                },
                text = {
                    Text(
                        text = peopleCount.toString(),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Start
                    )
                }
            )
        }
    }
}



@Preview(showBackground = false)
@Composable
fun CreateEventv2Preview() {
    CreateEventv2()
}



