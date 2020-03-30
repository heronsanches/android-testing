package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Test

class StatisticsUtilsTest {
    private val dummyTask = Task("title", "desc", isCompleted = false)

    @Test
    fun getActiveAndCompletedStats_noCompleted_returnsHundredZero() {
        val tasks = listOf<Task>(dummyTask)
        val result = getActiveAndCompletedStats(tasks)
        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(100f))
    }

    /* If there is one completed task and no active tasks, the activeTasks percentage
    should be 0f, the completed tasks percentage should be 100f. * */
    @Test
    fun getActiveAndCompletedStats_onlyOneTask_completed() {
        val tasks = listOf<Task>(dummyTask.copy(isCompleted = true))
        val result = getActiveAndCompletedStats(tasks)
        assertEquals(100f, result.completedTasksPercent)
        assertEquals(0f, result.activeTasksPercent)
    }

    /* If there are two completed tests and three active test, the completed percentage
        should be 40f and the active percentage should be 60f. * */
    @Test
    fun getActiveAndCompletedStats_twoCompletedAndThreeActive() {
        val tasks = listOf<Task>(
                dummyTask.copy(isCompleted = true), dummyTask.copy(isCompleted = true),
                dummyTask.copy(isCompleted = false), dummyTask.copy(isCompleted = false),
                dummyTask.copy(isCompleted = false)
        )
        val result = getActiveAndCompletedStats(tasks)
        assertEquals(40f, result.completedTasksPercent)
        assertEquals(60f, result.activeTasksPercent)
    }

    @Test
    fun getActiveAndCompletedStats_emptyList_returns0() {
        val tasks = listOf<Task>()
        val result = getActiveAndCompletedStats(tasks)
        assertEquals(0f, result.completedTasksPercent)
        assertEquals(0f, result.activeTasksPercent)
    }

    @Test
    fun getActiveAndCompletedStats_nullList_returns0() {
        val tasks = null
        val result = getActiveAndCompletedStats(tasks)
        assertEquals(0f, result.completedTasksPercent)
        assertEquals(0f, result.activeTasksPercent)
    }
}