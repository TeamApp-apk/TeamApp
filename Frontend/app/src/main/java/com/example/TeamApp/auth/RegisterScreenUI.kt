package com.example.TeamApp.auth

import android.app.Activity
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
import com.example.compose.secondaryLight
import com.example.TeamApp.R


/*
do oifowania powtorzenie hasla sa dwie oddzielne funkcje

Ten remember password pamieta dwa te same pola chacik pisze ze cos we viewmodel

 */
@Composable
fun RegisterScreen(navController: NavController){
    val context = LocalContext.current
    val view = LocalView.current
    SideEffect {
        val window = (context as? Activity)?.window ?: return@SideEffect
        window.statusBarColor = Color.Transparent.toArgb()
        WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
    }
    val gradientColors= listOf(
        Color(0xFFE8E8E8)
        ,Color(0xFF007BFF)
    )
Surface(modifier = Modifier.fillMaxSize()) {
    Box(modifier = Modifier

        .fillMaxSize()
        .background(brush = Brush.linearGradient(colors = gradientColors))
        .padding(horizontal = 28.dp) ){
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Spacer(modifier = Modifier.height(32.dp))
            UpperTextField(value = "Dołącz!")
            Spacer(modifier = Modifier.height(24.dp))
            NameAndEmailBox(labelValue = "Nazwa", painterResource (id = R.drawable.user_icon))
            Spacer(modifier = Modifier.height(20.dp))
            NameAndEmailBox(labelValue = "E-mail", painterResource (id = R.drawable.mail_icon) )
            Spacer(modifier = Modifier.height(20.dp))
            PasswordTextField(labelValue ="Hasło" , painterResource (id=R.drawable.lock_icon) )
            Spacer(modifier = Modifier.height(20.dp))
            PasswordTextFieldRepeatPassword(labelValue ="Powtórz hasło" , painterResource (id=R.drawable.lock_icon) )
            Spacer(modifier = Modifier.height(40.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center) // Center horizontally
            ) {
                ButtonSignUP()

            }
            Spacer(modifier = Modifier.height(36.dp))
            DividerTextComponent()
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center) // Center horizontally
            ) {
                GoogleLoginButton()

            }
            Spacer(modifier = Modifier.height(20.dp))
            ClickableLoginTextComponent(modifier = Modifier.align(Alignment.CenterHorizontally), navController)
        }

        }

}
}
//@Composable
//@Preview
//fun FinalRegisterScreenPreview(){
//    RegisterScreen()
//}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameAndEmailBox(labelValue: String, painterResource: Painter) {
    val textValue = remember { mutableStateOf("") }
    val viewModel: LoginViewModel = viewModel()
    val email by viewModel.email.observeAsState("")

    TextField(
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = labelValue) },
        value = if (labelValue == "E-mail") email else textValue.value,
        onValueChange = {
            if (labelValue == "E-mail") {
                viewModel.onEmailChange(it)
            } else {
                textValue.value = it
            }
        },
        keyboardOptions = KeyboardOptions.Default,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = Color.White

        ),
        leadingIcon = {
            Icon(painter = painterResource, contentDescription = "", modifier = Modifier.padding(1.dp)
                .width(22.dp)
                .height(22.dp))
        },
        shape = MaterialTheme.shapes.medium.copy(all = CornerSize(15.dp))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(labelValue: String, painterResource: Painter) {
    val viewModel: LoginViewModel = viewModel()
    val password by viewModel.password.observeAsState("")
    val passwordVisible = remember { mutableStateOf(false) }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = labelValue) },
        value = password,
        onValueChange = { viewModel.onPasswordChanged(it) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = Color.White
        ),
        leadingIcon = {
            Icon(painter = painterResource, contentDescription = "", modifier = Modifier.padding(1.dp).width(22.dp).height(22.dp))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextFieldRepeatPassword(labelValue: String, painterResource: Painter) {
    val viewModel: LoginViewModel = viewModel()
    val confirmPassword by viewModel.confirmPassword.observeAsState("")
    val passwordVisible = remember { mutableStateOf(false) }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = labelValue) },
        value = confirmPassword,
        onValueChange = { viewModel.onConfirmPasswordChanged(it) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = Color.White
        ),
        leadingIcon = {
            Icon(painter = painterResource, contentDescription = "", modifier = Modifier.padding(1.dp).width(22.dp).height(22.dp))
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
fun ButtonSignUP() {
    val context = LocalContext.current
    val textValue = remember { mutableStateOf("") }
    val viewModel: LoginViewModel = viewModel()
    Button(onClick = { viewModel.onRegisterClick(context) }, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
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
            Box(
                modifier = Modifier
                    .weight(1f) // Take up remaining space on the left
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Zarejestruj się!",

                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.robotobold)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF003366),
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.sp

                )
            }
            Image(painter = painterResource(id = R.drawable.send), contentDescription = "Send", contentScale = ContentScale.Fit,
                modifier = Modifier.size(28.dp))
        }
    }

}
@Composable
fun DividerTextComponent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            color = secondaryLight,
            thickness = 1.dp
        )
        Text(text = "lub", fontSize = 14.sp)
        Divider(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
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
        append(initialText)
        pushStringAnnotation(tag = "logintext", annotation = loginText)
        withStyle(style = SpanStyle(color = Color.Blue, fontFamily =
        FontFamily(Font(R.font.robotobold)) )) {
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
fun GoogleLoginButton() {
    val textValue = remember { mutableStateOf("") }
    val viewModel: LoginViewModel = viewModel()
    val context = LocalContext.current
    Button(onClick = { viewModel.signInWithGoogle(context) }, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .shadow(
                elevation = 30.dp,
                spotColor = Color(0x40D3D4E2),
                ambientColor = Color(0x40D3D4E2)
            )

            .padding(0.5.dp)
            .width(300.dp)
            .height(56.dp)
            .background(color = Color.White, shape = RoundedCornerShape(size = 12.dp))
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(painter = painterResource(id = R.drawable.googleicon), contentDescription = "googleRegister", contentScale = ContentScale.Fit,
                modifier = Modifier.size(28.dp))
            Box(
                modifier = Modifier
                    .weight(1f) // Take up remaining space on the left
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Zaloguj się przez Google",

                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.robotoregular)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF003366),
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.sp

                )
            }

        }
    }

}

