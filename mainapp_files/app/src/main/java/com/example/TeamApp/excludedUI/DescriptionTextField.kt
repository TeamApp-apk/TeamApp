import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DescriptionTextField(
    label: String,
    modifier: Modifier = Modifier,
    isEditable: Boolean,
    text: String = "", // Dodany parametr text do ustawienia opisu
    maxLength: Int = 250,
    maxLines: Int = 12
) {
    var description by remember { mutableStateOf(text) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        if (isEditable) {
            BasicTextField(
                value = description,
                onValueChange = { newText ->
                    val lines = newText.split("\n").size
                    if (newText.length <= maxLength && lines <= maxLines) {
                        description = newText
                    }
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp
                ),
                decorationBox = { innerTextField ->
                    Box(
                        Modifier
                            .background(Color.Transparent)
                            .padding(16.dp)
                    ) {
                        if (description.isEmpty()) {
                            Text(
                                text = label,
                                style = TextStyle(color = Color.Gray)
                            )
                        }
                        innerTextField()
                    }
                }
            )
            Text(
                text = "${description.length} / $maxLength",
                modifier = Modifier.padding(top = 4.dp),
                style = TextStyle(color = Color.Gray)
            )
        } else {
            BasicTextField(
                value = description,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(Color.Transparent),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp
                ),
                decorationBox = { innerTextField ->
                    Box(
                        Modifier
                            .background(Color.Transparent)
                            .padding(16.dp)
                    ) {
                        if (description.isEmpty()) {
                            Text(
                                text = "Brak opisu",
                                style = TextStyle(color = Color.Gray)
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}




