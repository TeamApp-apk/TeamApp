package com.example.TeamApp.auth

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.primaryLight
import com.example.compose.secondaryLight
import com.example.TeamApp.R

@Composable
fun LoginScreen(){
    Surface( modifier = Modifier
        .fillMaxSize()
        .padding(27.dp)
        .background(Color.White))
    {
        Column(modifier = Modifier



            .fillMaxSize(),
            verticalArrangement = Arrangement.Center
            ) {
            TextComponentUpper1(value = "")
            TextComponentUpper2(value = "Welcome back")
            Spacer(modifier = Modifier.height(23.dp))
            MyTextField(labelValue = "E-mail", painterResource(id = R.drawable.emailicon) )
            Spacer(modifier = Modifier.height(12.dp))
            PasswordTextField(labelValue = "Password", painterResource(id = R.drawable.passwordicon) )
            Spacer(modifier = Modifier.height(31.dp))
            TextComponentUnderLined(value = "Forgot your password?")
            Spacer(modifier = Modifier.height(15.dp))
            ButtonComponent(value = "Login")
            Spacer(modifier = Modifier.height(21.dp))
            DividerTextComponent()
            Spacer(modifier = Modifier.height(21.dp))
            Row(modifier = Modifier
                .padding(21.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                FaceBookButton()
                Spacer(modifier = Modifier.width(45.dp))
                GoogleButton()
            }
            Spacer(modifier = Modifier.height(21.dp))
            ClickableRegisterTextComponent(modifier = Modifier.align(Alignment.CenterHorizontally))



        }




    }
    }

@Preview
@Composable
fun LoginScreenPreview(){
    LoginScreen()

}
@Composable
fun TextComponentUnderLined(value: String){
    Text(text = value,
        modifier = Modifier
            .fillMaxWidth()

            .heightIn(min = 40.dp)
        ,style= TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal, fontStyle = FontStyle.Normal),
        color = Color.Gray,
        textAlign = TextAlign.Center,
        textDecoration = TextDecoration.Underline

    )

}
@Composable
fun ClickableRegisterTextComponent(modifier: Modifier = Modifier) {
    val viewModel: LoginViewModel = viewModel()
    val context = LocalContext.current
    val initialText = "Don't have an account yet?  "
    val loginText = "Register"
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
        modifier=modifier,
        onClick = {
            viewModel.getToLoginScreen(context)
            //viewModel.onLoginClick(context)
        }
    )
}
@Composable
fun FaceBookButton(){
    // Replace with your image resource ID
    val image: Painter = painterResource(id = R.drawable.facebooklogo)

    Box(
        modifier = Modifier
            .size(60.dp) // Size of the button
            .clip(RoundedCornerShape(14.dp)) // Rounded corners with 24 dp radius
            .border(
                width = 2.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(14.dp)
            ) // Border with color and shape
            .clickable { /* Handle click event */ } // Clickable functionality
            .background(Color.White)
    ) {
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}
@Composable
fun GoogleButton(){

    val image: Painter = painterResource(id = R.drawable.googlelogo)

    Box(
        modifier = Modifier
            .size(60.dp) // Size of the button
            .clip(RoundedCornerShape(14.dp)) // Rounded corners with 24 dp radius
            .border(
                width = 2.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(14.dp)
            ) // Border with color and shape
            .clickable { /* Handle click event */ } // Clickable functionality
            .background(Color.White)
    ) {
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}






