package com.example.TeamApp.event
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.TeamApp.R
import com.example.TeamApp.data.Event

@Composable
fun MainScreen(){
    val activityList = remember { mutableStateListOf<Event>() }
val currentEventsCounter = activityList.size
    val gradientColors= listOf(
        Color(0xFFE8E8E8)
        ,Color(0xFF007BFF)
    )
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = gradientColors))
            .padding(horizontal = 20.dp)
            .padding(vertical = 50.dp)){

            Row(modifier = Modifier
                .width(340.dp)
                .height(30.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CurrentCity(value = "KRAKÓW")
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp) // Odstęp między elementami
            ) {
                if (currentEventsCounter == 0) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            NoCurrentActivitiesBar()
                        }
                    }
                } else {
                    item { Spacer(modifier = Modifier.height(20.dp)) }

                    // Tworzenie listy elementów
                    items(activityList) { activity ->
                        ActivityCard(

                            iconResId = activity.iconResId,
                            date = activity.date.toString(),
                            activityName = activity.activityName,
                            currentParticipants = activity.currentParticipants,
                            maxParticipants = activity.maxParticipants,
                            location = activity.location
                        ) {
                            // Akcja do wykonania przy kliknięciu na kartę
                        }
                    }
                }
            }

            Box(contentAlignment =Alignment.BottomCenter, modifier = Modifier.fillMaxSize() ){
                BarOnTheBottom()

            }

        }

    }

}
@Composable
@Preview(showBackground = false)

fun MainScreenPreview(){
    MainScreen()
}
@Composable
fun CurrentCity(value : String){
    Text(modifier = Modifier
        .width(105.dp)
        .height(30.dp),
        text = value,
        style = TextStyle(
            fontSize = 26.sp,
            fontFamily = FontFamily(Font(R.font.robotoblackitalic)),
            fontWeight = FontWeight.ExtraBold,
            fontStyle = FontStyle.Italic,
            color = Color(0xFF003366),

        )
    )

}
@Composable
fun NoCurrentActivitiesBar(){
    Box(contentAlignment = Alignment.Center,modifier = Modifier
        .width(193.dp)
        .height(40.dp)
        .background(color = Color(0xFFF2F2F2), shape = RoundedCornerShape(size = 16.dp))){
        Text(
            text = "Brak aktywnośći",
            style = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.robotoitalic)),
                fontWeight = FontWeight(900),
                fontStyle = FontStyle.Italic,
                color = Color.Black,

                textAlign = TextAlign.Center,

            )
        )


}
}
@Composable
fun BarOnTheBottom() {
    // Główny kontener dla paska
    Row(

        horizontalArrangement = Arrangement.Center, // Wyśrodkowanie ikonek poziomo
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width(328.dp)
            .height(42.dp)
            .background(color = Color(0xFFF2F2F2), shape = RoundedCornerShape(size = 73.dp))
            .padding(start = 42.dp, top = 9.dp, end = 42.dp, bottom = 9.dp)
    ) {
        // Kontener dla ikon
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, // Rozmieszczenie ikon równomiernie
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .width(244.dp) // Ustalona szerokość, aby dopasować ikony
                .height(24.dp)
        ) {
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .size(24.dp) // Ustalona wielkość ikony
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.pluscircle),
                    contentDescription = "circle",
                    modifier = Modifier.fillMaxSize()
                )
            }

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .size(24.dp) // Ustalona wielkość ikony
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "search",
                    modifier = Modifier.fillMaxSize()
                )
            }

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .size(24.dp) // Ustalona wielkość ikony
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "user",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
