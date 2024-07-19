package com.example.TeamApp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.FirebaseApp


public class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        val auth = FirebaseAuth.getInstance()
        auth.useEmulator("localhost", 9099)
        setContent {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp, top = 16.dp)
                        .wrapContentWidth(Alignment.End)
                ) {
                    PLFLAG()
                    Spacer(modifier = Modifier.width(16.dp))
                    UKFLAG()
                }

                Spacer(modifier = Modifier.height(336.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    CustomButton(text = "SIGN UP", onClick = { /* TODO */ })
                }
                Spacer(modifier = Modifier.height(40.dp))

                BlueOnTheBottom()
            }
        }
    }
}

@Composable
fun PLFLAG() {
    Box(
        modifier = Modifier
            .border(width = 1.dp, color = Color.Black)
            .size(32.dp)
            .background(color = Color.White)
            .clickable { /* Handle flag click */ }
    ) {
        Image(
            painter = painterResource(id = R.drawable.plflag),
            contentDescription = "Polish Flag",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun UKFLAG() {
    Box(
        modifier = Modifier
            .border(width = 1.dp, color = Color.Black)
            .size(32.dp)
            .background(color = Color.White)
            .clickable { /* Handle flag click */ }
    ) {
        Image(
            painter = painterResource(id = R.drawable.ukflag),
            contentDescription = "UK Flag",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun CustomButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(60.dp)
            .width(300.dp)
            .padding(horizontal = 8.dp)
            .background(color = Color(0xFF22ACCA), shape = RoundedCornerShape(100.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                textAlign = TextAlign.Center,
                letterSpacing = 0.25.sp,
            ),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun BlueOnTheBottom() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(322.dp)
            .background(color = Color(0xFF22ACCA)),
        contentAlignment = Alignment.Center
    ) {
        // Content for the blue box at the bottom
    }
}
