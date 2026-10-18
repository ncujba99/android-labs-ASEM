package com.example.buildthehouse

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buildthehouse.ui.theme.Background
import com.example.buildthehouse.ui.theme.Door
import com.example.buildthehouse.ui.theme.Ground
import com.example.buildthehouse.ui.theme.Outline
import com.example.buildthehouse.ui.theme.Roof
import com.example.buildthehouse.ui.theme.Window

val TriangleShape = GenericShape { size, _ ->
    moveTo(size.width / 2f, 0f)
    lineTo(size.width, size.height)
    lineTo(0f, size.height)
    close()
}

// Layout of the 320 x 300 dp stage. Every part is anchored to BottomCenter and lifted with
// a negative y offset, so parts drawn later in code sit on top of earlier ones.
private val GroundHeight = 4.dp
private val WallWidth = 220.dp
private val WallHeight = 170.dp
private val RoofWidth = 300.dp
private val RoofHeight = 110.dp
private val WindowSize = 40.dp
private val WindowTopInset = 30.dp
private val WindowCenterOffset = 60.dp
private val DoorWidth = 44.dp
private val DoorHeight = 100.dp

/** Stateless drawing of the house: shows one element for every checked step. */
@Composable
fun HouseStageView(steps: List<BuildStep>, modifier: Modifier = Modifier) {
    val built = steps.filter { it.isDone }.map { it.part }.toSet()
    val windowLift = -(GroundHeight + WallHeight - WindowTopInset - WindowSize)

    Box(modifier.size(width = 320.dp, height = 300.dp)) {
        if (HousePart.FOUNDATION in built) {
            Box(
                Modifier
                    .align(Alignment.BottomCenter)
                    .size(width = 320.dp, height = GroundHeight)
                    .background(Ground)
            )
        }
        if (HousePart.WALLS in built) {
            Box(
                Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = -GroundHeight)
                    .size(width = WallWidth, height = WallHeight)
                    .background(Color.White)
                    .border(3.dp, Outline)
            )
        }
        if (HousePart.ROOF in built) {
            // Overlaps the top edge of the walls by 2 dp, as in the reference model.
            Box(
                Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = -(GroundHeight + WallHeight - 2.dp))
                    .size(width = RoofWidth, height = RoofHeight)
                    .background(Roof, TriangleShape)
            )
        }
        if (HousePart.LEFT_WINDOW in built) {
            Box(
                Modifier
                    .align(Alignment.BottomCenter)
                    .offset(x = -WindowCenterOffset, y = windowLift)
                    .size(WindowSize)
                    .background(Window)
            )
        }
        if (HousePart.RIGHT_WINDOW in built) {
            Box(
                Modifier
                    .align(Alignment.BottomCenter)
                    .offset(x = WindowCenterOffset, y = windowLift)
                    .size(WindowSize)
                    .background(Window)
            )
        }
        if (HousePart.DOOR in built) {
            Box(
                Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = -GroundHeight)
                    .size(width = DoorWidth, height = DoorHeight)
                    .background(Door)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF4F3EF)
@Composable
private fun HouseStageViewPreview() {
    HouseStageView(steps = BuildSteps.withDoneCount(BuildSteps.initial.size))
}

@Preview(showBackground = true, backgroundColor = 0xFFF4F3EF)
@Composable
private fun HouseStageViewHalfPreview() {
    HouseStageView(steps = BuildSteps.withDoneCount(3), modifier = Modifier.background(Background))
}
