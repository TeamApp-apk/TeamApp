import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.TeamApp.R
import com.example.TeamApp.chat.Message
import com.example.TeamApp.chat.MessageItem
import com.example.TeamApp.chat.sendMessage
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(eventId: String, currentUserId: String) {
    val db = FirebaseFirestore.getInstance()
    val messages = remember { mutableStateListOf<Message>() }
    var messageText by remember { mutableStateOf("") }

    // Pobieranie wiadomości w czasie rzeczywistym
    LaunchedEffect(eventId) {
        val messagesRef = db.collection("events").document(eventId).collection("messages")
        messagesRef.orderBy("timestamp").addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("Chat", "Listen failed.", error)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                val newMessages = snapshot.toObjects(Message::class.java)
                messages.clear()
                messages.addAll(newMessages)
            } else {
                Log.d("Chat", "No messages found.")
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Wyświetlanie listy wiadomości
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(messages) { message ->
                MessageItem(message, currentUserId)
            }
        }

        // Pole do wysyłania wiadomości
        Row(modifier = Modifier.padding(8.dp)) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text("Napisz wiadomość...", fontFamily = FontFamily(Font(R.font.proximanovaregular))) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(

                    containerColor = Color.Transparent // Kolor tła
                )
            )
//            Button(
//                onClick = {
//                    sendMessage(eventId, currentUserId, messageText)
//                    messageText = "" // Resetowanie pola tekstowego po wysłaniu wiadomości
//                },
//                modifier = Modifier.padding(start = 8.dp)
//            ) {
//                Text("Wyślij")
//            }
            IconButton(onClick = { sendMessage(eventId, currentUserId, messageText)
            messageText=""}, modifier = Modifier.size(48.dp).padding(start = 8.dp)
            ) {
                Icon(painter = painterResource(id = R.drawable.sendmessage), contentDescription ="send message", modifier =
                Modifier.size(48.dp))

            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewChatScreen() {
    // Zamień `eventId` i `currentUserId` na wartości testowe
    val testEventId = "3175bf2e-0fbd-4d3e-b63e-a7797c173b8a"
    val testUserId = "testUser123"

    // Wywołanie funkcji `ChatScreen` w `Preview`
    ChatScreen(eventId = testEventId, currentUserId = testUserId)
}