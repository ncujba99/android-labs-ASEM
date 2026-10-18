package com.example.buildthehouse

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BuildModelTest {

    @Test
    fun initialList_hasSixUncheckedStepsInOrder() {
        val steps = BuildSteps.initial
        assertEquals(6, steps.size)
        assertEquals(0, steps.doneCount())
        assertEquals(HousePart.entries, steps.map { it.part })
    }

    @Test
    fun noStepsDone_onlyFirstStepEnabled() {
        val enabled = (0 until 6).map { isStepEnabled(it, done = 0) }
        assertEquals(listOf(true, false, false, false, false, false), enabled)
    }

    @Test
    fun threeStepsDone_lastCheckedAndNextUncheckedEnabled() {
        val enabled = (0 until 6).map { isStepEnabled(it, done = 3) }
        assertEquals(listOf(false, false, true, true, false, false), enabled)
    }

    @Test
    fun allStepsDone_onlyLastStepEnabled() {
        assertTrue(isStepEnabled(5, done = 6))
        assertFalse(isStepEnabled(4, done = 6))
    }

    @Test
    fun withDoneCount_checksFirstNSteps() {
        val steps = BuildSteps.withDoneCount(4)
        assertEquals(listOf(true, true, true, true, false, false), steps.map { it.isDone })
    }
}
