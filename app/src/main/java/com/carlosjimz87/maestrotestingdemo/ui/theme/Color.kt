package com.carlosjimz87.maestrotestingdemo.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.carlosjimz87.maestrotestingdemo.data.Priority

// TaskFlow teal/blue palette
val Teal80 = Color(0xFF80CBC4)
val TealGrey80 = Color(0xFFB2DFDB)
val Amber80 = Color(0xFFFFE082)

val Teal40 = Color(0xFF00897B)
val TealGrey40 = Color(0xFF4DB6AC)
val Amber40 = Color(0xFFFF8F00)

// Priority colors
val PriorityHigh = Color(0xFFE53935)
val PriorityMedium = Color(0xFFFB8C00)
val PriorityLow = Color(0xFF43A047)

// Category colors
val CategoryWork = Color(0xFF1E88E5)
val CategoryPersonal = Color(0xFF8E24AA)
val CategoryShopping = Color(0xFFFDD835)
val CategoryHealth = Color(0xFF43A047)

@Composable
fun priorityColor(priority: Priority): Color = when (priority) {
    Priority.HIGH -> PriorityHigh
    Priority.MEDIUM -> PriorityMedium
    Priority.LOW -> PriorityLow
}
