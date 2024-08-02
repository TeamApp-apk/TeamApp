package com.example.TeamApp.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val context = LocalContext.current
    val email by viewModel.email.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val loginSuccess by viewModel.loginSuccess.observeAsState()
    val registerSuccess by viewModel.registerSuccess.observeAsState()
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    // Manage Snackbar visibility
    LaunchedEffect(loginSuccess, registerSuccess) {
        if (loginSuccess != null || registerSuccess != null) {
            showSnackbar = true
            delay(2000) // Delay for 2 seconds
            showSnackbar = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "TeamApp",
            style = TextStyle(fontSize = 24.sp),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        BasicTextField(
            value = email,
            onValueChange = { viewModel.onEmailChange(it) },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .background(Color.White, shape = MaterialTheme.shapes.small)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    if (email.isEmpty()) {
                        Text("Email", style = TextStyle(color = Color.Gray))
                    }
                    innerTextField()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        BasicTextField(
            value = password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .background(Color.White, shape = MaterialTheme.shapes.small)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    if (password.isEmpty()) {
                        Text("Password", style = TextStyle(color = Color.Gray))
                    }
                    innerTextField()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Button(
            onClick = { viewModel.onLoginClick(context) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp)
        ) {
            Text(text = "Log In")
        }
    }

    if (showSnackbar) {
        CustomSnackbar(
            success = loginSuccess ?: registerSuccess ?: false,
            onDismiss = { showSnackbar = false },
            isLogin = loginSuccess != null
        )
    }
}

//@Composable
//fun CustomSnackbar(success: Boolean, onDismiss: () -> Unit, isLogin: Boolean) {
//    Snackbar(
//        modifier = Modifier
//            .padding(80.dp)
//            .wrapContentSize(Alignment.Center),
//        shape = RoundedCornerShape(60.dp),
//        containerColor = if (success) Color(0xFF4CAF50) else Color(0xFFF44336),
//        contentColor = Color.White,
//        action = {
//        }
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth(),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                text = if (success) {
//                    if (isLogin) "Login Successful" else "Registration Successful"
//                } else {
//                    if (isLogin) "Login Failed" else "Registration Failed"
//                },
//                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
//            )
//        }
//    }
//}



