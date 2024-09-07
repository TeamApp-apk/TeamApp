package com.example.TeamApp.excludedUI

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.TeamApp.R

@Composable
fun UserProfileButton(iconId: Int, mainText: String, bottomText: String, navController: NavController, route: String ){
    Box(
        modifier = Modifier
            .background(color = Color(0xFFF2F2F2))
            .clickable { navController.navigate(route) }
    ){
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id = iconId),
                contentDescription = "Activity Icon",
                modifier = Modifier.padding(end = 12.dp).height(28.dp).width(28.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(end = 8.dp)
                ){
                    Text(
                        text = mainText,
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            fontWeight = FontWeight(200),
                            color = Variables.Black,
                            textAlign = TextAlign.Center,
                        )
                    )
                    Image(
                        painter = painterResource(id = R.drawable.arrowdownicon),
                        contentDescription = "Activity Icon",
                        modifier = Modifier.height(32.dp).width(32.dp),
                    )
                }
                Text(
                    text = bottomText,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                        fontWeight = FontWeight(500),
                        color = Color(0xFF979797),
                        textAlign = TextAlign.Center,
                    )
                )
            }
        }
    }
}

//@Preview
//@Composable
//fun previewUserProfileButton(){
//    UserProfileButton(iconId = R.drawable.mapicon, mainText = "Twoje wydarzenia", bottomText = "Zarządzaj swoimi wydarzeniami")
//}