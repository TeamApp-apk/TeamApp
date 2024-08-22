import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun DescriptionTextField(
    label: String,            // Etykieta pola tekstowego
    modifier: Modifier = Modifier,
    isEditable: Boolean,      // Czy pole jest edytowalne
    maxLength: Int = 250      // Maksymalna liczba znaków
) {
    // Stan tekstu
    var text by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        if (isEditable) {
            // TextField do edytowalnego tekstu
            TextField(
                value = text,
                onValueChange = { newText ->
                    if (newText.text.length <= maxLength) {
                        text = newText
                    }
                },
                label = { Text(label) },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                maxLines = 10
            )

            // Informacja o liczbie znaków
            Text(
                text = "${text.text.length} / $maxLength",
                modifier = Modifier.padding(top = 4.dp)
            )
        } else {
            // BasicTextField dla tekstu tylko do odczytu bez ramki
            BasicTextField(
                value = text,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) { innerTextField ->
                // Wyświetlamy sam tekst bez obramowania i bez trybu edycji
                Text(
                    text = if (text.text.isEmpty()) "Brak opisu" else text.text,
                    style = TextStyle(

                    )
                )
            }
        }
    }
}

