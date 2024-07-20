package com.example.TeamApp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val context = LocalContext.current
    val username by viewModel.username.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val loginSuccess by viewModel.loginSuccess.observeAsState()
    val registerSuccess by viewModel.registerSuccess.observeAsState()

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
            value = username,
            onValueChange = { viewModel.onUsernameChanged(it) },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .background(Color.White, shape = MaterialTheme.shapes.small)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    if (username.isEmpty()) {
                        Text("Username", style = TextStyle(color = Color.Gray))
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
            onClick = { viewModel.onLoginClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp)
        ) {
            Text(text = "Log In")
        }
        Button(
            onClick = { viewModel.onRegisterClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp)
        ) {
            Text(text = "Register")
        }
    }

    loginSuccess?.let { success ->
        CustomSnackbar(success = success, onDismiss = {},true)
    }
    registerSuccess?.let { success ->
        CustomSnackbar(success = success, onDismiss = {},false)
    }

}
@Composable
fun CustomSnackbar(success: Boolean, onDismiss: () -> Unit, isLogin: Boolean) {
    Snackbar(
        action = {
            TextButton(onClick = onDismiss) {
                Text(
                    "OK",
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        },
        modifier = Modifier.padding(12.dp),
        shape = RoundedCornerShape(12.dp),
        containerColor = if (success) Color(0xFF4CAF50) else Color(0xFFF44336),
        contentColor = Color.White
    ) {
        Text(
            text = if (success) {
                if (isLogin) "Login Successful" else "Registration Successful"
            } else {
                if (isLogin) "Login Failed" else "Registration Failed"
            },
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
        )
    }
}

