package com.schibsted.nde.feature.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader

@Composable
fun MealImage(thumb: String, modifier: Modifier = Modifier) {
    Box(
        modifier
            .clip(RoundedCornerShape(4.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
            .padding(4.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = thumb,
                imageLoader = LocalContext.current.imageLoader
            ),
            contentDescription = "thumb",
            modifier = Modifier.fillMaxSize(),
        )
    }
}
