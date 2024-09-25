package com.example.TeamApp.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import com.example.TeamApp.R
import com.example.TeamApp.data.User
import androidx.compose.foundation.layout.*
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Paint.Align
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import com.example.TeamApp.auth.user
import java.util.Calendar


fun getIconResourceId(context: Context, iconName: String): Int {
    val resourceId = context.resources.getIdentifier(iconName, "drawable", context.packageName)
    return resourceId
}


@Composable
fun EditProfile(user: User){
    val gradientColors = listOf(
        Color(0xFFE8E8E8),
        Color(0xFF007BFF),

    )
    var newNameValue= remember {
        mutableStateOf(user.name)
    }
    val context = LocalContext.current
    var newBirthdayValue = remember { mutableStateOf(user.birthDay) }



    ConstraintLayout(modifier= Modifier
        .fillMaxSize()
        .background(Brush.linearGradient(colors = gradientColors))
        ){
        val (WhiteBox,TopRow,AvatarImage,NameTextField,BirthdayTextField,GenderRow,AcceptButton) = createRefs()
        val WhiteBoxStart = createGuidelineFromStart(0.05f)
        val WhiteBoxEnd = createGuidelineFromStart(0.95f)
        val WhiteBoxTop = createGuidelineFromTop(0.08f)
        val WhiteBoxBottom = createGuidelineFromBottom(0.25f)

        Column(
            modifier = Modifier
                .constrainAs(WhiteBox) {
                    start.linkTo(WhiteBoxStart)
                    end.linkTo(WhiteBoxEnd)
                    top.linkTo(WhiteBoxTop) // Aligning to the top of the parent within the padding
                    bottom.linkTo(
                        WhiteBoxBottom,


                    )
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints// Adjust the margin to control how far from the bottom it should go
                }
                .width(360.dp)
                .height(570.dp)
                .background(color = Color.White, shape = RoundedCornerShape(size = 16.dp))
                .padding(10.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConstraintLayout {
                Row(
                    modifier = Modifier
                        .width(359.dp)
                        .constrainAs(TopRow) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                        }
                        .height(45.dp)
                        .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    // Back arrow, taking minimal space
                    Image(
                        modifier = Modifier
                            .padding(1.dp)
                            .width(24.dp)
                            .height(24.dp),
                        painter = painterResource(id = R.drawable.arrow_icon),
                        contentDescription = "back"
                    )

                    // Spacer to push the text to the right
                    Spacer(modifier = Modifier.weight(1f))

                    // Title text, aligned to the right
                    Text(
                        text = "Edytuj profil",
                        style = TextStyle(
                            fontSize = 20.sp,
                            lineHeight = 25.sp,
                            fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                            fontWeight = FontWeight(900),
                            color = Color(0xFF003366),
                            textAlign = TextAlign.Right,
                        ),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                EditAvatar(modifier=Modifier.constrainAs(AvatarImage){
                    top.linkTo(TopRow.bottom, margin = 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)},user = user)

                    OutlinedTextField(value = newNameValue.value.toString(), onValueChange ={newNameValue.value=it},
                        placeholder = {Text(text = user.name.toString() )}, singleLine = true, modifier = Modifier

                            .width(317.dp)
                            .height(56.dp)
                            .constrainAs(NameTextField) {
                                top.linkTo(AvatarImage.bottom, margin = 12.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(size = 12.dp)
                            ),shape = RoundedCornerShape(size = 12.dp), keyboardOptions = KeyboardOptions.Default,
                        leadingIcon ={ Icon(painter = painterResource(id = R.drawable.user), contentDescription ="." , modifier =
                        Modifier.size(22.dp))})
                MyDatePicker(
                    modifier = Modifier
                        .constrainAs(BirthdayTextField) {
                            top.linkTo(NameTextField.bottom, margin = 12.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .width(317.dp)
                        .height(56.dp)
                        .fillMaxSize(),
                    initialDate = user.birthDay.toString(),
                    onDateChange = { newBirthdayValue ->
                        user.birthDay = newBirthdayValue
                    }
                )
                SelectedSex(user = user, modifier =Modifier.constrainAs(GenderRow){
                    top.linkTo(BirthdayTextField.bottom,margin=12.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                } )
                AcceptChangesButton(modifier = Modifier.constrainAs(AcceptButton){
                    top.linkTo(GenderRow.bottom, margin = 12.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })






            }
        }

    }
}
@Composable
@Preview(showBackground = false)
fun EditProfilePreview(){
    EditProfile(user = User(
        name = "Jan",
        email = "jan@example.com",
        birthDay = "01/01/1990",
        gender = "Male",
        avatar = "testavatar"
    )   )

}

@Composable
fun EditAvatar(modifier: Modifier = Modifier, user: User) {
    val context = LocalContext.current
    val iconID = getIconResourceId(context, "testavatar")

    Box(
        modifier = modifier
            .size(204.dp)
            .background(color = Color.White)
            .fillMaxSize(),
        contentAlignment = Alignment.Center // Centers the Row
    ) {
        Row(
            modifier = Modifier
                .width(180.dp)
                .height(180.dp)
                .background(color = Color.White)
                .padding(10.dp), // Consolidated padding
        ) {
            // Avatar Image
            Image(
                modifier = Modifier
                    .width(192.dp)
                    .height(192.dp)
                    .clickable { },
                painter = painterResource(id = iconID),
                contentDescription = "current avatar"
            )
        }

        // Pen Image, aligned to bottom right corner
        Image(
            painter = painterResource(id = R.drawable.pen),
            contentDescription = "pen icon",
            modifier = Modifier
                .size(32.dp) // Adjust the size of the pen icon
                .align(Alignment.BottomEnd) // Align it to bottom-right
                .padding(8.dp) // Add some padding from the edges
        )
    }
}
@Composable
fun MyDatePicker(
    modifier: Modifier,
    initialDate: String,
    onDateChange: (String) -> Unit
) {
    var selectedDate by remember { mutableStateOf(initialDate) }
    val context = LocalContext.current

    // Extract day, month, and year from the initial date
    val parts = initialDate.split("/")
    val calendar = Calendar.getInstance().apply {
        if (parts.size == 3) {
            set(Calendar.DAY_OF_MONTH, parts[0].toInt())
            set(Calendar.MONTH, parts[1].toInt() - 1) // Month is 0-based
            set(Calendar.YEAR, parts[2].toInt())
        }
    }

    // Create the DatePickerDialog
    val datePickerDialog = DatePickerDialog(
        context,
        R.style.CustomTimePickerTheme,
        { _, pickedYear, pickedMonth, pickedDayOfMonth ->
            selectedDate = String.format("%02d/%02d/%d", pickedDayOfMonth, pickedMonth + 1, pickedYear)
            onDateChange(selectedDate) // Notify parent about the new date
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Show the dialog when clicked
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { datePickerDialog.show() }
            .padding(16.dp), // Padding for the content
        contentAlignment = Alignment.CenterStart
    ) {
        Image(
            painter = painterResource(id = R.drawable.user),
            contentDescription = "placeholder",
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = if (selectedDate.isEmpty()) "Data" else selectedDate,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 25.sp,
                textAlign = TextAlign.Start,
                fontFamily = if (selectedDate.isEmpty()) FontFamily(Font(R.font.proximanovaregular)) else FontFamily(Font(R.font.proximanovaregular)),

                color = if (selectedDate.isEmpty()) Color.Black else Color.Black,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 40.dp) // Adding padding to avoid overlap with the image
        )
    }
}
@Composable
fun SelectedSex(user: User, modifier: Modifier) {
    var selectedSex by remember { mutableStateOf(user.gender) } // Store the selected gender

    Row(
        modifier = modifier
            .width(317.dp)
            .height(56.dp)
            .padding(start = 10.dp, end = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Male button
        Button(
            onClick = {
                selectedSex = "Male" // Update selected gender
                user.gender = selectedSex // Assuming User class has a mutable gender property
            },
            modifier = Modifier
                .weight(1f)
                .width(140.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedSex == "Male") Color(0xFF007BFF) else Color.LightGray
            ),
            shape = RoundedCornerShape(12.dp)

        ) {
            // Male Icon
            Icon(
                imageVector = Icons.Filled.Male, // Replace with your specific male icon if available
                contentDescription = "Male",
                tint = if (selectedSex == "Male") Color.White else Color.Black
            )
        }

        // Female button
        Button(
            onClick = {
                selectedSex = "Female" // Update selected gender
                user.gender = selectedSex // Assuming User class has a mutable gender property
            },
            modifier = Modifier
                .weight(1f)
                .width(140.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedSex == "Female") Color(0xFFFF94DF) else Color.LightGray
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            // Female Icon (you can use a different icon for female if available)
            Icon(
                imageVector = Icons.Filled.Female, // Replace with your specific female icon if available
                contentDescription = "Female",
                tint = if (selectedSex == "Female") Color.White else Color.Black
            )
        }
    }
}
@Composable
fun AcceptChangesButton(modifier: Modifier) {
    Row(
        modifier = modifier
            .width(180.dp)
            .height(56.dp)
            .clickable {
                // Handle click event here
            }
            .background(
                color = Color(0xFFF2F2F2), shape = RoundedCornerShape(size = 8.dp)
            ) .border(
                width = 0.5.dp,
                color = Color.Black, // Set your border color here
                shape = RoundedCornerShape(size = 8.dp) // Match the corner shape of the background
            ),
        verticalAlignment = Alignment.CenterVertically, // Center content vertically
        horizontalArrangement = Arrangement.Center // Center content horizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize() // Take up the full size of the Row
                .padding(8.dp) // Optional: padding around the text
        ) {
            Text(
                text = "Zapisz zmiany",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                    fontWeight = FontWeight(500),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.align(Alignment.Center) // Center text within the Box
            )
        }
    }
}




