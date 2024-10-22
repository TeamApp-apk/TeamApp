package com.example.TeamApp.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutBaseScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.TeamApp.R
import com.example.compose.primaryLight
import com.example.compose.secondaryLight
import com.example.ui.theme.fontFamily
import com.example.TeamApp.excludedUI.CustomSnackbar
import com.example.ui.theme.*
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension


@Composable
fun ForgotPasswordScreen(navController: NavController){
    val viewModel: LoginViewModel = viewModel()
    val loginSuccess by viewModel.loginSuccess.observeAsState()
    val registerSuccess by viewModel.registerSuccess.observeAsState()
    val emailSent by viewModel.emailSent.observeAsState()
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarSuccess by remember { mutableStateOf(false) }


    val gradientColors= listOf(
        Color(0xFFE8E8E8)
        ,Color(0xFF007BFF)
    )

    Surface(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.linearGradient(colors = gradientColors))
        ) {}

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (upperText, lowerText, email, sendBox) = createRefs()
            val upperTextTop = createGuidelineFromTop(0.1f)
            val upperTextStart = createGuidelineFromStart(0.1f)
            val upperTextEnd = createGuidelineFromEnd(0.9f)
            val upperTextBottom = createGuidelineFromTop(0.13f)
            UpperTextField(
                value = "Zresetuj hasło",
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

            val lowerTextTop = createGuidelineFromTop(0.2f)
            val lowerTextStart = createGuidelineFromStart(0.1f)
            val lowerTextEnd = createGuidelineFromStart(0.9f)
            val lowerTextBottom = createGuidelineFromTop(0.2f)
            LowerTextField(
                value = "Wpisz tu swój e-mail\n" +
                        " do którego chcesz otrzymać link do resetu hasła",
                modifier = Modifier
                    .constrainAs(lowerText) {
                        top.linkTo(lowerTextTop)
                        start.linkTo(lowerTextStart)
                        end.linkTo(lowerTextEnd)
                        bottom.linkTo(lowerTextBottom)
                        width = Dimension.fillToConstraints
                        centerHorizontallyTo(parent)
                    }
            )

            val emailStart = createGuidelineFromStart(0.1f)
            val emailEnd = createGuidelineFromStart(0.9f)
            val emailTop = createGuidelineFromTop(0.25f)
            val emailBottom = createGuidelineFromTop(0.315f)

            EmailButtonField(labelValue ="Your Email" ,
                painterResource (id=R.drawable.mail_icon ),
                modifier = Modifier
                    .constrainAs(email) {
                        top.linkTo(emailTop)
                        bottom.linkTo(emailBottom)
                        start.linkTo(emailStart)
                        end.linkTo(emailEnd)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
            )

            val sendBoxStart = createGuidelineFromStart(0.1f)
            val sendBoxEnd = createGuidelineFromStart(0.9f)
            val sendBoxTop = createGuidelineFromTop(0.35f)
            val sendBoxBottom = createGuidelineFromTop(0.44f)
            ButtonWithSend(
                onSnackbarMessageChanged = { message -> snackbarMessage = message ?: "" },
                onShowSnackbar = { showSnackbar = it },
                onSnackbarSuccess = { success -> snackbarSuccess = success},
                modifier = Modifier
                    .constrainAs(sendBox) {
                        top.linkTo(sendBoxTop)
                        bottom.linkTo(sendBoxBottom)
                        start.linkTo(sendBoxStart)
                        end.linkTo(sendBoxEnd)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
            )
        }
    }

    /*

                EmailButtonField(labelValue ="Your Email" , painterResource (id=R.drawable.mail_icon ))
                Spacer(modifier = Modifier.height(54.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center) // Center horizontally
                ) {
                    ButtonWithSend(
                        onSnackbarMessageChanged = { message -> snackbarMessage = message ?: "" },
                        onShowSnackbar = { showSnackbar = it },
                        onSnackbarSuccess = { success -> snackbarSuccess = success}
                    )
                }
            }
        }
    }*/
    if (showSnackbar) {
        CustomSnackbar(
            success = snackbarSuccess,
            type = snackbarMessage,
            onDismiss = {
                showSnackbar = false
                viewModel.resetSuccess()
            }
        )
    }
}

@Composable
fun UpperTextField(value: String, modifier: Modifier){
    Text(
        textAlign = TextAlign.Center,
        style = textInUpperBoxForgotPassword,
        text = value,
        modifier = modifier
    )
}

@Composable
fun LowerTextField(value: String, modifier: Modifier){
    Text(
        textAlign = TextAlign.Center,
        text = value,
        modifier = modifier,
        style = textInLowerBoxForgotPassword
    )

}
//@Preview
//@Composable

//fun ForgotPasswordUIPreview(){
//    ForgotPasswordUI()
//
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailButtonField(labelValue: String,
                     painterResource: Painter,
                     modifier: Modifier) {
    val textValue = remember { mutableStateOf("") }
    val viewModel: LoginViewModel = viewModel()
    val email by viewModel.email.observeAsState("")

    OutlinedTextField(
        modifier = modifier,
        label = { Text(text = labelValue) },
        value = if (labelValue == "Your Email") email else textValue.value,
        onValueChange = { newText ->
            val allowedCharsRegex = Regex("^[0-9a-zA-Z!@#\$%^&*()_+=\\-{}\\[\\]:\";'<>?,./]*\$")
            if (allowedCharsRegex.matches(newText)) {
                viewModel.onEmailChange(newText)
            }
            /*
            if (labelValue == "Your Email") {
                viewModel.onEmailChange(it)
            } else {
                textValue.value = it
            }
             */
        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = primaryLight,
            focusedLabelColor = secondaryLight,
            unfocusedBorderColor = Color.Gray,
            cursorColor = primaryLight,
            containerColor = Color.Transparent,
        ),
        leadingIcon = {
            Icon(painter = painterResource, contentDescription = "", modifier = Modifier.size(35.dp))
        },
        shape = MaterialTheme.shapes.medium.copy(all = CornerSize(15.dp))
    )
}


@Composable
fun ButtonWithSend(onSnackbarMessageChanged: (String?) -> Unit,
                   onShowSnackbar: (Boolean) -> Unit,
                   onSnackbarSuccess: (Boolean) -> Unit,
                   modifier: Modifier) {
    val textValue = remember { mutableStateOf("") }
    val viewModel: LoginViewModel = viewModel()
    Button(onClick = { viewModel.onForgotPasswordClick(){ result ->
            if (result == null) {
                onSnackbarMessageChanged("Wysłano maila")
                onSnackbarSuccess(true)
            } else {
                onSnackbarMessageChanged(result)
                onSnackbarSuccess(false)
            }
            onShowSnackbar(true)
        }
                     },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .shadow(
                elevation = 35.dp,
                spotColor = Color(0x406F7EC9),
                ambientColor = Color(0x406F7EC9)
            )

            .background(color = Color(0xFF007BFF), shape = RoundedCornerShape(10.dp))
            .padding(0.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .weight(1f) // Take up remaining space on the left
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "SEND",
                    style = sendButton,
                )
            }
            Image(painter = painterResource(id = R.drawable.arrow_right_icon), contentDescription = "Send", contentScale = ContentScale.Fit,
                modifier = Modifier.size(28.dp))
        }
    }

}
