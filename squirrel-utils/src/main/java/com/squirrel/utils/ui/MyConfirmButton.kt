package com.squirrel.utils.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MyConfirmButton(text: String, enabled: Boolean = true, handleClick: () -> Unit) {
    Row(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.onTertiary)
//                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                .clickable(enabled = enabled) {
                    handleClick()
                },
            horizontalArrangement = Arrangement.Center
        ) {
            Box(Modifier.padding(10.dp)) {
                Text(
                    text = text,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }

}