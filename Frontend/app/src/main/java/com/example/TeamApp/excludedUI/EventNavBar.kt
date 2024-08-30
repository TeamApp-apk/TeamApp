import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.TeamApp.R

@Composable
fun BottomNavBar(navController: NavController) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = Color(0xFFF2F2F2))
            .navigationBarsPadding()
            .padding(horizontal = 40.dp, vertical = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
        ) {
            IconButton(
                onClick = {
                    if (!currentDestination.equals("search")) {
                        navController.navigate("search")
                    }
                },
                    modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "search",
                    tint = if (currentDestination == "search") Color(0xFF003366) else Color.Gray,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Ikona 'pluscircle' (zamieniona miejscami z 'search')
            IconButton(
                onClick = {  if (!currentDestination.equals("createEvent")) {
                    navController.navigate("createEvent")
                } },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.pluscircle),
                    contentDescription = "circle",
                    tint = if (currentDestination == "createEvent") Color(0xFF003366) else Color.Gray,
                    modifier = Modifier.fillMaxSize()
                )
            }

            IconButton(
                onClick = {  if (!currentDestination.equals("profile")) {
                    navController.navigate("profile")
                } },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "user",
                    tint = if (currentDestination == "profile") Color(0xFF003366) else Color.Gray,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
