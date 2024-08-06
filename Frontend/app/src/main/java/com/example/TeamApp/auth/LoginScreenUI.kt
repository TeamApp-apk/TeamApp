package com.example.TeamApp.auth
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.TeamApp.R
import com.example.TeamApp.auth.ui.theme.TeamAppTheme

@Composable
fun LoginScreen(){
    val gradientColors= listOf(
        Color(0xFFE8E8E8)
        ,Color(0xFF007BFF)
    )
TeamAppTheme {
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
                Image(painterResource(id = R.drawable.arrow),
                    contentDescription = "arrow",
                    modifier = Modifier
                        .clickable { }
                        .size(22.dp))

            }
            Spacer(modifier = Modifier.height(160.dp))
            UpperTextField(value = "Witaj ponownie!")
            Spacer(modifier = Modifier.height(28.dp))
            EmailBoxForLogin(labelValue ="E-Mail" , painterResource (id = R.drawable.message) )
            Spacer(modifier = Modifier.height(20.dp))
            PasswordTextFieldForLogin(labelValue ="Hasło" , painterResource (id = R.drawable.lock) )
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.width(8.dp))
                RememberMeTextField()
                Spacer(modifier = Modifier.width(64.dp))
                ForgotPasswordTextField()
            }
            Spacer(modifier = Modifier.height(36.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center) // Center horizontally
            ) {
                ButtonSignIN()

            }
            Spacer(modifier = Modifier.height(24.dp))
            DividerTextComponent()
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center) // Center horizontally
            ) {
                GoogleLoginButton()

            }
            Spacer(modifier = Modifier.height(24.dp))
            ClickableRegisterComponent(modifier = Modifier.align(Alignment.CenterHorizontally))

        }

        }

    }
}

}
@Composable
@Preview
fun LoginScreenPreview(){
    LoginScreen()
}
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
                .width(32.3.dp)
                .height(19.dp)


        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailBoxForLogin(labelValue: String, painterResource: Painter) {
    val textValue = remember { mutableStateOf("") }
    val viewModel: LoginViewModel = viewModel()
    val email by viewModel.email.observeAsState("")

    TextField(
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = labelValue) },
        value = if (labelValue == "Your Email") email else textValue.value,
        onValueChange = {
            if (labelValue == "Your Email") {
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
fun PasswordTextFieldForLogin(labelValue: String, painterResource: Painter) {
    val viewModel: LoginViewModel = viewModel()
    val password by viewModel.password.observeAsState("")
    val passwordVisible = remember { mutableStateOf(false) }
    TextField(
        modifier =Modifier.fillMaxWidth()
        ,

        label = { Text(text = labelValue) },
        value = password,
        onValueChange = { viewModel.onPasswordChanged(it) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = Color.White
        )

        ,
        leadingIcon = {
            Icon(painter = painterResource, contentDescription ="" ,
                modifier = Modifier
                    .padding(1.dp)
                    .width(22.dp)
                    .height(22.dp))
        },
        shape = MaterialTheme.shapes.medium.copy(all= CornerSize(15.dp)),

        trailingIcon = {
            val iconImage= if(passwordVisible.value){
                Icons.Filled.Visibility

            }
            else{
                Icons.Filled.VisibilityOff
            }
            var description=if(passwordVisible.value){
                "Hide password"
            }
            else{
                "Show password"
            }

            IconButton(onClick ={
                passwordVisible.value=!passwordVisible.value
            } ){
                Icon(imageVector = iconImage, contentDescription = "")
            }

        },
        visualTransformation = if(passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation()

    )
}
@Composable
fun ForgotPasswordTextField(){
    Text(modifier = Modifier
        .clickable {/*TODO
          */
        }
        ,

        text = "Zapomniałeś hasła?",
        style = TextStyle(
            fontSize = 14.sp,
            lineHeight = 23.sp,
            fontFamily = FontFamily(Font(R.font.robotoregular)),
            fontWeight = FontWeight(400),
            color = Color(0xFF003366),
            textAlign = TextAlign.Right,
        )
    )
}

@Composable
fun RememberMeTextField() {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ToggleSwitch()
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            modifier = Modifier
                ,
            text = "Zapamiętaj mnie",
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 23.sp,
                fontFamily = FontFamily(Font(R.font.robotoregular)), // Ensure R.font.robotoregular is a valid reference
                fontWeight = FontWeight(400),
                color = Color(0xFF003366),
                textAlign = TextAlign.Right
            )
        )
    }
}
@Composable
fun ButtonSignIN() {
    val textValue = remember { mutableStateOf("") }
    val viewModel: LoginViewModel = viewModel()
    val context = LocalContext.current
    Button(onClick = { viewModel.onLoginClick(context) }, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
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
                    text = "Zaloguj się",

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
fun ClickableRegisterComponent(modifier: Modifier = Modifier) {
    val viewModel: LoginViewModel = viewModel()
    val context = LocalContext.current
    val initialText = "Nie masz jeszcze konta?  "
    val loginText = "Zarejestruj się!"
    val annotatedString = buildAnnotatedString {
        append(initialText)
        pushStringAnnotation(tag = "logintext", annotation = loginText)
        withStyle(style = SpanStyle(color = Color.Blue)) {
            append(loginText)
        }
        pop()
    }

    ClickableText(
        text = annotatedString,
        modifier = modifier,
        onClick = {
            viewModel.getToRegisterScreen(context)
            //viewModel.onLoginClick(context)
        }
    )
}