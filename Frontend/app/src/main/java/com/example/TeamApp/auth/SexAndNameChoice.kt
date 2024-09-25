package com.example.TeamApp.auth

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.TeamApp.R
import com.example.ui.theme.buttonLogIn

@Composable
fun SexAndNameChoice (navController: NavController) {
    val nameFocusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    val view = LocalView.current
    val viewModel: LoginViewModel = LoginViewModelProvider.loginViewModel
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarSuccess by remember { mutableStateOf(false) }
    var isMale by remember { mutableStateOf(false) }
    var isFemale by remember { mutableStateOf(false) }

    val gradientColors= listOf(
        Color(0xFFE8E8E8)
        ,Color(0xFF007BFF)
    )
    LaunchedEffect(Unit){
        viewModel.loadAvatars(25)

    }

    Surface( modifier = Modifier.fillMaxSize() ) {
        //we are drawing on top of the box since guidelines do not work with that for some reason?
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.linearGradient(colors = gradientColors))
        ) {}

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (upperText, nameBox, maleBox, femaleBox, birthdayBox,
                nextButton, femaleIcon, maleIcon) = createRefs()
            val upperTextTop = createGuidelineFromTop(0.1f)
            val upperTextStart = createGuidelineFromStart(0.1f)
            val upperTextEnd = createGuidelineFromEnd(0.9f)
            val upperTextBottom = createGuidelineFromTop(0.13f)
            UpperTextField(
                value = "Uzupełnij profil",
                modifier = Modifier
                    .constrainAs(upperText) {
                        top.linkTo(upperTextTop)
                        start.linkTo(upperTextStart)
                        end.linkTo(upperTextEnd)
                        bottom.linkTo(upperTextBottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                        centerHorizontallyTo(parent)
                    }
            )

            val nameStart = createGuidelineFromStart(0.1f)
            val nameEnd = createGuidelineFromStart(0.9f)
            val nameTop = createGuidelineFromTop(0.2f)
            val nameBottom = createGuidelineFromTop(0.265f)
            NameBox (labelValue = "Imię",
                painterResource (id = R.drawable.user_icon),
                nextFocusRequester = null,
                focusRequester = nameFocusRequester,
                modifier = Modifier
                    .constrainAs(nameBox) {
                        top.linkTo(nameTop)
                        bottom.linkTo(nameBottom)
                        start.linkTo(nameStart)
                        end.linkTo(nameEnd)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
            )

            val birthdayStart = createGuidelineFromStart(0.1f)
            val birthdayEnd = createGuidelineFromStart(0.9f)
            val birthdayTop = createGuidelineFromTop(0.3f)
            val birthdayBottom = createGuidelineFromTop(0.365f)
            NameBox (labelValue = "Data urodzenia",
                painterResource (id = R.drawable.user_icon),
                nextFocusRequester = null,
                focusRequester = nameFocusRequester,
                modifier = Modifier
                    .constrainAs(birthdayBox) {
                        top.linkTo(birthdayTop)
                        bottom.linkTo(birthdayBottom)
                        start.linkTo(birthdayStart)
                        end.linkTo(birthdayEnd)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
            )

            val maleStart = createGuidelineFromStart(0.15f)
            val maleEnd = createGuidelineFromStart(0.45f)
            val maleTop = createGuidelineFromTop(0.4f)
            val maleBottom = createGuidelineFromTop(0.465f)
            Box ( modifier = Modifier
                .constrainAs(maleBox) {
                    top.linkTo(maleTop)
                    bottom.linkTo(maleBottom)
                    start.linkTo(maleStart)
                    end.linkTo(maleEnd)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
                .clip( shape = RoundedCornerShape(23.dp))
                .background(if (isMale) Color.Cyan else Color.White)
                .clickable()
                {
                    isMale = !isMale
                    isFemale = false
                }
            )
            {
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    Image (
                        painter = painterResource(R.drawable.user_icon),
                        contentDescription = "male",
                        modifier = Modifier
                            .constrainAs(femaleIcon) {
                                centerVerticallyTo(parent)
                                centerHorizontallyTo(parent)
                            }
                    )
                }
            }

                val femaleStart = createGuidelineFromStart(0.55f)
            val femaleEnd = createGuidelineFromStart(0.85f)
            val femaleTop = createGuidelineFromTop(0.4f)
            val femaleBottom = createGuidelineFromTop(0.465f)
            Box ( modifier = Modifier
                .constrainAs(femaleBox) {
                    top.linkTo(femaleTop)
                    bottom.linkTo(femaleBottom)
                    start.linkTo(femaleStart)
                    end.linkTo(femaleEnd)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
                .clip( shape = RoundedCornerShape(23.dp))
                .background(if (isFemale) Color.Cyan else Color.White)
                .clickable()
                {
                    isFemale = !isFemale
                    isMale = false
                }
            )
            {
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    Image (
                        painter = painterResource(R.drawable.user_icon),
                        contentDescription = "female",
                        modifier = Modifier
                            .constrainAs(femaleIcon) {
                                centerVerticallyTo(parent)
                                centerHorizontallyTo(parent)
                            }
                    )
                }
            }

            val nextStart = createGuidelineFromStart(0.1f)
            val nextEnd = createGuidelineFromStart(0.9f)
            val nextTop = createGuidelineFromTop(0.5f)
            val nextBottom = createGuidelineFromTop(0.565f)
            ButtonNext( modifier = Modifier
                .background(color = Color(0xFF007BFF), shape = RoundedCornerShape(23.dp))
                .constrainAs(nextButton) {
                    top.linkTo(nextTop)
                    bottom.linkTo(nextBottom)
                    start.linkTo(nextStart)
                    end.linkTo(nextEnd)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
                ,navController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameBox(labelValue: String, painterResource: Painter,
                    nextFocusRequester: FocusRequester?,
                    focusRequester: FocusRequester,
                    modifier: Modifier
) {

    val textValue = remember { mutableStateOf("") }
    val viewModel: LoginViewModel = viewModel()
    val email by viewModel.email.observeAsState("")
    val focusManager = LocalFocusManager.current

    TextField(
        modifier = modifier,
        label = {
            Text(
                text = labelValue,
            )
        },
        value = if (labelValue == "E-mail") email else textValue.value,
        onValueChange = { newText ->

            if (labelValue == "E-mail") {
                val allowedCharsRegex =
                    Regex("^[0-9a-zA-Z!@#\$%^&*()_+=\\-{}\\[\\]:\";'<>?,./]*\$")
                if (allowedCharsRegex.matches(newText)) {
                    viewModel.onEmailChange(newText)

                }
            } else {
                val allowedCharsRegex =
                    Regex("^[0-9\\sa-zA-Z!@#\$%^&*()_+=\\-{}\\[\\]:\";'<>?,./]*\$")
                if (allowedCharsRegex.matches(newText)) {
                    textValue.value = newText
                }
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = if (nextFocusRequester != null) ImeAction.Next else ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                nextFocusRequester?.requestFocus() ?: focusManager.clearFocus()
            },
            onDone = {
                focusManager.clearFocus()
            }
        ),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = Color.White

        ),
        leadingIcon = {
            Icon(
                painter = painterResource, contentDescription = "", modifier = Modifier
                    .padding(1.dp)
                    .width(22.dp)
                    .height(22.dp)
            )
        },
        shape = MaterialTheme.shapes.medium.copy(all = CornerSize(23.dp))
    )
}

@Composable
fun ButtonNext(modifier: Modifier, navController: NavController) {
    val viewModel: LoginViewModel = LoginViewModelProvider.loginViewModel
    val isLoading by viewModel.isLoading.observeAsState(false)

    Button(
        onClick = {
            Log.d("SexAndNameChoice", "ButtonNext: onClick")
            navController.navigate("avatarSelection") {
                popUpTo("sexName") {
                    inclusive = false
                }
            }
        }, // TODO
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .weight(1f) // Take up remaining space on the left
                        .wrapContentWidth(align = Alignment.CenterHorizontally)


                ) {
                    Text(
                        text = "Dalej",
                        style = buttonLogIn
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.arrow_right_icon),
                    contentDescription = "Send",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}