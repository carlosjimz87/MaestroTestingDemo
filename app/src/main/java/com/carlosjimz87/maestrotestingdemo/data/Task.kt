package com.carlosjimz87.maestrotestingdemo.data

import java.time.LocalDate

enum class Priority { HIGH, MEDIUM, LOW }

enum class Category(val label: String) {
    WORK("Work"),
    PERSONAL("Personal"),
    SHOPPING("Shopping"),
    HEALTH("Health")
}

data class Task(
    val id: String,
    val title: String,
    val description: String = "",
    val priority: Priority = Priority.MEDIUM,
    val isCompleted: Boolean = false,
    val dueDate: LocalDate? = null,
    val category: Category = Category.PERSONAL
)
