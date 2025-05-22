package com.yourpackage.composables // Or your preferred package

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class) // For FlowRow
@Composable
fun OptionSelectorComponent(
    modifier: Modifier = Modifier,
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    selectedButtonContainerColor: Color = Color(0xff4fc3f7), // Specific blue from common UI patterns
    selectedButtonContentColor: Color = Color.White,
    unselectedButtonContainerColor: Color = Color.White,
    unselectedButtonContentColor: Color = Color.Black,
    unselectedButtonBorderColor: Color = Color(0xFFDCDCDC) // Light grey, similar to image
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.Black,

        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp), // Spacing between buttons
            verticalArrangement = Arrangement.spacedBy(8.dp)   // Spacing if buttons wrap to next line
        ) {
            options.forEach { optionText ->
                val isSelected = optionText == selectedOption
                Button(
                    onClick = { onOptionSelected(optionText) },
                    shape = RoundedCornerShape(8.dp), // Rounded corners
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) selectedButtonContainerColor else unselectedButtonContainerColor,
                        contentColor = if (isSelected) selectedButtonContentColor else unselectedButtonContentColor
                    ),
                    border = if (!isSelected) BorderStroke(1.dp, unselectedButtonBorderColor) else null,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp), // Ample padding
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp) // Flat buttons
                ) {
                    Text(
                        text = optionText,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}