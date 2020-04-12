package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.succeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@MediumTest
@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class TasksLocalDataSourceTest {
    @get:Rule
    private var instantTaskExecutorRule = InstantTaskExecutorRule()

    // parts under testing
    private lateinit var database: ToDoDatabase
    private lateinit var tasksLocalDataSource: TasksLocalDataSource

    @Before
    fun initilizeDatabaseAndDataSource() {
        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(), ToDoDatabase::class.java
        ).allowMainThreadQueries().build()
        tasksLocalDataSource = TasksLocalDataSource(database.taskDao(), Dispatchers.Main)
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    // runBlocking is used here because of https://github.com/Kotlin/kotlinx.coroutines/issues/1204
// TODO: Replace with runBlockingTest once issue is resolved
    @Test
    fun saveTask_retrievesTask() = runBlocking {
        // GIVEN - A new task saved in the database.
        val newTask = Task("title", "description", false)
        tasksLocalDataSource.saveTask(newTask)

        // WHEN  - Task retrieved by ID.
        val result = tasksLocalDataSource.getTask(newTask.id)

        // THEN - Same task is returned.
        assertThat(result.succeeded, `is`(true))
        result as Result.Success
        assertThat(result.data, `is`(newTask))
    }

    @Test
    fun completeTask_retrievedTaskIsComplete() = runBlocking {
        val newTask = Task("title", "description", false)
        tasksLocalDataSource.saveTask(newTask)
        tasksLocalDataSource.completeTask(newTask)
        val completedTask = tasksLocalDataSource.getTask(newTask.id)
        assertThat(completedTask.succeeded, `is`(true))
        completedTask as Result.Success
        assertThat(completedTask.data.isCompleted, `is`(true))
    }
}