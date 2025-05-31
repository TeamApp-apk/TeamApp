import androidx.compose.foundation.background
// import androidx.compose.foundation.clickable // Not needed for ClickableText's primary function
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText // <-- IMPORT THIS
import androidx.compose.material3.LocalTextStyle // <-- IMPORT THIS for default style
import androidx.compose.material3.Text // Keep for simple text parts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

const val INITIAL_WORDS_LIMIT = 33
const val INCREMENT_WORDS_COUNT = 120

@Composable
fun DescriptionTextField(
    label: String,
    modifier: Modifier = Modifier,
    isEditable: Boolean,
    text: String,
    maxLength: Int = 250,
    maxLines: Int = 12
) {
    var editableDescription by remember { mutableStateOf(if (isEditable) text else "") }

    val originalWords = remember(text) { text.split(' ').filter { it.isNotBlank() } }
    var displayedWordCount by remember { mutableStateOf(minOf(INITIAL_WORDS_LIMIT, originalWords.size)) }
    var isFullyExpanded by remember { mutableStateOf(INITIAL_WORDS_LIMIT >= originalWords.size && originalWords.isNotEmpty()) }


    LaunchedEffect(text, isEditable) {
        if (!isEditable) {
            val newOriginalWords = text.split(' ').filter { it.isNotBlank() }
            val needsReset = originalWords.joinToString(" ") != newOriginalWords.joinToString(" ") ||
                    (displayedWordCount > INITIAL_WORDS_LIMIT && newOriginalWords.size <= INITIAL_WORDS_LIMIT)

            if (needsReset) {
                displayedWordCount = minOf(INITIAL_WORDS_LIMIT, newOriginalWords.size)
                isFullyExpanded = (displayedWordCount >= newOriginalWords.size && newOriginalWords.isNotEmpty())
            }
        } else {
            editableDescription = text
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        if (isEditable) {
            // ... (Editable BasicTextField part remains the same)
            BasicTextField(
                value = editableDescription,
                onValueChange = { newText ->
                    val lines = newText.split("\n").size
                    if (newText.length <= maxLength && lines <= maxLines) {
                        editableDescription = newText
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(vertical = 16.dp),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp
                ),
                decorationBox = { innerTextField ->
                    Box(
                        Modifier
                            .padding(horizontal = 16.dp)
                    ) {
                        if (editableDescription.isEmpty()) {
                            Text(
                                text = label,
                                style = TextStyle(color = Color.Gray, fontSize = 16.sp)
                            )
                        }
                        innerTextField()
                    }
                }
            )
            Text(
                text = "${editableDescription.length} / $maxLength",
                modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp),
                style = TextStyle(color = Color.Gray)
            )
        } else {
            val currentWordsToDisplay = originalWords.take(displayedWordCount)
            val showShowMore = displayedWordCount < originalWords.size
            val showCollapse = isFullyExpanded && originalWords.size > INITIAL_WORDS_LIMIT

            val annotatedText = buildAnnotatedString {
                // Apply base style for the main text part
                withStyle(style = SpanStyle(color = Color.Black, fontSize = 16.sp)) {
                    append(currentWordsToDisplay.joinToString(" "))
                }

                if (showShowMore) {
                    withStyle(style = SpanStyle(color = Color.Black, fontSize = 16.sp)) { // Ensure ellipsis has base style
                        append("... ")
                    }
                    pushStringAnnotation(tag = "WIĘCEJ", annotation = "więcej")
                    withStyle(
                        style = SpanStyle(
                            color = Color.Gray, // This is your link color
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    ) {
                        append("Więcej")
                    }
                    pop()
                } else if (showCollapse) {
                    withStyle(style = SpanStyle(color = Color.Black, fontSize = 16.sp)) { // Ensure space has base style
                        append(" ")
                    }
                    pushStringAnnotation(tag = "ZWIŃ", annotation = "zwiń")
                    withStyle(
                        style = SpanStyle(
                            color = Color.Gray, // This is your link color
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    ) {
                        append("Zwiń")
                    }
                    pop()
                }
            }

            if (text.isNotBlank()) {
                ClickableText(
                    text = annotatedText,
                    style = LocalTextStyle.current.merge( // Use LocalTextStyle and merge for defaults
                        TextStyle(
                            // You can override specific parts of the default style here if needed
                            // but the main styling is now within buildAnnotatedString
                        )
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    onClick = { offset ->
                        if (showShowMore) {
                            annotatedText.getStringAnnotations(tag = "WIĘCEJ", start = offset, end = offset)
                                .firstOrNull()?.let {
                                    val nextDisplayCount = minOf(displayedWordCount + INCREMENT_WORDS_COUNT, originalWords.size)
                                    if (nextDisplayCount >= originalWords.size) {
                                        isFullyExpanded = true
                                    }
                                    displayedWordCount = nextDisplayCount
                                    return@ClickableText
                                }
                        }
                        if (showCollapse) {
                            annotatedText.getStringAnnotations(tag = "ZWIŃ", start = offset, end = offset)
                                .firstOrNull()?.let {
                                    displayedWordCount = minOf(INITIAL_WORDS_LIMIT, originalWords.size)
                                    isFullyExpanded = (displayedWordCount >= originalWords.size && originalWords.isNotEmpty())
                                }
                        }
                    }
                )
            } else {
                Text(
                    text = "Brak opisu",
                    style = TextStyle(color = Color.Gray, fontSize = 16.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}