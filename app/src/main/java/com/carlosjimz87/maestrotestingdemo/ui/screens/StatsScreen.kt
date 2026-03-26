package com.carlosjimz87.maestrotestingdemo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.carlosjimz87.maestrotestingdemo.data.Category
import com.carlosjimz87.maestrotestingdemo.ui.theme.CategoryHealth
import com.carlosjimz87.maestrotestingdemo.ui.theme.CategoryPersonal
import com.carlosjimz87.maestrotestingdemo.ui.theme.CategoryShopping
import com.carlosjimz87.maestrotestingdemo.ui.theme.CategoryWork

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    completedCount: Int,
    totalCount: Int,
    tasksByCategory: Map<Category, Int>,
    overdueCount: Int,
    onBack: () -> Unit
) {
    val completionRate = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statistics") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .testTag("stats_screen")
        ) {
            // Completion progress
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Completion Rate", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { completionRate },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .testTag("completion_progress"),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "$completedCount of $totalCount tasks completed (${(completionRate * 100).toInt()}%)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.testTag("completion_text")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Overdue
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (overdueCount > 0)
                        MaterialTheme.colorScheme.errorContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Overdue Tasks", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "$overdueCount",
                        style = MaterialTheme.typography.headlineMedium,
                        color = if (overdueCount > 0)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.primary,
                        modifier = Modifier.testTag("overdue_count")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category breakdown
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Tasks by Category", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(12.dp))

                    val maxCount = tasksByCategory.values.maxOrNull() ?: 1
                    Category.entries.forEach { category ->
                        val count = tasksByCategory[category] ?: 0
                        val color = categoryColor(category)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                category.label,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.width(80.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(24.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(if (maxCount > 0) count.toFloat() / maxCount else 0f)
                                        .height(24.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(color)
                                        .testTag("category_bar_${category.name}")
                                )
                            }
                            Text(
                                "$count",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .width(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun categoryColor(category: Category): Color = when (category) {
    Category.WORK -> CategoryWork
    Category.PERSONAL -> CategoryPersonal
    Category.SHOPPING -> CategoryShopping
    Category.HEALTH -> CategoryHealth
}
