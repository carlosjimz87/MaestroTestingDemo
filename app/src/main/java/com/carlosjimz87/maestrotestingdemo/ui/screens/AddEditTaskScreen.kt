package com.carlosjimz87.maestrotestingdemo.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.carlosjimz87.maestrotestingdemo.data.Category
import com.carlosjimz87.maestrotestingdemo.data.Priority

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    onSave: (title: String, description: String, priority: Priority, category: Category) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(Priority.MEDIUM) }
    var selectedCategory by remember { mutableStateOf(Category.PERSONAL) }
    var titleError by remember { mutableStateOf<String?>(null) }
    var priorityExpanded by remember { mutableStateOf(false) }
    var categoryExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Task") },
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
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    titleError = null
                },
                label = { Text("Title") },
                isError = titleError != null,
                supportingText = titleError?.let { { Text(it) } },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("task_title_field")
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("task_description_field")
            )
            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = priorityExpanded,
                onExpandedChange = { priorityExpanded = it },
                modifier = Modifier.testTag("priority_dropdown")
            ) {
                OutlinedTextField(
                    value = selectedPriority.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Priority") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = priorityExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = priorityExpanded,
                    onDismissRequest = { priorityExpanded = false }
                ) {
                    Priority.entries.forEach { priority ->
                        DropdownMenuItem(
                            text = { Text(priority.name) },
                            onClick = {
                                selectedPriority = priority
                                priorityExpanded = false
                            },
                            modifier = Modifier.testTag("priority_option_${priority.name}")
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = it },
                modifier = Modifier.testTag("category_dropdown")
            ) {
                OutlinedTextField(
                    value = selectedCategory.label,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    Category.entries.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.label) },
                            onClick = {
                                selectedCategory = category
                                categoryExpanded = false
                            },
                            modifier = Modifier.testTag("category_option_${category.label}")
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = {
                        if (title.isBlank()) {
                            titleError = "Title is required"
                        } else if (title.length > 100) {
                            titleError = "Title must be under 100 characters"
                        } else {
                            onSave(title, description, selectedPriority, selectedCategory)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("save_task_button")
                ) {
                    Text("Save Task")
                }
            }
        }
    }
}
