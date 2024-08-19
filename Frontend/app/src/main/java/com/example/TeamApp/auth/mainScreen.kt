package com.example.TeamApp.auth
import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun MainScreen(){
val currentEventsCounter = 0
    val gradientColors= listOf(
        Color(0xFFE8E8E8)
        ,Color(0xFF007BFF)
    )
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = gradientColors))
            .padding(horizontal = 28.dp)
            .padding(vertical = 50.dp)){

            Row(modifier = Modifier
                .width(340.dp)
                .height(30.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CurrentCity(value = "Kraków")

            }
            if (currentEventsCounter==0){
                Box(modifier =Modifier.fillMaxSize(),  // Pełny rozmiar ekranu
                    contentAlignment = Alignment.Center ){
                NoCurrentActivitiesBar()
                    }
            }
            else{

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
            fontFamily = FontFamily(Font(R.font.robotoitalic)),
            fontWeight = FontWeight(900),
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
        .background(color = Color(0xFFD9D9D9), shape = RoundedCornerShape(size = 16.dp))){
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
fun BarOnTheBottom(){
    Row(horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,modifier = Modifier
            .width(328.dp)
            .height(42.dp)
            .background(color = Color(0xFFF2F2F2), shape = RoundedCornerShape(size = 73.dp))
            .padding(start = 42.dp, top = 9.dp, end = 42.dp, bottom = 9.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(86.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .width(244.dp)
                .height(24.dp)
        ) {
            IconButton(onClick = { /*TODO*/ }, modifier = Modifier
                .padding(1.dp)
                .width(24.dp)
                .height(24.dp)) {
                Icon(painterResource(id =R.drawable.pluscircle )  , contentDescription ="circle",Modifier.fillMaxSize() )

                
            }
            IconButton(onClick = { /*TODO*/ }, modifier = Modifier
                .padding(1.dp)
                .width(24.dp)
                .height(24.dp)) {
                Icon(painterResource(id =R.drawable.search )  , contentDescription ="search",Modifier.fillMaxSize() )


            }
            IconButton(onClick = { /*TODO*/ }, modifier = Modifier
                .padding(1.dp)
                .width(24.dp)
                .height(24.dp)) {
                Icon(painterResource(id =R.drawable.user )  , contentDescription ="user",Modifier.fillMaxSize() )


            }


        }

    }
}
