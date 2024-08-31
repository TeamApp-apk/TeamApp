package com.example.TeamApp.event

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.PopupProperties
//import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.TeamApp.R
import com.example.TeamApp.data.Event
import com.example.TeamApp.excludedUI.EventButton
import com.example.TeamApp.excludedUI.Picker
import com.example.TeamApp.excludedUI.PickerExample
import com.example.TeamApp.excludedUI.getPlaceSuggestions
import java.util.Calendar
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CreateEventScreen(navController: NavController) {
    val viewModel: CreateEventViewModel = ViewModelProvider.createEventViewModel
    val sport by viewModel.sport.observeAsState("")
    val address by viewModel.location.observeAsState("")
    val limit by viewModel.limit.observeAsState("")
    val description by viewModel.description.observeAsState("")
    val availableSports = viewModel.getAvailableSports()
    val allowedCharsRegex = Regex("^[0-9\\sa-zA-Z!@#\$%^&*()_+=\\-{}\\[\\]:\";'<>?,./]*\$")
    val location by viewModel.location.observeAsState("")
    val dateTime by viewModel.dateTime.observeAsState("")

    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarSuccess by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    val gradientColors = listOf(
        Color(0xFFE8E8E8),
        Color(0xFF007BFF)
    )
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        viewModel.fetchEvents()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = gradientColors))
            .padding(horizontal = 20.dp, vertical = 60.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Stwórz wydarzenie",
                style = TextStyle(
                    fontSize = 26.sp,
                    fontFamily = FontFamily(Font(R.font.robotobold)),
                    fontWeight = FontWeight(900),
                    color = Color(0xFF003366),
                    textAlign = TextAlign.Center,
                    lineHeight = 25.sp,
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(color = Color(0xFFF2F2F2), shape = RoundedCornerShape(size = 30.dp))
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .background(
                            color = Color(0xFFF2F2F2),
                            shape = RoundedCornerShape(size = 16.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DescriptionInputField(
                        description = description,
                        onEditClick = { showDialog = true }
                    )

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SearchStreetField()
                        ButtonsColumn()
                        MyDateTimePickerv2()
                        Spacer(modifier = Modifier.height(25.dp))
                        EventButton(text = "Stwórz", onClick = {

                            Log.d("CreateEventScreen", "Submit button clicked")
                            val participantLimit = limit.toIntOrNull()
                            Log.e("CreateEventScreen", "Participant limit: $participantLimit")
                            Log.e("CreateEventScreen", "Sport: $sport")
                            Log.e("CreateEventScreen", "Address: $address")
                            Log.e("CreateEventScreen", "Date: $dateTime")
                            Log.e("CreateEventScreen", "Description: $description")
                            if (sport.isNotEmpty() && address.isNotEmpty()  && location.isNotEmpty()) {
                                Log.d("CreateEventScreen", "Valid input")
                                val newEvent = Event(
                                    iconResId = Event.sportIcons[sport] ?: "",
                                    date = dateTime,
                                    activityName = sport,
                                    currentParticipants = 0,
                                    maxParticipants = limit.toInt(),
                                    location = location
                                )
                                viewModel.createEvent(newEvent){ result ->
                                    if (result == null) {
                                        snackbarMessage = "Utworzono Event"
                                        snackbarSuccess = true
                                        viewModel.resetFields()
                                    } else {
                                        snackbarMessage = result
                                        snackbarSuccess = false
                                    }
                                    showSnackbar = true
                                }

                            } else {
                                snackbarMessage = "Uzupełnij wszystkie pola"
                                snackbarSuccess = false
                                showSnackbar = true
                            }

                        })
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

        }
    }

    if (showDialog) {
        DescriptionDialog(
            initialText = description,
            onDismiss = { showDialog = false },
            onSave = { newText ->
                viewModel.onDescriptionChange(newText)
                showDialog = false
            }
        )
    }
    if (showSnackbar) {
        com.example.TeamApp.excludedUI.CustomSnackbar(
            success = snackbarSuccess,
            type = snackbarMessage,
            onDismiss = {
                showSnackbar = false
            }
        )
    }
}

@Composable
fun DescriptionInputField(
    description: String,
    onEditClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 27.dp, vertical = 8.dp)
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onEditClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (description.isEmpty()) "Opis" else description,
            textAlign = TextAlign.Center,
            style = TextStyle(
                color = if (description.isEmpty()) Color.Gray else Color.Black,
                fontSize = 16.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionDialog(
    initialText: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    val allowedCharsRegex = Regex("^[0-9\\sa-zA-Z!@#\$%^&*()_+=\\-{}\\[\\]:\";'<>?,./]*\$")
    var text by remember { mutableStateOf(initialText) }
    val viewModel: CreateEventViewModel = ViewModelProvider.createEventViewModel

    // Create a FocusRequester to request focus on the TextField
    val focusRequester = remember { FocusRequester() }

    // Get the keyboard controller
    val keyboardController = LocalSoftwareKeyboardController.current

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Opisz pokrótce szczegóły wydarzenia...",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(8.dp)
                        ) // Ensure the background is white
                        .padding(horizontal = 8.dp) // Add some padding inside the TextField
                ) {
                    TextField(
                        value = text,
                        onValueChange = { newText -> text = newText },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester), // Attach FocusRequester to the TextField
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent, // No underline when focused
                            unfocusedIndicatorColor = Color.Transparent // No underline when unfocused
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = onDismiss) {
                        Text("Anuluj")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        viewModel.onDescriptionChange(text)
                        onSave(text)
                    }) {
                        Text("Zapisz")
                    }
                }
            }
        }
    }
    // Automatically focus the TextField and show the keyboard when the dialog is opened
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
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
    val viewModel:CreateEventViewModel = ViewModelProvider.createEventViewModel
    val location by viewModel.location.observeAsState("")
    query = location

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            // Pole tekstowe
            TextField(
                value = query,
                onValueChange = { newText ->
                    // Aktualizacja query
                    query = newText

                    // Wywołanie onAddressChange z nowym tekstem
                    viewModel.onAddressChange(newText)
                    // Uruchomienie coroutineScope dla uzyskania sugestii
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
                            fontSize = 16.sp,
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
                            viewModel.onAddressChange(suggestion)
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
            fontSize = 16.sp,
            color = Color.Black
        )
    )
}


@SuppressLint("DefaultLocale")
@Composable
fun MyDateTimePickerv2() {
    val viewModel: CreateEventViewModel = ViewModelProvider.createEventViewModel
    var selectedDateTime by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("")}
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val date by viewModel.dateTime.observeAsState("")
    var showDialog by remember { mutableStateOf(false) }
    val originalFormat = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale("pl", "PL"))

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        R.style.CustomTimePickerTheme,
        { _, pickedYear, pickedMonth, pickedDayOfMonth ->
            calendar.set(pickedYear, pickedMonth, pickedDayOfMonth)
            selectedDate = "$pickedDayOfMonth/${pickedMonth + 1}/$pickedYear"
        },
        year, month, day
    ).apply {
        datePicker.minDate = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, 70)
        datePicker.maxDate = calendar.timeInMillis
    }

        Button(
            onClick = { showDialog = true},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(56.dp)
                .background(color = Color.White, shape = RoundedCornerShape(size = 16.dp))
        )  {
            Text(
                text = if (date.isEmpty()) "Data i godzina" else date,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 25.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = if(date.isEmpty()) FontFamily(Font(R.font.robotoregular)) else FontFamily(Font(R.font.robotobold)),
                    fontWeight = if(date.isEmpty()) FontWeight.Medium else FontWeight.Bold,
                    color = if(date.isEmpty()) Color.Gray else Color(0xFF003366),
                ),
                    maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

    if (showDialog)
    {
        Dialog(onDismissRequest = { showDialog = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(16.dp))

            ) {
                Column {
                    Text(
                        text = "Wybierz Datę",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(8.dp)

                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(
                                Color.White,
                                shape = RoundedCornerShape(8.dp)
                            ) // Ensure the background is white
                            .padding(horizontal = 8.dp) // Add some padding inside the TextField
                    ) {
                        PickerExample(
                            selectedDate = selectedDate,
                            onDateTimeSelected = { selectedDate ->
                                val localDateTime = LocalDateTime.parse(selectedDate, originalFormat)

                                // Get the current date and time
                                val currentDateTime = LocalDateTime.now()

                                val finalDateTime = if (localDateTime.isBefore(currentDateTime)) {
                                    currentDateTime // Use the current time if selectedDate is in the past
                                } else {
                                    localDateTime // Use the selected date if it is valid
                                }
                                val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale("pl"))
                                selectedDateTime = finalDateTime.format(formatter)
                            }
                        )
                    }

                    //Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Button(onClick = { datePickerDialog.show()},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xff4fc3f7), // Kolor tła przycisku
                                contentColor = Color.Black // Kolor tekstu przycisku
                            )) {
                            Text("Kalendarz")
                        }
                        Button(
                            onClick = { showDialog = false },
                                colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xff4fc3f7), // Kolor tła przycisku
                            contentColor = Color.Black // Kolor tekstu przycisku
                        )
                        ) {
                            Text("Anuluj")

                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            viewModel.onDateChange(selectedDateTime)
                            showDialog = false
                        },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xff4fc3f7), // Kolor tła przycisku
                                contentColor = Color.Black // Kolor tekstu przycisku
                        )) {
                            Text("Zapisz")
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun MyDateTimePicker(onDateChange: (String) -> Unit) {
    val viewModel: CreateEventViewModel = ViewModelProvider.createEventViewModel
    var selectedDateTime by remember { mutableStateOf("") }
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val date by viewModel.dateTime.observeAsState("")


    val timePickerDialog = TimePickerDialog(
        LocalContext.current,
        R.style.CustomDatePickerTheme,
        { _, pickedHour, pickedMinute ->
            val pickedDateTime = LocalDateTime.of(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                pickedHour,
                pickedMinute
            )
            val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale("pl"))
            selectedDateTime = pickedDateTime.format(formatter)
            onDateChange(selectedDateTime) // Pass formatted string to onDateChange
        },
        hour, minute, true
    )

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        R.style.CustomTimePickerTheme,
        { _, pickedYear, pickedMonth, pickedDayOfMonth ->
            calendar.set(pickedYear, pickedMonth, pickedDayOfMonth)
            selectedDateTime = "$pickedDayOfMonth/${pickedMonth + 1}/$pickedYear"
            timePickerDialog.show()
        },
        year, month, day
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { datePickerDialog.show() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .padding(8.dp)
                .fillMaxWidth()
                .height(56.dp)
                .background(color = Color.White, shape = RoundedCornerShape(size = 16.dp))
        ) {
            Text(
                text = if (date.isEmpty()) "Data i godzina" else date,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 25.sp,
                    fontFamily = if(date.isEmpty()) FontFamily(Font(R.font.robotoregular)) else FontFamily(Font(R.font.robotobold)),
                    fontWeight = if(date.isEmpty()) FontWeight.Medium else FontWeight.Bold,
                    color = if(date.isEmpty()) Color.Gray else Color(0xFF003366),
                )
            )
        }
    }
}



@Composable
fun ButtonsColumn() {  // Zmieniłem nazwę na ButtonsColumn, bo teraz to będzie kolumna
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Sport Dropdown Button
        SportPopupButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp) // Dystans między przyciskami
        )

        // Participants Dropdown Button
        ParticipantsPopupButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp) // Dystans między przyciskami
        )
    }
}


@Composable
fun SportPopupButton(modifier: Modifier = Modifier) {
    val viewModel: CreateEventViewModel = ViewModelProvider.createEventViewModel
    val availableSports = viewModel.getAvailableSports()
    var showDialog by remember { mutableStateOf(false) }
    val selectedSport by viewModel.sport.observeAsState("")
    // Przycisk, który otwiera popup
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { showDialog = true } // Kliknięcie otwiera popup
            .padding(16.dp)
            .heightIn(min = 28.dp),
        contentAlignment = Alignment.Center

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (selectedSport.isEmpty()) "Dyscyplina" else selectedSport,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentSize(Alignment.Center),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = if (selectedSport.isEmpty()) FontFamily(Font(R.font.robotoregular)) else FontFamily(Font(R.font.robotobold)),
                    fontWeight = if(selectedSport.isEmpty()) FontWeight.Medium else FontWeight.Bold,
                    color = if (selectedSport.isEmpty()) Color.Gray else Color(0xFF003366),
                    textAlign = TextAlign.Center
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    // Popup dialog z przewijalną listą
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Wybierz dyscyplinę",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    // Lista przewijalna
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp) // Ograniczenie wysokości popupu
                    ) {
                        items(availableSports) { sport ->
                            Text(
                                text = sport,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.onSportChange(sport)
                                        showDialog = false // Zamknięcie popupu po wybraniu opcji
                                    }
                                    .padding(12.dp),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ParticipantsPopupButton(modifier: Modifier = Modifier) {
    var selectedPeople by remember { mutableStateOf<Int?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    val viewModel: CreateEventViewModel = ViewModelProvider.createEventViewModel
    val limit by viewModel.limit.observeAsState("")

    // Przycisk, który otwiera popup
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { showDialog = true } // Kliknięcie otwiera popup
            .padding(16.dp)
            .heightIn(min = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (limit.isEmpty()) "Liczba uczestników" else limit,
                modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = if(limit.isEmpty()) FontFamily(Font(R.font.robotoregular)) else FontFamily(Font(R.font.robotobold)),
                    fontWeight = if(limit.isEmpty()) FontWeight.Medium else FontWeight.Bold,
                    color = if(limit.isEmpty()) Color.Gray else Color(0xFF003366),
                    textAlign = TextAlign.Center
                )
            )
        }
    }

    // Popup dialog z przewijalną listą uczestników
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Wybierz liczbę uczestników",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    // Lista przewijalna
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp) // Ograniczenie wysokości popupu
                    ) {
                        items((1..22).toList()) { peopleCount ->
                            Text(
                                text = peopleCount.toString(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedPeople = peopleCount
                                        viewModel.onLimitChange(peopleCount.toString())
                                        showDialog = false // Zamknięcie popupu po wyborze liczby
                                    }
                                    .padding(12.dp),
                                fontSize = 18.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                }
            }
        }
    }
}



//@Preview(showBackground = false)
//@Composable
//fun CreateEventv2Preview() {
//    CreateEventv2()
//}



