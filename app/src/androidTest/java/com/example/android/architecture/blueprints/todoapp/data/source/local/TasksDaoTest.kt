package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@SmallTest
@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TasksDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ToDoDatabase

    @Before
    fun initializesDatabase() {
        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(), ToDoDatabase::class.java
        ).build()
    }

    @After
    fun closeDatabase() = database.close()

    @Test
    fun insertTaskAndGetById() = runBlockingTest {
        val task = Task("title", "description")
        database.taskDao().insertTask(task)
        val loaded = database.taskDao().getTaskById(task.id)
        assertThat(loaded, notNullValue())
        assertThat(loaded, `is`(task))
    }

    // Write a test that inserts a task, updates it, and then checks that it has the updated values
    @Test
    fun updateTaskAndGetById() = runBlockingTest {
        val task = Task("title", "description")
        val taskToBeUpdated = task.copy(title = "updated task")
        database.taskDao().insertTask(task)
        database.taskDao().updateTask(taskToBeUpdated)
        val updatedTask = database.taskDao().getTaskById(task.id)
        assertThat(updatedTask, `is`(taskToBeUpdated))
    }
}