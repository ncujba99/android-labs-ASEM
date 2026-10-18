package com.example.buildthehouse

/** The visible parts of the house, one per construction step. */
enum class HousePart {
    FOUNDATION,
    WALLS,
    ROOF,
    LEFT_WINDOW,
    RIGHT_WINDOW,
    DOOR,
}

data class BuildStep(
    val id: Int,
    val title: String,
    val part: HousePart,
    val isDone: Boolean = false,
)

object BuildSteps {
    val initial = listOf(
        BuildStep(id = 1, title = "Turnarea fundației", part = HousePart.FOUNDATION),
        BuildStep(id = 2, title = "Ridicarea pereților", part = HousePart.WALLS),
        BuildStep(id = 3, title = "Montarea acoperișului", part = HousePart.ROOF),
        BuildStep(id = 4, title = "Instalarea ferestrei din stânga", part = HousePart.LEFT_WINDOW),
        BuildStep(id = 5, title = "Instalarea ferestrei din dreapta", part = HousePart.RIGHT_WINDOW),
        BuildStep(id = 6, title = "Montarea ușii", part = HousePart.DOOR),
    )

    /** Rebuilds the step list with the first [doneCount] steps checked. */
    fun withDoneCount(doneCount: Int): List<BuildStep> =
        initial.mapIndexed { index, step -> step.copy(isDone = index < doneCount) }
}

fun List<BuildStep>.doneCount(): Int = count { it.isDone }

/**
 * Order rule: with [done] steps checked, only step [index] == done can be checked
 * and only step [index] == done - 1 can be unchecked. Every other step is disabled.
 */
fun isStepEnabled(index: Int, done: Int): Boolean = index == done || index == done - 1
