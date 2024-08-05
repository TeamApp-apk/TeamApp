package com.example.TeamApp.auth

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
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
import com.example.compose.primaryLight
import com.example.compose.secondaryLight
import com.example.TeamApp.R
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen() {
    val viewModel : LoginViewModel = viewModel()
    val loginSuccess by viewModel.loginSuccess.observeAsState()
    val registerSuccess by viewModel.registerSuccess.observeAsState()
    val emailSent by viewModel.emailSent.observeAsState()
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(loginSuccess, registerSuccess, emailSent) {
        when {
            emailSent != null -> {
                snackbarMessage = "Email"
                snackbarSuccess = emailSent ?: false
                showSnackbar = true
            }
            loginSuccess != null -> {
                snackbarMessage = "login"
                snackbarSuccess = loginSuccess ?: false
                showSnackbar = true
            }
            registerSuccess != null -> {
                snackbarMessage = "register"
                snackbarSuccess = registerSuccess ?: false
                showSnackbar = true
            }
        }
    }
    Surface( modifier = Modifier
        .fillMaxSize()
        .padding(28.dp)
        .background(Color.White))
        {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center

            ) {
            TextComponentUpper1(value = "Hey there, ")
            TextComponentUpper2(value = "Create an account")
            Spacer(modifier = Modifier.height(30.dp))
            MyTextField(labelValue = "Name", painterResource(id =R.drawable.usericon ))
            Spacer(modifier = Modifier.height(15.dp))
            MyTextField(labelValue = "E-mail", painterResource(id =R.drawable.emailicon ))
            Spacer(modifier = Modifier.height(15.dp))
            PasswordTextField(
                labelValue = "Password",
                painterResource = painterResource(id = R.drawable.passwordicon )
            )
            Spacer(modifier = Modifier.height(15.dp))
            CheckBox(value = "Terms and conditions")
            Spacer(modifier = Modifier.height(110.dp))
            ButtonComponent(value = "Register")
            Spacer(modifier = Modifier.height(20.dp))
            DividerTextComponent()
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier
                .padding(21.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                FaceBookButton()
                Spacer(modifier = Modifier.width(45.dp))
                GoogleButton()
            }
            ClickableLoginTextComponent(modifier = Modifier.align(Alignment.CenterHorizontally))
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
@Composable
fun TextComponentUpper1(value: String){
    Text(text = value,
        modifier = Modifier
            .fillMaxWidth()

            .heightIn(min = 40.dp)
        ,style= TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Normal, fontStyle = FontStyle.Normal),
        textAlign = TextAlign.Center

    )

}
@Composable
fun TextComponentUpper2(value:String){
    Text(text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn()
            ,
        style = TextStyle(fontStyle =FontStyle.Normal, fontWeight = FontWeight.Bold, fontSize = 30.sp ),
        textAlign = TextAlign.Center

    )

}


@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(labelValue: String, painterResource: Painter) {
    val textValue = remember { mutableStateOf("") }
    val viewModel: LoginViewModel = viewModel()
    val email by viewModel.email.observeAsState("")

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth()
            ,
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
        shape = MaterialTheme.shapes.medium.copy(all= CornerSize(15.dp))
    )
}

@Composable
fun CheckBox(value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(56.dp)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val checkedState = remember { mutableStateOf(false) }
        Checkbox(
            checked = checkedState.value,
            onCheckedChange = {
                checkedState.value = it
            }
        )

        Spacer(modifier = Modifier.width(1.dp))

        ClickableTextComponent()
    }
}
@Composable

fun ClickableTextComponent() {

    val initialText = "By continuing you accept our "
    val privacyPolicyText = "Privacy Policy"
    val annotatedString = buildAnnotatedString {
        append(initialText)
        pushStringAnnotation(tag = "URL", annotation = privacyPolicyText)
        withStyle(style = SpanStyle(color = Color.Blue)) {
            append(privacyPolicyText)
        }
        pop()
    }

    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(start = offset, end = offset).firstOrNull()?.let { annotation ->
                if (annotation.item == privacyPolicyText) {
                    Log.d("ClickableText", "Privacy Policy clicked")

                }
            }
        }
    )
}
@Composable
fun ButtonComponent(value: String) {
    val viewModel: LoginViewModel = viewModel()
    val context = LocalContext.current
    if(value == "Login"){
        Button(
            onClick = { viewModel.onLoginClick(context) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp),
            contentPadding = PaddingValues(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        brush = Brush.horizontalGradient(listOf(primaryLight, secondaryLight)),
                        shape = RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = value,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold// Adjust the text color as needed
                )
            }
        }
    }
    else{
        Button(
            onClick = { viewModel.onRegisterClick(context) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp),
            contentPadding = PaddingValues(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        brush = Brush.horizontalGradient(listOf(primaryLight, secondaryLight)),
                        shape = RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = value,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold// Adjust the text color as needed
                )
            }
        }
    }

}



