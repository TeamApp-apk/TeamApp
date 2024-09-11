package com.example.TeamApp.excludedUI

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.TeamApp.R
import com.example.TeamApp.auth.density
import com.example.TeamApp.event.CreateEventViewModel
import com.example.TeamApp.event.ViewModelProvider
import com.example.TeamApp.settings.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccountDialog(painterResource: Painter,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    val allowedCharsRegex = Regex("^[0-9a-zA-Z!@#\$%^&*()_+=\\-{}\\[\\]:\";'<>?,./]*\$")
    val viewModel: SettingsViewModel = com.example.TeamApp.settings.ViewModelProvider.SettingsViewModel

    // Create a FocusRequester to request focus on the TextField
    val focusRequester = remember { FocusRequester() }
    val password by viewModel.password.observeAsState("")

    // Get the keyboard controller
    val keyboardController = LocalSoftwareKeyboardController.current
    val hapticFeedback = LocalHapticFeedback.current

    val passwordVisible = remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val height = configuration.screenHeightDp.dp
    val width = configuration.screenWidthDp.dp
    var textValue by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current


    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Potwierdź hasło",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.proximanovabold)),
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(8.dp)
                        ) // Ensure the background is white
                        .padding(horizontal = 8.dp) // Add some padding inside the TextField
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height * 0.00625f * 8 * density)
                            .focusRequester(focusRequester),
                        label = { Text(
                            text = "hasło",
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.proximanovaregular)), // Apply custom font here
                                fontSize = 16.sp // Adjust font size if needed
                            )
                        ) },
                        value = password,
                        onValueChange = { newValue ->
                            // Add validation if needed
                            if (newValue.matches(allowedCharsRegex)) {
                                viewModel.onPasswordChanged(newValue)
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction =  ImeAction.Done,
                            keyboardType = KeyboardType.Password),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Gray, // Set to grey when focused
                            unfocusedIndicatorColor = Color.LightGray, // Set to light grey when not focused
                            containerColor = Color.White // Background color inside the TextField
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

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)

                ) {
                    Text(
                        text = "Anuluj",
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.proximanovabold)),
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple() // Efekt "fal" przy kliknięciu
                            ) {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                onDismiss()
                            }
                            .padding(8.dp)
                    )
                    Divider(
                        color = Color.Black,
                        thickness = 6.dp,
                        modifier = Modifier
                            .height(20.dp)
                            .width(1.dp)
                    )
                    Text(
                        text = "Gotowe",
                        fontSize = 22.sp,
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.proximanovabold)),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple() // Efekt "fal" przy kliknięciu
                            ) {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                onSave()
                            }
                            .padding(8.dp)
                    )
                }

            }
        }
    }
    // Automatically focus the TextField and show the keyboard when the dialog is opened
//    LaunchedEffect(Unit) {
//        focusRequester.requestFocus()
//        keyboardController?.show()
//    }
}