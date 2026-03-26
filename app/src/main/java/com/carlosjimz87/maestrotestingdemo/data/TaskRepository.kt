package com.carlosjimz87.maestrotestingdemo.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class TaskRepository {
    private val tasks = MutableStateFlow(SampleData.getTasks())

    fun getTasks(): Flow<List<Task>> = tasks

    fun getTask(id: String): Task? = tasks.value.find { it.id == id }

    suspend fun addTask(task: Task) {
        delay(500)
        tasks.value = tasks.value + task
    }

    suspend fun deleteTask(id: String) {
        delay(300)
        tasks.value = tasks.value.filter { it.id != id }
    }

    suspend fun toggleComplete(id: String) {
        delay(200)
        tasks.value = tasks.value.map {
            if (it.id == id) it.copy(isCompleted = !it.isCompleted) else it
        }
    }

    fun searchTasks(query: String): Flow<List<Task>> = tasks.map { list ->
        list.filter {
            it.title.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true)
        }
    }

    suspend fun refresh() {
        delay(1000)
        // Simulate a network refresh - in a real app this would fetch from API
    }

    fun getNextId(): String {
        val maxId = tasks.value.maxOfOrNull { it.id.toIntOrNull() ?: 0 } ?: 0
        return (maxId + 1).toString()
    }
}
