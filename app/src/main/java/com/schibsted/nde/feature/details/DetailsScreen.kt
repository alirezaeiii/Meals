package com.schibsted.nde.feature.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import com.schibsted.nde.domain.Meal
import com.schibsted.nde.feature.common.MealImage

@OptIn(ExperimentalMotionApi::class)
@Composable
fun DetailsScreen(
    meal: Meal,
    navigateUp: () -> Unit
) {
    val scroll = rememberScrollState(0)
    val big = 350.dp
    val small = 64.dp
    val surfaceColor = MaterialTheme.colors.surface
    val onSurfaceColor = MaterialTheme.colors.onSurface
    val scene = MotionScene {
        val (title, image, icon) = createRefsFor("title", "image", "icon")

        val start1 = constraintSet {
            constrain(title) {
                bottom.linkTo(image.bottom)
                start.linkTo(image.start, 32.dp)
                customColor("color", Color.White)
            }
            constrain(image) {
                width = Dimension.matchParent
                height = Dimension.value(big)
                top.linkTo(parent.top)
                customColor("cover", Color(0x000000FF))
            }
            constrain(icon) {
                top.linkTo(image.top, 16.dp)
                start.linkTo(image.start, 16.dp)
                customColor("bg", Color.White)
                customColor("tint", Color.Black)
                width = Dimension.value(32.dp)
                height = Dimension.value(32.dp)
            }
        }
        val end1 = constraintSet {
            constrain(title) {
                bottom.linkTo(image.bottom)
                start.linkTo(icon.end)
                centerVerticallyTo(image)
                scaleX = 0.7f
                scaleY = 0.7f
                customColor("color", onSurfaceColor)
            }
            constrain(image) {
                width = Dimension.matchParent
                height = Dimension.value(small)
                top.linkTo(parent.top)
                customColor("cover", surfaceColor)
            }
            constrain(icon) {
                top.linkTo(image.top, 8.dp)
                start.linkTo(image.start, 16.dp)
                customColor("bg", Color.Transparent)
                customColor("tint", onSurfaceColor)
            }
        }
        transition(start1, end1, "default") {}
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(scroll)
    ) {
        Spacer(Modifier.height(big))
        repeat(4) {
            Text(
                text = meal.strInstructions,
                modifier = Modifier
                    .background(surfaceColor)
                    .padding(16.dp),
                color = onSurfaceColor
            )
        }
    }
    val gap = with(LocalDensity.current) { big.toPx() - small.toPx() }
    val progress = minOf(scroll.value / gap, 1f)

    MotionLayout(
        modifier = Modifier.fillMaxSize(),
        motionScene = scene,
        progress = progress
    ) {

        val coverColor = customColor("image", "cover")

        Box(
            modifier = Modifier
                .layoutId("image")
                .fillMaxSize()
        ) {
            MealImage(
                thumb = meal.strMealThumb,
                modifier = Modifier.matchParentSize()
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(coverColor)
            )
        }
        IconButton(
            onClick = navigateUp,
            modifier = Modifier
                .layoutId("icon")
                .background(color = customColor("icon", "bg"), shape = CircleShape)
        ) {
            Icon(
                Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "back",
                tint = customColor("icon", "tint")
            )
        }
        Text(
            modifier = Modifier
                .layoutId("title")
                .padding(vertical = 40.dp, horizontal = 12.dp),
            text = meal.strMeal,
            fontSize = 30.sp,
            color = customColor("title", "color")
        )
    }
}