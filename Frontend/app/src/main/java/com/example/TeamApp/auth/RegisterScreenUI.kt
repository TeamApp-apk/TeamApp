package com.example.TeamApp.auth

import android.app.Activity
import android.content.res.Resources
import android.util.DisplayMetrics
import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.compose.secondaryLight
import com.example.TeamApp.R
import com.example.ui.theme.textInBoxes
import kotlinx.coroutines.time.delay
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import com.example.TeamApp.excludedUI.CustomSnackbar


/*
do oifowania powtorzenie hasla sa dwie oddzielne funkcje

Ten remember password pamieta dwa te same pola chacik pisze ze cos we viewmodel

 */

//height * 0.00625f * n/4, where n is hard coded dpi value

val metrics = Resources.getSystem().displayMetrics
val density = metrics.density / 2

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
        viewModel.mySetSignInLauncher(launcher)
        focusManager.clearFocus()

    }
    SideEffect {
        val window = (context as? Activity)?.window ?: return@SideEffect
        window.statusBarColor = Color.Transparent.toArgb()
        WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
    }
    val configuration = LocalConfiguration.current
    val height = configuration.screenHeightDp.dp
    val width = configuration.screenWidthDp.dp
    val gradientColors= listOf(
        Color(0xFFE8E8E8)
        ,Color(0xFF007BFF)
    )
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier

            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = gradientColors))
            .padding(horizontal = width * 0.078f) ){
            Column() {
                Spacer(modifier = Modifier.height(height * 0.00625f * 5))
                Spacer(modifier = Modifier.height(height * 0.00625f * 8))
                UpperTextField(value = "Dołącz do nas!", )
                Spacer(modifier = Modifier.height(height * 0.00625f * 6 ))
                NameAndEmailBox(labelValue = "Nazwa", painterResource (id = R.drawable.user_icon), nextFocusRequester = emailFocusRequester, focusRequester = nameFocusRequester)
                Spacer(modifier = Modifier.height(height * 0.00625f * 10 / density))
                NameAndEmailBox(labelValue = "E-mail", painterResource (id = R.drawable.mail_icon), nextFocusRequester = passwordFocusRequester, focusRequester = emailFocusRequester)
                Spacer(modifier = Modifier.height(height * 0.00625f * 10 / density))
                PasswordTextField(labelValue ="Hasło" , painterResource (id=R.drawable.lock_icon),  nextFocusRequester = repeatpasswordFocusRequester, focusRequester = passwordFocusRequester)
                Spacer(modifier = Modifier.height(height * 0.00625f * 10 / density))
                PasswordTextFieldRepeatPassword(labelValue ="Powtórz hasło", painterResource (id=R.drawable.lock_icon), nextFocusRequester = null, focusRequester = repeatpasswordFocusRequester)
                Spacer(modifier = Modifier.height(height * 0.00625f * 8 / density))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center) // Center horizontally
                ) {
                    ButtonSignUP(navController,
                        onSnackbarMessageChanged = { message -> snackbarMessage = message ?: "" },
                        onShowSnackbar = { showSnackbar = it },
                        onSnackbarSuccess = { success -> snackbarSuccess = success}
                    )
                }
                Spacer(modifier = Modifier.height(height * 0.00625f * 9 / density))
                DividerTextComponent()
                Spacer(modifier = Modifier.height(height * 0.00625f * 4))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center) // Center horizontally
                ) {
                    GoogleLoginButton()

                }
                Spacer(modifier = Modifier.height(height * 0.00625f * 5 * density))
                ClickableLoginTextComponent(modifier = Modifier.align(Alignment.CenterHorizontally), navController)
            }
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
fun NameAndEmailBox(labelValue: String, painterResource: Painter, nextFocusRequester: FocusRequester?, focusRequester: FocusRequester) {
    val textValue = remember { mutableStateOf("") }
    val viewModel: LoginViewModel = viewModel()
    val email by viewModel.email.observeAsState("")
    val configuration = LocalConfiguration.current
    val height = configuration.screenHeightDp.dp
    val width = configuration.screenWidthDp.dp
    val focusManager = LocalFocusManager.current


    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(height * 0.00625f * 8 * density)
            .focusRequester(focusRequester),
        label = { Text(
            text = labelValue,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.proximanovaregular)), // Apply custom font here
                fontSize = 16.sp // Adjust font size if needed
            )
            ) },
        value = if (labelValue == "E-mail") email else textValue.value,
        onValueChange = {
                newText ->

            if (labelValue == "E-mail") {
                val allowedCharsRegex = Regex("^[0-9a-zA-Z!@#\$%^&*()_+=\\-{}\\[\\]:\";'<>?,./]*\$")
                if (allowedCharsRegex.matches(newText)) {
                    viewModel.onEmailChange(newText)

                }
            } else {
                val allowedCharsRegex = Regex("^[0-9\\sa-zA-Z!@#\$%^&*()_+=\\-{}\\[\\]:\";'<>?,./]*\$")
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
            Icon(painter = painterResource, contentDescription = "", modifier = Modifier
                .padding(1.dp)
                .width(22.dp)
                .height(22.dp))
        },
        shape = MaterialTheme.shapes.medium.copy(all = CornerSize(height * 0.023f))
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(labelValue: String, painterResource: Painter, nextFocusRequester: FocusRequester?, focusRequester: FocusRequester) {
    val viewModel: LoginViewModel = viewModel()
    val password by viewModel.password.observeAsState("")
    val passwordVisible = remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val height = configuration.screenHeightDp.dp
    val width = configuration.screenWidthDp.dp
    val textValue = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(height * 0.00625f * 8 * density)
            .focusRequester(focusRequester),
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
        shape = MaterialTheme.shapes.medium.copy(all = CornerSize(height * 0.023f)),
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
fun PasswordTextFieldRepeatPassword(labelValue: String, painterResource: Painter, nextFocusRequester: FocusRequester?, focusRequester: FocusRequester) {
    val viewModel: LoginViewModel = viewModel()
    val confirmPassword by viewModel.confirmPassword.observeAsState("")
    val passwordVisible = remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val height = configuration.screenHeightDp.dp
    val width = configuration.screenWidthDp.dp
    val focusManager = LocalFocusManager.current

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(height * 0.00625f * 8 * density)
            .focusRequester(focusRequester),
        label = { Text(
            text = labelValue,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.proximanovaregular)), // Apply custom font here
                fontSize = 16.sp // Adjust font size if needed
            )
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
        shape = MaterialTheme.shapes.medium.copy(all = CornerSize(height * 0.023f)),
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
                 onSnackbarSuccess: (Boolean) -> Unit) {
    val context = LocalContext.current
    val viewModel: LoginViewModel = viewModel()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density // Użycie gęstości dla rozmiarów
    val height = configuration.screenHeightDp.dp
    val width = configuration.screenWidthDp.dp

    
    Button(
        onClick = {viewModel.onRegisterClick(navController) { result ->
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
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .width(width * 0.83f * density)
            .height(height * 0.09f)
            .shadow(
                elevation = 35.dp,
                spotColor = Color(0x406F7EC9),
                ambientColor = Color(0x406F7EC9)
            )
            .background(color = Color(0xFF007BFF), shape = RoundedCornerShape(height * 0.023f))
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
                        text = "Zarejestruj się!",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.proximanovabold)),
                        fontWeight = FontWeight(600),
                        color = Color(0xffe0e0e0),
                        textAlign = TextAlign.Center,
                        letterSpacing = 1.sp
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
fun DividerTextComponent() {
    val configuration = LocalConfiguration.current
    val height = configuration.screenHeightDp.dp
    val width = configuration.screenWidthDp.dp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = height * 0.00625f * 2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = width * 0.02f),
            color = secondaryLight,
            thickness = 1.dp
        )
        Text(text = "lub", fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.proximanovaregular)))
        Divider(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = width * 0.02f),
            color = secondaryLight,
            thickness = 1.dp
        )
    }
}
@Composable
fun ClickableLoginTextComponent(modifier: Modifier = Modifier, navController: NavController) {
    val viewModel: LoginViewModel = viewModel()
    val context = LocalContext.current
    val initialText = "Masz już konto?  "
    val loginText = "Zaloguj się!"
    val annotatedString = buildAnnotatedString {
        pushStringAnnotation(tag = "initialText", annotation = initialText)
        withStyle(style = SpanStyle(color = Color(0xffe0e0e0), fontFamily =
        FontFamily(Font(R.font.proximanovaregular)), fontWeight = FontWeight(100)
        )) {
            append(initialText)
        }
        pushStringAnnotation(tag = "logintext", annotation = loginText)
        withStyle(style = SpanStyle(color = Color(0xffe0e0e0), fontFamily =
        FontFamily(Font(R.font.proximanovabold)), fontWeight = FontWeight(900) )) {
            append(loginText)
        }

        pop()
    }

    ClickableText(
        text = annotatedString,
        modifier=modifier,
        onClick = {
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





