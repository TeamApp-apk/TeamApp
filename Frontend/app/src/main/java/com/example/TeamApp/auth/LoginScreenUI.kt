package com.example.TeamApp.auth
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.TeamApp.R
import com.example.TeamApp.excludedUI.CustomSnackbar
import com.example.ui.theme.buttonLogIn
import com.example.ui.theme.fontFamily
import com.example.ui.theme.forgotPasswordLogin
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(navController: NavController){
    val configuration = LocalConfiguration.current
    val height = configuration.screenHeightDp.dp
    val width = configuration.screenWidthDp.dp
    val context = LocalContext.current
    val viewModel: LoginViewModel = viewModel()
    val loginSuccess by viewModel.loginSuccess.observeAsState()
    val registerSuccess by viewModel.registerSuccess.observeAsState()
    val emailSent by viewModel.emailSent.observeAsState()
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarSuccess by remember { mutableStateOf(false) }
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            if (context is LoginActivity) {
                context.handleSignInResult(data, navController)
            } else if (context is RegisterActivity) {
                context.handleSignInResult(data, navController)
            } else {
                Log.e("LoginScreen", "Unhandled context: ${context::class.java.simpleName}")
            }
        } else {
            Log.e("LoginScreen", "Google Sign-In failed")
        }
    }
    LaunchedEffect(Unit) {
        viewModel.mySetSignInLauncher(launcher)
        focusManager.clearFocus()
    }
    val gradientColors= listOf(
        Color(0xFFE8E8E8)
        ,Color(0xFF007BFF)
    )
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier

            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = gradientColors))
            .padding(horizontal = 28.dp)){
            Column {
                Spacer(modifier = Modifier.height(53.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow_icon),
                        contentDescription = "arrow",
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                viewModel.getToRegisterScreen(navController)
                            }
                            .size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.height(height * 0.00625f * 6 * density))
                UpperTextField(
                    value = "Witaj ponownie!",
                )
                Spacer(modifier = Modifier.height(height * 0.00625f * 8 * density))
                EmailBoxForLogin(labelValue ="E-Mail" , painterResource (id = R.drawable.mail_icon), nextFocusRequester = passwordFocusRequester, focusRequester = emailFocusRequester )
                Spacer(modifier = Modifier.height(height * 0.00625f * 6 / density))
                PasswordTextFieldForLogin(labelValue ="Hasło" , painterResource (id = R.drawable.lock_icon), nextFocusRequester = null, focusRequester = passwordFocusRequester )
                Spacer(modifier = Modifier.height(height * 0.00625f * 6 / density))
                Row(horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(8.dp * density))
                    ForgotPasswordTextField(navController)
                }
                Spacer(modifier = Modifier.height(height * 0.00625f * 8 * density))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center) // Center horizontally
                ) {
                    ButtonSignIN(navController,
                        onSnackbarMessageChanged = { message -> snackbarMessage = message ?: "" },
                        onShowSnackbar = { showSnackbar = it },
                        onSnackbarSuccess = { success -> snackbarSuccess = success})
                }
                Spacer(modifier = Modifier.height(height * 0.00625f * 5 * density))
                DividerTextComponent()
                Spacer(modifier = Modifier.height(height * 0.00625f * 5 * density))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center) // Center horizontally
                ) {
                    GoogleLoginButton()

                }
                Spacer(modifier = Modifier.height(height * 0.00625f * 5 * density))
                ClickableRegisterComponent(modifier = Modifier.align(Alignment.CenterHorizontally), navController)

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
//fun LoginScreenPreview(){
//    LoginScreen()
//}
@Composable
fun ToggleSwitch(){

    var isChecked by remember {
        mutableStateOf(false)
    }
    Switch(checked =isChecked , onCheckedChange ={isChecked=it},
        colors = SwitchDefaults.colors(
            checkedTrackColor = Color(0xFF0056B3)
        )
        , modifier = Modifier
            .width(32.dp)
            .height(16.dp)
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailBoxForLogin(labelValue: String, painterResource: Painter, nextFocusRequester: FocusRequester?, focusRequester: FocusRequester) {
    val viewModel: LoginViewModel = viewModel()
    val email by viewModel.email.observeAsState("")
    val focusManager = LocalFocusManager.current

    TextField(
        modifier = Modifier.fillMaxWidth()
            .focusRequester(focusRequester),
        label = { Text(text = labelValue) },
        value = email,
        onValueChange = { newText ->
            val allowedCharsRegex = Regex("^[0-9a-zA-Z!@#\$%^&*()_+=\\-{}\\[\\]:\";'<>?,./]*\$")
            if (allowedCharsRegex.matches(newText)) {
                viewModel.onEmailChange(newText)
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = if (nextFocusRequester != null) ImeAction.Next else ImeAction.Done, keyboardType = KeyboardType.Text),
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
        shape = MaterialTheme.shapes.medium.copy(all = CornerSize(15.dp))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextFieldForLogin(labelValue: String, painterResource: Painter, nextFocusRequester: FocusRequester?, focusRequester: FocusRequester) {
    val viewModel: LoginViewModel = viewModel()
    val password by viewModel.password.observeAsState("")
    val passwordVisible = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    TextField(
        modifier = Modifier.fillMaxWidth()
            .focusRequester(focusRequester),
        label = { Text(text = labelValue) },
        value = password,
        onValueChange = {
                newText ->
            val allowedCharsRegex = Regex("^[0-9a-zA-Z!@#\$%^&*()_+=\\-{}\\[\\]:\";'<>?,./]*\$")
            if (allowedCharsRegex.matches(newText)) {
                viewModel.onPasswordChanged(newText)
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = if (nextFocusRequester != null) ImeAction.Next else ImeAction.Done, keyboardType = KeyboardType.Password),
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
        shape = MaterialTheme.shapes.medium.copy(all = CornerSize(15.dp)),
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
fun ForgotPasswordTextField(navController: NavController) {
    val viewModel: LoginViewModel = viewModel()
    Text(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                viewModel.getToChangePasswordScreen(navController)
            },
        text = "Zapomniałeś hasła?",
        style = forgotPasswordLogin
    )
}


@Composable
fun ButtonSignIN(navController: NavController,
                 onSnackbarMessageChanged: (String?) -> Unit,
                 onShowSnackbar: (Boolean) -> Unit,
                 onSnackbarSuccess: (Boolean) -> Unit) {
    val viewModel: LoginViewModel = viewModel()
    val isLoading by viewModel.isLoading.observeAsState(false)

    Button(
        onClick = { viewModel.onLoginClick(navController){ result ->
            if (result == null) {
                onSnackbarMessageChanged("Logowanie przebiegło pomyślnie")
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
        modifier = Modifier
            .width(300.dp)
            .height(58.dp)
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
                        text = "Zaloguj się",
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
fun ClickableRegisterComponent(modifier: Modifier = Modifier, navController: NavController) {
    val viewModel: LoginViewModel = viewModel()
    val initialText = "Nie masz jeszcze konta?  "
    val loginText = "Zarejestruj się!"
    val annotatedString = buildAnnotatedString {
        pushStringAnnotation(tag = "initialText", annotation = initialText)
        withStyle(style = SpanStyle(color = Color(0xffe0e0e0), fontFamily =
        FontFamily(Font(R.font.proximanovaregular)), fontWeight = FontWeight.Normal)) {
            append(initialText)
        }
        pushStringAnnotation(tag = "logintext", annotation = loginText)
            withStyle(style = SpanStyle(color = Color(0xffe0e0e0),fontFamily =
            FontFamily(Font(R.font.proximanovabold)), fontWeight = FontWeight.ExtraBold)) {
                append(loginText)
            }
            pop()
    }
    ClickableText(
        text = annotatedString,
        modifier = modifier,
        onClick = {
            viewModel.getToRegisterScreen(navController)
        }
    )
}

