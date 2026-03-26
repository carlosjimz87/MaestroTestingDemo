package com.carlosjimz87.maestrotestingdemo.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.carlosjimz87.maestrotestingdemo.ui.screens.AddEditTaskScreen
import com.carlosjimz87.maestrotestingdemo.ui.screens.LoginScreen
import com.carlosjimz87.maestrotestingdemo.ui.screens.ProfileScreen
import com.carlosjimz87.maestrotestingdemo.ui.screens.SearchScreen
import com.carlosjimz87.maestrotestingdemo.ui.screens.SettingsScreen
import com.carlosjimz87.maestrotestingdemo.ui.screens.StatsScreen
import com.carlosjimz87.maestrotestingdemo.ui.screens.TaskDetailScreen
import com.carlosjimz87.maestrotestingdemo.ui.screens.TaskListScreen
import com.carlosjimz87.maestrotestingdemo.viewmodel.TaskViewModel

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    data object Tasks : BottomNavItem("tasks", "Tasks", Icons.Default.TaskAlt)
    data object Search : BottomNavItem("search", "Search", Icons.Default.Search)
    data object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
}

private val bottomNavItems = listOf(BottomNavItem.Tasks, BottomNavItem.Search, BottomNavItem.Profile)
private val bottomNavRoutes = bottomNavItems.map { it.route }.toSet()

@Composable
fun MyNavController(navController: NavHostController) {
    val taskViewModel: TaskViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in bottomNavRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            modifier = Modifier.testTag("tab_${item.route}")
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController,
            startDestination = "login",
            modifier = Modifier.padding(padding)
        ) {
            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate("tasks") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }

            composable("tasks") {
                val uiState by taskViewModel.uiState.collectAsState()
                TaskListScreen(
                    uiState = uiState,
                    onTaskClick = { taskId -> navController.navigate("task/$taskId") },
                    onToggleComplete = { taskId -> taskViewModel.toggleComplete(taskId) },
                    onDeleteTask = { taskId -> taskViewModel.deleteTask(taskId) },
                    onAddTask = { navController.navigate("add_task") },
                    onRefresh = { taskViewModel.refresh() }
                )
            }

            composable("task/{taskId}") { backStackEntry ->
                val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
                val task = taskViewModel.getTask(taskId)
                TaskDetailScreen(
                    task = task,
                    onBack = { navController.popBackStack() },
                    onToggleComplete = { id ->
                        taskViewModel.toggleComplete(id)
                        navController.popBackStack()
                    }
                )
            }

            composable("add_task") {
                AddEditTaskScreen(
                    onSave = { title, description, priority, category ->
                        taskViewModel.addTask(title, description, priority, category, null)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable("search") {
                val query by taskViewModel.searchQuery.collectAsState()
                val results by taskViewModel.searchResults.collectAsState()
                SearchScreen(
                    query = query,
                    results = results,
                    onQueryChange = { taskViewModel.updateSearchQuery(it) },
                    onTaskClick = { taskId -> navController.navigate("task/$taskId") }
                )
            }

            composable("profile") {
                ProfileScreen(
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onStatsClick = { navController.navigate("stats") },
                    onSettingsClick = { navController.navigate("settings") }
                )
            }

            composable("stats") {
                StatsScreen(
                    completedCount = taskViewModel.getCompletedCount(),
                    totalCount = taskViewModel.getTotalCount(),
                    tasksByCategory = taskViewModel.getTasksByCategory(),
                    overdueCount = taskViewModel.getOverdueCount(),
                    onBack = { navController.popBackStack() }
                )
            }

            composable("settings") {
                SettingsScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
