package com.example.TeamApp.searchThrough
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.TeamApp.R
import com.example.TeamApp.auth.LoginViewModel
import com.example.TeamApp.event.CreateEventViewModel
import com.example.TeamApp.event.ViewModelProvider
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FiltersScreen(navController: NavController) {
    val viewModel: SearchThroughViewModel = viewModel()
    var showEmptyMessage by remember { mutableStateOf(false) }
    //val context = LocalContext.current

    ConstraintLayout(modifier = Modifier.fillMaxSize()
        .background(color = Color(0xFFF2F2F2))) {
        val (acceptButton, reset, sport, genderButtons, ageSlider, distanceSlider ) = createRefs()

        TopAppBar(
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.CenterEnd // Align to the end

                ) {
                    Text(
                        text = "Filtruj wydarzenia",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            fontWeight = FontWeight(900),
                            color = Color(0xFF003366),
                            textAlign = TextAlign.End,
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            navigationIcon = {
                IconButton(modifier = Modifier.padding(8.dp), onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrowleft),
                        contentDescription = "Back Icon"
                    )

                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.White)
        )


        //mozna tworzyc guideline nie od poczatku ekranu tylko od innych obiektow
        //np top.linkTo(reset.bottom, margin = 16.dp)
        val sportStart = createGuidelineFromStart(0.1f)
        val sportEnd = createGuidelineFromStart(0.9f)
        val sportTop = createGuidelineFromTop(0.20f)
        val sportBottom = createGuidelineFromTop(0.28f)

        SportPopupButton(
            modifier = Modifier
                .constrainAs(sport) {
                    top.linkTo(sportTop)
                    bottom.linkTo(sportBottom)
                    start.linkTo(sportStart)
                    end.linkTo(sportEnd)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
        )

        val ageSliderTop = createGuidelineFromTop(0.45f)
        val ageSliderBottom = createGuidelineFromTop(0.50f)

        RangeSliderExample(
            modifier = Modifier.constrainAs(ageSlider) {
                top.linkTo(ageSliderTop)
                bottom.linkTo(ageSliderBottom)
                start.linkTo(sportStart)
                end.linkTo(sportEnd)
                width = Dimension.fillToConstraints
            },
        )

        val distanceSliderTop = createGuidelineFromTop(0.56f)
        val distanceSliderBottom = createGuidelineFromTop(0.66f)

        DistanceSlider(
            modifier = Modifier.constrainAs(distanceSlider) {
                top.linkTo(distanceSliderTop)
                bottom.linkTo(distanceSliderBottom)
                start.linkTo(sportStart)
                end.linkTo(sportEnd)
                width = Dimension.fillToConstraints
            },
            onValueChange = { distance ->
                // Handle distance changes
            }
        )


        val passStart = createGuidelineFromStart(0.1f)
        val passEnd = createGuidelineFromStart(0.9f)
        val passTop = createGuidelineFromTop(0.86f)
        val passBottom = createGuidelineFromTop(0.94f)

        val genderButtonsTop = createGuidelineFromTop(0.32f)
        val genderButtonsBottom = createGuidelineFromTop(0.42f)
        Row(
            modifier = Modifier
                .constrainAs(genderButtons) {
                    top.linkTo(genderButtonsTop)
                    bottom.linkTo(genderButtonsBottom)
                    start.linkTo(sportStart)
                    end.linkTo(sportEnd)
                }
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GenderButtonWithLabel("mężczyźni")
            GenderButtonWithLabel("mieszane")
            GenderButtonWithLabel("kobiety")
        }

        AcceptButton(
            text = "Filtruj",
            onClick = {

            },
            modifier = Modifier
                .constrainAs(acceptButton) {
                    top.linkTo(passTop)
                    bottom.linkTo(passBottom)
                    start.linkTo(passStart)
                    end.linkTo(passEnd)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
        )

        val resetStart = createGuidelineFromStart(0.35f)
        val resetEnd = createGuidelineFromStart(0.65f)
        val resetTop = createGuidelineFromTop(0.94f)
        val resetBottom = createGuidelineFromTop(0.99f)

        ClickableResetTextComponent(
            modifier = Modifier
                .constrainAs(reset) {
                    top.linkTo(resetTop)
                    bottom.linkTo(resetBottom)
                    start.linkTo(resetStart)
                    end.linkTo(resetEnd)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints

                },
            navController = navController
        )
    }
}
@Composable
fun AcceptButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hapticFeedback = LocalHapticFeedback.current
    Button(
        onClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            onClick()
        },
        modifier = modifier,

        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xff4fc3f7), // Button background color
            contentColor = Color.Black // Button text color
        )
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.proximanovabold)),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF003366), // Button text color
                textAlign = TextAlign.Center,
                letterSpacing = 1.sp
            )
        )
    }
}
@Composable
fun ClickableResetTextComponent(modifier: Modifier = Modifier, navController: NavController) {
    val viewModel: LoginViewModel = viewModel()
    val context = LocalContext.current
    val loginText = "Resetuj filtry"
    val annotatedString = buildAnnotatedString {
        pushStringAnnotation(tag = "Resetuj", annotation = loginText)
        withStyle(style = SpanStyle(color = Color(0xffd46161), fontFamily =
        FontFamily(Font(R.font.proximanovabold)), fontWeight = FontWeight(900) )
        ) {
            append(loginText)
        }
        pop()
    }

    Box(
        modifier = modifier
            .fillMaxWidth() // Make the box fill the available width
            .padding(8.dp), // Optional padding for aesthetics
        contentAlignment = Alignment.Center // Center the content (text) within the box
    ) {
        ClickableText(
            text = annotatedString,
            modifier = Modifier,
            style = TextStyle(textAlign = TextAlign.Center), // Center text horizontally
            onClick = {

            }
        )
    }
}

@Composable
fun SportPopupButton(modifier: Modifier = Modifier) {
    val viewModel: CreateEventViewModel = ViewModelProvider.createEventViewModel
    val availableSports = viewModel.getAvailableSports()
    var showDialog by remember { mutableStateOf(false) }
    val selectedSport by viewModel.sport.observeAsState("")
    val hapticFeedback = LocalHapticFeedback.current
    // Przycisk, który otwiera popup
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                showDialog = true
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            } // Kliknięcie otwiera popup
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
                    fontFamily = if (selectedSport.isEmpty()) FontFamily(Font(R.font.proximanovaregular)) else FontFamily(Font(R.font.proximanovabold)),
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
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
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
                                    .clip(RoundedCornerShape(8.dp))
                                    .fillMaxWidth()
                                    .clickable {

                                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                        showDialog = false // Zamknięcie popupu po wybraniu opcji
                                    }

                                    .padding(12.dp),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.proximanovalight)),
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
fun GenderButtonWithLabel(label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = { /* Handle button click */ },
            modifier = Modifier
                .size(50.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp)) // Placeholder border for icon
        ) {
            Icon(
                painter = painterResource(id = R.drawable.usersicon),
                contentDescription = label,
                tint = Color.Gray
            )
        }
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun DistanceSlider(
    modifier: Modifier = Modifier,
    range: IntRange = 0..150,
    initialValue: Float = 75f,
    onValueChange: (Float) -> Unit
) {
    var sliderValue by remember { mutableStateOf(initialValue) }

    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "odległość", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.align(Alignment.Start))

        Text(
            text = sliderValue.toInt().toString(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )


        Slider(
            value = sliderValue,
            onValueChange = { newValue ->
                sliderValue = newValue
                onValueChange(sliderValue)
            },
            valueRange = range.first.toFloat()..range.last.toFloat(),
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = Color(0xff4fc3f7),
                activeTrackColor = Color(0xff4fc3f7)
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeSliderExample(modifier: Modifier = Modifier) {
    var sliderPosition by remember { mutableStateOf(0f..100f) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Wiek: ${sliderPosition.start.toInt()} - ${sliderPosition.endInclusive.toInt()}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        RangeSlider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = 0f..100f,
            onValueChangeFinished = {

            },
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = Color(0xff4fc3f7),
                activeTrackColor = Color(0xff4fc3f7)
            )
        )
    }
}
