package com.example.TeamApp.searchThrough
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.ripple.rememberRipple
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
import androidx.navigation.NavController
import com.example.TeamApp.R
import com.example.TeamApp.event.CreateEventViewModel
import com.example.TeamApp.event.CreateEventViewModelProvider
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalConfiguration
import com.yourpackage.composables.OptionSelectorComponent
import kotlin.math.roundToInt


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FiltersScreen(navController: NavController) {
    val viewModel: SearchThroughViewModel = SearchViewModelProvider.searchThroughViewModel
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp

    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .background(color = Color(0xFFF2F2F2))) {
        val (topbar, filters, acceptButton) = createRefs()

        TopAppBar(
            modifier = Modifier.constrainAs(topbar)
            {
              top.linkTo(parent.top);
              start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.value(screenHeightDp * 0.11f)
            },
            title = {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Filtruj wydarzenia",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            fontWeight = FontWeight.W900,
                            color = Color(0xFF003366),
                            textAlign = TextAlign.End,
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            navigationIcon = {
                Box(
                    modifier = Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() }

                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrowleft),
                            contentDescription = "Back Icon",

                            //tint = Color(0xFF003366)
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        val compStart = createGuidelineFromStart(0.05f)
        val compEnd = createGuidelineFromStart(0.95f)

        ConstraintLayout(
            modifier = Modifier
                .constrainAs(filters) {
                    top.linkTo(topbar.bottom)
                    start.linkTo(compStart)
                    end.linkTo(compEnd)
                    bottom.linkTo(parent.bottom);
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                //.fillMaxSize()
                //.background(color = Color(0xFF000000))

        ) {
            val (sport, distanceSlider, sportPopupRef, priceOptionSelectorRef, reset ) = createRefs()
            val filtersTop = createGuidelineFromTop(0.03f)
            val distanceSliderHeight = Dimension.value(screenHeightDp * 0.10f)
            val distanceValue by viewModel.distance.observeAsState(75)
            val verticalSpacing = screenHeightDp * 0.025f

            val selectedPriceOptionFromVm by viewModel.selectedPriceOptionUi.collectAsState()
            OptionSelectorComponent(
                modifier = Modifier.constrainAs(priceOptionSelectorRef) {
                    top.linkTo(filtersTop) // Position after DateFilter
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent // Let FlowRow determine its height
                },
                label = "Cena",
                options = viewModel.priceFilterOptionsList,
                selectedOption = selectedPriceOptionFromVm,
                onOptionSelected = { option -> viewModel.onPriceOptionUiSelected(option) }
            )

            DistanceSlider(
                modifier = Modifier.constrainAs(distanceSlider) {
                    top.linkTo(priceOptionSelectorRef.bottom, margin = verticalSpacing)
                    height = distanceSliderHeight
                    width = Dimension.fillToConstraints
                },
                label = "Odległość", // Pass your desired label
                currentValue = distanceValue,
                range = 0..150, // Or your desired range
                onValueChange = { newDistance ->
                    viewModel.onDistanceChange(newDistance)
                },
                valueSuffix = " km" // Optional
            )
            val sportPopupHeight = Dimension.value(screenHeightDp * 0.1f)

            val selectedLevelOptionFromVm by viewModel.selectedLevelOptionUi.collectAsState()
            OptionSelectorComponent(
                modifier = Modifier.constrainAs(sportPopupRef) {
                    top.linkTo(distanceSlider.bottom)
                    //height = sportPopupHeight
                    width = Dimension.fillToConstraints
                },
                label = "Poziom",
                options = viewModel.levelFilterOptionsList,
                selectedOption = selectedLevelOptionFromVm,
                onOptionSelected = { option -> viewModel.onLevelOptionUiSelected(option) }
            )


            SportPopupButton(
                modifier = Modifier
                    .constrainAs(sport) {
                        top.linkTo(sportPopupRef.bottom, margin = verticalSpacing)
                        height = sportPopupHeight
                        width = Dimension.fillToConstraints
                    }
            )
            val resetHeight = Dimension.value(screenHeightDp * 0.05f)
            ClickableResetTextComponent(
                modifier = Modifier
                    .constrainAs(reset) {
                        top.linkTo(sport.bottom, margin = screenHeightDp * 0.01f)
                        height =  resetHeight
                    },
                navController = navController,
                viewModel
            )




        }

        val passStart = createGuidelineFromStart(0.1f)
        val passEnd = createGuidelineFromStart(0.9f)
        val passTop = createGuidelineFromTop(0.90f)

        val AcceptButtonHeight = Dimension.value(screenHeightDp * 0.08f)
        AcceptButton(
            text = "Filtruj",
            onClick = {
                viewModel.onFilterAccept()
                navController.popBackStack()
            },
            modifier = Modifier
                .constrainAs(acceptButton) {
                    top.linkTo(passTop)
                    start.linkTo(passStart)
                    end.linkTo(passEnd)
                    height = AcceptButtonHeight
                    width = Dimension.fillToConstraints
                }
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
fun ClickableResetTextComponent(modifier: Modifier = Modifier, navController: NavController, viewModel: SearchThroughViewModel) {
    val context = LocalContext.current
    val loginText = "Resetuj filtry"
    val annotatedString = buildAnnotatedString {
        pushStringAnnotation(tag = "Resetuj", annotation = loginText)
        withStyle(style = SpanStyle(color = Color(0xff4fc3f7), fontFamily =
        FontFamily(Font(R.font.proximanovabold)), fontWeight = FontWeight(900) )
        ) {
            append(loginText)
        }
        pop()
    }

    Box(
        modifier = modifier
            .fillMaxWidth(), // Make the box fill the available width

        Alignment.CenterStart
    ) {
        ClickableText(
            text = annotatedString,
            modifier = Modifier,
            style = TextStyle(textAlign = TextAlign.Center), // Center text horizontally
            onClick = {
                viewModel.onFilterReset()
            }
        )
    }
}

@Composable
fun SportPopupButton(modifier: Modifier = Modifier) {
    val otherviewModel: CreateEventViewModel = CreateEventViewModelProvider.createEventViewModel
    val viewModel: SearchThroughViewModel = SearchViewModelProvider.searchThroughViewModel
    val availableSports = viewModel.availableSports
    var showDialog by remember { mutableStateOf(false) }
    val hapticFeedback = LocalHapticFeedback.current

    val selectedSports by viewModel.selectedSports.observeAsState(availableSports)
    var isSelectAll by remember { mutableStateOf(true) }
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
                text ="Dyscypliny",
                modifier = Modifier
                    .weight(1f)
                    .wrapContentSize(Alignment.Center),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                    fontWeight = FontWeight.Medium,
                    color =  Color.Gray,
                    textAlign = TextAlign.Center
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween, // Space between elements
                        verticalAlignment = Alignment.CenterVertically // Align items vertically
                    ) {
                        Text(
                            text = "Wybierz dyscyplinę",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.proximanovabold)),
                                color = Color.Black
                            ),
                        )

                        IconButton(
                            onClick = { showDialog = false },
                            modifier = Modifier
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.close), // Replace with your icon
                                contentDescription = "Close"
                            )
                        }
                    }

                    // Lista przewijalna
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp) // Ograniczenie wysokości popupu
                    ) {
                        items(availableSports) { sport ->
                            val isSelected = selectedSports.contains(sport)
                            Log.d("SportPopupButton", "Sport: $sport, isSelected: $isSelected")
                            Text(
                                text = sport,
                                color = if (isSelected) Color(0xff4fc3f7) else Color.Black,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .fillMaxWidth()
                                    .clickable {
                                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.updateSportSelection(sport)
                                    }
                                    .padding(12.dp),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.proximanovalight)),
                                )
                            )
                        }
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Zaznacz wszyst.",
                            fontSize = 19.sp, // Zmniejsz rozmiar tekstu, aby dopasować
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple()
                                ) {
                                    isSelectAll = !isSelectAll
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    Log.d("SportPopupButton", "Select all sports")
                                    Log.d("SportPopupButton", "isSelectAll: $isSelectAll")
                                    viewModel.toggleAllSports(isSelectAll)
                                }
                                .padding(8.dp)
                        )
                        Text(
                            color = Color.Black,
                            text ="|",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Gotowe",
                            fontSize = 19.sp, // Zmniejsz rozmiar tekstu, aby dopasować
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple()
                                ) {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    showDialog = false
                                }
                                .padding(8.dp)
                        )
                    }


                }
            }
        }
    }
}

@Composable
fun GenderButtonWithLabel(label: String, viewModel: SearchThroughViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = { viewModel.onSexChange(label)},
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
    label: String, // Text for the label (e.g., "Odległość" or "Age range")
    currentValue: Int, // The current value of the slider, typically from a ViewModel
    range: IntRange = 0..150,
    onValueChange: (Int) -> Unit, // Callback when the slider value changes
    valueSuffix: String = "" // Optional suffix like "km" or "lat"
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Label Text (e.g., "Odległość" or "Age range")
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp, // Adjusted for prominence like "Gender" or "Age range"
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
        )

        // Row for Slider and its current value display
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Pushes slider and value text apart
        ) {
            // Slider
            Slider(
                value = currentValue.toFloat(),
                onValueChange = { newValue ->
                    onValueChange(newValue.toInt()) // Notify value change with Int
                },
                valueRange = range.first.toFloat()..range.last.toFloat(),
                modifier = Modifier
                    .weight(1f) // Allow the slider to fill available space
                    .padding(end = 16.dp), // Space between slider and value text
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xff4fc3f7), // Blue thumb
                    activeTrackColor = Color(0xff4fc3f7), // Blue active track
                    inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant // Default inactive track
                )
            )

            // Distance value text, aligned to the right
            Text(
                text = "$currentValue$valueSuffix",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal, // Or FontWeight.Bold if preferred
                color = Color.DarkGray,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .widthIn(min = 40.dp) // Give some minimum space for the number
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeSliderExample(modifier: Modifier = Modifier, viewModel: SearchThroughViewModel) {
    val minAge by viewModel.minAge.observeAsState(0)
    val maxAge by viewModel.maxAge.observeAsState(100)

    val sliderPosition = minAge.toFloat()..maxAge.toFloat()

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
            onValueChange = { val newMin = it.start.toInt()
                val newMax = it.endInclusive.toInt()
                viewModel.onMinAgeChange(it.start.toInt())
                viewModel.onMaxAgeChange(it.endInclusive.toInt())
                            },
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



