package com.carlosjimz87.maestrotestingdemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosjimz87.maestrotestingdemo.data.Category
import com.carlosjimz87.maestrotestingdemo.data.Priority
import com.carlosjimz87.maestrotestingdemo.data.Task
import com.carlosjimz87.maestrotestingdemo.data.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
)

class TaskViewModel : ViewModel() {
    private val repository = TaskRepository()
    private val _isLoading = MutableStateFlow(true)
    private val _isRefreshing = MutableStateFlow(false)
    private val _searchQuery = MutableStateFlow("")
    private val _searchResults = MutableStateFlow<List<Task>>(emptyList())

    val searchQuery: StateFlow<String> = _searchQuery
    val searchResults: StateFlow<List<Task>> = _searchResults

    val uiState: StateFlow<TaskUiState> = combine(
        repository.getTasks(),
        _isLoading,
        _isRefreshing
    ) { tasks, loading, refreshing ->
        TaskUiState(tasks = tasks, isLoading = loading, isRefreshing = refreshing)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskUiState(isLoading = true))

    init {
        viewModelScope.launch {
            kotlinx.coroutines.delay(800)
            _isLoading.value = false
        }
    }

    fun getTask(id: String): Task? = repository.getTask(id)

    fun addTask(title: String, description: String, priority: Priority, category: Category, dueDate: LocalDate?) {
        viewModelScope.launch {
            val task = Task(
                id = repository.getNextId(),
                title = title,
                description = description,
                priority = priority,
                category = category,
                dueDate = dueDate
            )
            repository.addTask(task)
        }
    }

    fun deleteTask(id: String) {
        viewModelScope.launch {
            repository.deleteTask(id)
        }
    }

    fun toggleComplete(id: String) {
        viewModelScope.launch {
            repository.toggleComplete(id)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            repository.refresh()
            _isRefreshing.value = false
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            repository.searchTasks(query).collect { results ->
                _searchResults.value = results
            }
        }
    }

    fun getCompletedCount(): Int = uiState.value.tasks.count { it.isCompleted }
    fun getTotalCount(): Int = uiState.value.tasks.size
    fun getTasksByCategory(): Map<Category, Int> = uiState.value.tasks.groupBy { it.category }.mapValues { it.value.size }
    fun getOverdueCount(): Int = uiState.value.tasks.count {
        !it.isCompleted && it.dueDate != null && it.dueDate.isBefore(LocalDate.now())
    }
}
