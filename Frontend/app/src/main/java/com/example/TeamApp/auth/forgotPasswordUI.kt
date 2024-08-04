package com.example.TeamApp.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.TeamApp.R
import com.example.compose.primaryLight
import com.example.compose.secondaryLight
import kotlinx.coroutines.delay
@Composable
fun ForgotPasswordUI(){
val gradientColors= listOf(
    Color(0xFFE8E8E8)
    ,Color(0xFF007BFF)
)
    Surface(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.linearGradient(colors = gradientColors))
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(50.dp),
                     // Center horizontally
                ) {
                UpperTextField(value = "Reset Password")
                Spacer(modifier = Modifier.height(14.dp))
                LowerTextField(value =  "Please enter your email address to request a password reset")
                Spacer(modifier = Modifier.height(20.dp))
                EmailButtonField(labelValue ="Your Email" , painterResource (id=R.drawable.message ),"Your Email")
                Spacer(modifier = Modifier.height(54.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center) // Center horizontally
                ) {
                    ButtonWithSend()
                }
            }
        }
    }

}
@Composable
fun UpperTextField(value: String){
    Text(text = value, modifier = Modifier
        .width(186.dp)
        .height(28.dp), style = TextStyle(fontSize = 24.sp, fontFamily =
    FontFamily(Font(R.font.robotobold)), fontWeight = FontWeight(500), color = Color.Black
    )
    )

}
@Composable
fun LowerTextField(value: String){
    Text(text = value, modifier = Modifier
        .width(244.dp)
        .height(50.dp), style = TextStyle(fontSize = 15.sp, lineHeight = 25.sp, fontFamily =
    FontFamily(Font(R.font.robotoregular)), fontWeight = FontWeight(400), color = Color.Black
    )
    )

}
@Preview
@Composable

fun ForgotPasswordUIPreview(){
    ForgotPasswordUI()

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailButtonField(
    labelValue: String,
    painterResource: Painter,
    emailPlaceholder: String // New parameter to pass the placeholder text
) {
    val textValue = remember { mutableStateOf("") }
    val viewModel: LoginViewModel = viewModel()
    val email by viewModel.email.observeAsState("")

    Column(
        Modifier
            .fillMaxWidth()
            .padding(start = 0.dp),
        ) {

        TextField(
            modifier = Modifier

                .border(
                    width = 1.dp,
                    color = Color(0xFFE4DFDF),
                    shape = RoundedCornerShape(size = 12.dp)
                )

                .width(350.dp)
                .height(56.dp)
                .background(color = Color.White, shape = RoundedCornerShape(size = 12.dp)),
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
            leadingIcon = {
                Icon(painter = painterResource, contentDescription = "", modifier = Modifier.size(35.dp))
            },
            shape = RoundedCornerShape(size = 12.dp),
            colors = TextFieldDefaults.textFieldColors(
                // Hide default indicators to use custom styling
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = Color.White

            )
        )



    }

}

@Composable
fun ButtonWithSend() {

    Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .width(271.dp)
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
                    text = "SEND",

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