package com.example.TeamApp.auth

import android.app.Activity
import android.content.res.Resources
import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.compose.secondaryLight
import com.example.TeamApp.R
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutBaseScope
import androidx.constraintlayout.compose.Dimension
import com.example.TeamApp.excludedUI.CustomSnackbar
import com.example.ui.theme.buttonLogIn
import com.example.ui.theme.textInUpperBoxForgotPassword


/*
do oifowania powtorzenie hasla sa dwie oddzielne funkcje
Ten remember password pamieta dwa te same pola chacik pisze ze cos we viewmodel
*/

@Composable
fun RegisterScreen(navController: NavController){
    val context = LocalContext.current
    val view = LocalView.current
    val viewModel: LoginViewModel = viewModel()
    val loginSuccess by viewModel.loginSuccess.observeAsState()
    val registerSuccess by viewModel.registerSuccess.observeAsState()
    val emailSent by viewModel.emailSent.observeAsState()
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarSuccess by remember { mutableStateOf(false) }
    val nameFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val repeatpasswordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            (context as RegisterActivity).handleSignInResult(data, navController)
        } else {
            Log.e("LoginScreen", "Google Sign-In failed")
        }
    }
    LaunchedEffect(Unit) {
        delay(1000)
        viewModel.mySetSignInLauncher(launcher)
        focusManager.clearFocus()
    }
    SideEffect {
        val window = (context as? Activity)?.window ?: return@SideEffect
        window.statusBarColor = Color.Transparent.toArgb()
        WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
    }

    val gradientColors= listOf(
        Color(0xFFE8E8E8)
        ,Color(0xFF007BFF)
    )


    Surface( modifier = Modifier.fillMaxSize() ) {
        //we are drawing on top of the box since guidelines do not work with that for some reason?
        Box(modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = gradientColors)) ){}

        ConstraintLayout (modifier = Modifier.fillMaxSize()) {
            val (welcomeText, signUp, emailBox, nameBox, passBox, repeatBox,
                divider, googleButton, logInText) = createRefs()

            val startGuideline = createGuidelineFromStart(0.11f)
            val endGuideline = createGuidelineFromStart(0.9f)
            val topWelcomeText = createGuidelineFromTop(0.085f)
            val bottomWelcomeText = createGuidelineFromTop(0.14f)
            Text(
                textAlign = TextAlign.Center,
                style = textInUpperBoxForgotPassword,
                text = "Stwórz konto",
                modifier = Modifier
                    .constrainAs(welcomeText) {
                        top.linkTo(topWelcomeText)
                        bottom.linkTo(bottomWelcomeText)
                        start.linkTo(startGuideline)
                        end.linkTo(endGuideline)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
            )

            /*
            val nameStart = createGuidelineFromStart(0.1f)
            val nameEnd = createGuidelineFromStart(0.9f)
            val nameTop = createGuidelineFromTop(0.17f)
            val nameBottom = createGuidelineFromTop(0.235f)
            NameAndEmailBox(labelValue = "Nazwa", painterResource (id = R.drawable.user_icon),
                nextFocusRequester = emailFocusRequester,
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
                    .focusRequester(nameFocusRequester)
            )
            */

            val emailStart = createGuidelineFromStart(0.1f)
            val emailEnd = createGuidelineFromStart(0.9f)
            val emailTop = createGuidelineFromTop(0.27f)
            val emailBottom = createGuidelineFromTop(0.335f)
            NameAndEmailBox(labelValue = "E-mail", painterResource (id = R.drawable.mail_icon),
                nextFocusRequester = passwordFocusRequester,
                focusRequester = emailFocusRequester,
                modifier = Modifier
                    .constrainAs(emailBox) {
                        top.linkTo(emailTop)
                        bottom.linkTo(emailBottom)
                        start.linkTo(emailStart)
                        end.linkTo(emailEnd)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
                    .focusRequester(emailFocusRequester)
            )

            val passStart = createGuidelineFromStart(0.1f)
            val passEnd = createGuidelineFromStart(0.9f)
            val passTop = createGuidelineFromTop(0.37f)
            val passBottom = createGuidelineFromTop(0.435f)
            PasswordTextField(labelValue ="Hasło" , painterResource (id=R.drawable.lock_icon),
                nextFocusRequester = repeatpasswordFocusRequester,
                focusRequester = passwordFocusRequester,
                modifier = Modifier
                    .constrainAs(passBox) {
                        top.linkTo(passTop)
                        bottom.linkTo(passBottom)
                        start.linkTo(passStart)
                        end.linkTo(passEnd)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
                    .focusRequester(passwordFocusRequester)
            )

            val repeatStart = createGuidelineFromStart(0.1f)
            val repeatEnd = createGuidelineFromStart(0.9f)
            val repeatTop = createGuidelineFromTop(0.47f)
            val repeatBottom = createGuidelineFromTop(0.535f)
            PasswordTextFieldRepeatPassword(labelValue ="Powtórz hasło" , painterResource (id=R.drawable.lock_icon),
                nextFocusRequester = null,
                focusRequester = repeatpasswordFocusRequester,
                modifier = Modifier
                    .constrainAs(repeatBox) {
                        top.linkTo(repeatTop)
                        bottom.linkTo(repeatBottom)
                        start.linkTo(repeatStart)
                        end.linkTo(repeatEnd)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
                    .focusRequester(repeatpasswordFocusRequester)
            )

            val signUpStart = createGuidelineFromStart(0.1f)
            val signUpEnd = createGuidelineFromStart(0.9f)
            val signUpTop = createGuidelineFromTop(0.57f)
            val signUpBottom = createGuidelineFromTop(0.66f)
            ButtonSignUP(navController,
                onSnackbarMessageChanged = { message -> snackbarMessage = message ?: "" },
                onShowSnackbar = { showSnackbar = it },
                onSnackbarSuccess = { success -> snackbarSuccess = success},
                modifier = Modifier
                    .constrainAs(signUp) {
                        top.linkTo(signUpTop)
                        bottom.linkTo(signUpBottom)
                        start.linkTo(signUpStart)
                        end.linkTo(signUpEnd)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
            )

            val dividerTop = createGuidelineFromTop(0.7f)
            val dividerStart = createGuidelineFromStart(0.1f)
            val dividerEnd = createGuidelineFromStart(0.9f)
            DividerTextComponent(modifier = Modifier
                .constrainAs(divider) {
                    top.linkTo(dividerTop)
                    start.linkTo(dividerStart)
                    end.linkTo(dividerEnd)
                    width = Dimension.fillToConstraints
                }
            )

            val googleStart = createGuidelineFromStart(0.1f)
            val googleEnd = createGuidelineFromStart(0.9f)
            val googleTop = createGuidelineFromTop(0.75f)
            val googleBottom = createGuidelineFromTop(0.85f)
            GoogleLoginButton(modifier = Modifier
                .shadow(
                    elevation = 30.dp,
                    spotColor = Color(0x40D3D4E2),
                    ambientColor = Color(0x40D3D4E2)
                )
                .padding(1.dp)
                .background(color = Color.White, shape = RoundedCornerShape(size = 20.dp))
                .constrainAs(googleButton) {
                    top.linkTo(googleTop)
                    bottom.linkTo(googleBottom)
                    start.linkTo(googleStart)
                    end.linkTo(googleEnd)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            )

            val logInTextTop = createGuidelineFromTop(0.9f)
            ClickableLoginTextComponent ( modifier = Modifier
                .constrainAs(logInText) {
                    top.linkTo(logInTextTop)
                    centerHorizontallyTo(parent)
                },
                navController = navController
            )
        }
    }
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

//@Composable
//@Preview
//fun FinalRegisterScreenPreview(){
//    RegisterScreen()
//}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameAndEmailBox(labelValue: String, painterResource: Painter,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(labelValue: String, painterResource: Painter,
                      nextFocusRequester: FocusRequester?,
                      focusRequester: FocusRequester,
                      modifier: Modifier) {
    val viewModel: LoginViewModel = viewModel()
    val password by viewModel.password.observeAsState("")
    val passwordVisible = remember { mutableStateOf(false) }
    val textValue = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    TextField(
        modifier = modifier,
        label = { Text(
            text = labelValue,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.proximanovaregular)), // Apply custom font here
                fontSize = 16.sp // Adjust font size if needed
            )
        ) },
        value = password,
        onValueChange = { newText ->

            val allowedCharsRegex = Regex("^[0-9a-zA-Z!@#\$%^&*()_+=\\-{}\\[\\]:\";'<>?,./]*\$")
            if (allowedCharsRegex.matches(newText)) {
                viewModel.onPasswordChanged(newText)
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = if (nextFocusRequester != null) ImeAction.Next else ImeAction.Done,
            keyboardType = KeyboardType.Password),
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
            Icon(painter = painterResource, contentDescription = "", modifier = Modifier
                .padding(1.dp)
                .width(22.dp)
                .height(22.dp))
        },
        shape = MaterialTheme.shapes.medium.copy(all = CornerSize(23.dp)),
        trailingIcon = {
            val iconImage = if (passwordVisible.value) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            val description = if (passwordVisible.value) {
                "Hide password"
            } else {
                "Show password"
            }
            IconButton(onClick = {
                passwordVisible.value = !passwordVisible.value
            }) {
                Icon(imageVector = iconImage, contentDescription = description)
            }
        },
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextFieldRepeatPassword(labelValue: String, painterResource: Painter,
                                    nextFocusRequester: FocusRequester?,
                                    focusRequester: FocusRequester,
                                    modifier: Modifier) {
    val viewModel: LoginViewModel = viewModel()
    val confirmPassword by viewModel.confirmPassword.observeAsState("")
    val passwordVisible = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    TextField(
        modifier = modifier,
        label = { Text(
            text = labelValue,
        ) },
        value = confirmPassword,
        onValueChange = { newText ->
            val allowedCharsRegex = Regex("^[0-9a-zA-Z!@#\$%^&*()_+=\\-{}\\[\\]:\";'<>?,./]*\$")
            if (allowedCharsRegex.matches(newText)) {
                viewModel.onConfirmPasswordChanged(newText)
            }
             },
        keyboardOptions = KeyboardOptions(
            imeAction = if (nextFocusRequester != null) ImeAction.Next else ImeAction.Done,
            keyboardType = KeyboardType.Password),
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
            Icon(painter = painterResource, contentDescription = "", modifier = Modifier
                .padding(1.dp)
                .width(22.dp)
                .height(22.dp))
        },
        shape = MaterialTheme.shapes.medium.copy(all = CornerSize(23.dp)),
        trailingIcon = {
            val iconImage = if (passwordVisible.value) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            val description = if (passwordVisible.value) {
                "Hide password"
            } else {
                "Show password"
            }
            IconButton(onClick = {
                passwordVisible.value = !passwordVisible.value
            }) {
                Icon(imageVector = iconImage, contentDescription = description)
            }
        },
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation()
    )
}

@Composable
fun ButtonSignUP(navController: NavController,
                 onSnackbarMessageChanged: (String?) -> Unit,
                 onShowSnackbar: (Boolean) -> Unit,
                 onSnackbarSuccess: (Boolean) -> Unit,
                 modifier: Modifier) {
    val viewModel: LoginViewModel = viewModel()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val gradientColors = listOf(
        Color(0xB5D46161),
        Color(0xFF007BFF)
    )

    Button(
        onClick = {
            viewModel.onRegisterClick(navController) { result ->
            if (result == null) {
                onSnackbarMessageChanged("Rejestracja przebiegła pomyślnie")
                onSnackbarSuccess(true)
            } else {
                onSnackbarMessageChanged(result)
                onSnackbarSuccess(false)
            }
            onShowSnackbar(true)
            }
        }, // Zmieniono navController na context
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .shadow(
                elevation = 20.dp,
                spotColor = Color(0x406F7EC9),
                ambientColor = Color(0x406F7EC9),
                shape = RoundedCornerShape(20.dp)
            )
            .background(brush = Brush.horizontalGradient(gradientColors), shape = RoundedCornerShape(20.dp))
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
                        .weight(1f)
                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Zarejestruj się",
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

@Composable
fun DividerTextComponent(modifier: Modifier) {
    Row(
        modifier = modifier,
    verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp),
        color = secondaryLight,
        thickness = 1.dp
        )
        Text(text = "lub", fontSize = 14.sp)
        Divider(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp),
        color = secondaryLight,
        thickness = 1.dp
        )
    }
}

@Composable
fun ClickableLoginTextComponent(modifier: Modifier = Modifier, navController: NavController) {
    val viewModel: LoginViewModel = viewModel()
    val initialText = "Masz już konto?  "
    val loginText = "Zaloguj się!"
    var isClicked by remember { mutableStateOf(false) }
    val annotatedString = buildAnnotatedString {
        pushStringAnnotation(tag = "initialText", annotation = initialText)
        withStyle(style = SpanStyle(color = if (isClicked) Color.LightGray else Color(0xffe0e0e0), fontFamily =
        FontFamily(Font(R.font.proximanovaregular)), fontWeight = FontWeight(100), fontSize = 16.sp
        )) {
            append(initialText)
        }
        pushStringAnnotation(tag = "logintext", annotation = loginText)
        withStyle(style = SpanStyle(color = if (isClicked) Color.LightGray else Color(0xffe0e0e0), fontFamily =
        FontFamily(Font(R.font.proximanovabold)), fontWeight = FontWeight(900),fontSize = 16.sp )) {
            append(loginText)
        }
        pop()
    }

    ClickableText(
        text = annotatedString,
        modifier = modifier,
        onClick = {
            isClicked = true
            viewModel.getToLoginScreen(navController)
            //viewModel.onLoginClick(context)
        }
    )
}
@Composable
fun LoadingSpinner(isLoading: Boolean) {
    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000))
        ) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 4.dp
            )
        }
    }
}