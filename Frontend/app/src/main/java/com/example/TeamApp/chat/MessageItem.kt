package com.example.TeamApp.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MessageItem(message: Message, currentUserId: String) {
    val isCurrentUser = message.userId == currentUserId

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .background(
                    if (isCurrentUser) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        ) {
            Text(
                text = message.userId,
                style = MaterialTheme.typography.caption,
                color = if (isCurrentUser) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message.message,
                style = MaterialTheme.typography.body1,
                color = if (isCurrentUser) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message.timestamp?.toDate()?.toString() ?: "",
                style = MaterialTheme.typography.overline,
                color = if (isCurrentUser) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
            )
        }
    }
}