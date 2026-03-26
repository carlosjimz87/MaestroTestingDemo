package com.carlosjimz87.maestrotestingdemo.data

import java.time.LocalDate

object SampleData {
    fun getTasks(): List<Task> = listOf(
        Task("1", "Review pull requests", "Check open PRs on the main repo and provide feedback", Priority.HIGH, false, LocalDate.now().plusDays(1), Category.WORK),
        Task("2", "Buy groceries", "Milk, eggs, bread, fruits, vegetables", Priority.MEDIUM, false, LocalDate.now(), Category.SHOPPING),
        Task("3", "Morning run", "5km run in the park", Priority.LOW, true, LocalDate.now(), Category.HEALTH),
        Task("4", "Prepare presentation", "Create slides for the Maestro testing demo", Priority.HIGH, false, LocalDate.now().plusDays(3), Category.WORK),
        Task("5", "Call dentist", "Schedule annual checkup appointment", Priority.MEDIUM, false, LocalDate.now().plusDays(7), Category.HEALTH),
        Task("6", "Update dependencies", "Upgrade Kotlin and Compose to latest versions", Priority.HIGH, false, LocalDate.now().plusDays(2), Category.WORK),
        Task("7", "Read Kotlin coroutines book", "Chapter 5-7 on structured concurrency", Priority.LOW, false, LocalDate.now().plusDays(14), Category.PERSONAL),
        Task("8", "Fix login bug", "Users report intermittent login failures on slow networks", Priority.HIGH, false, LocalDate.now(), Category.WORK),
        Task("9", "Organize desk", "Clean up cables and sort documents", Priority.LOW, true, LocalDate.now().minusDays(1), Category.PERSONAL),
        Task("10", "Gym session", "Upper body workout and stretching", Priority.MEDIUM, false, LocalDate.now().plusDays(1), Category.HEALTH),
        Task("11", "Order new keyboard", "Mechanical keyboard with brown switches", Priority.LOW, false, LocalDate.now().plusDays(5), Category.SHOPPING),
        Task("12", "Write unit tests", "Cover the TaskRepository with tests", Priority.HIGH, false, LocalDate.now().plusDays(2), Category.WORK),
        Task("13", "Plan weekend trip", "Research destinations and book accommodation", Priority.MEDIUM, false, LocalDate.now().plusDays(4), Category.PERSONAL),
        Task("14", "Refactor navigation", "Migrate to type-safe navigation in Compose", Priority.MEDIUM, false, LocalDate.now().plusDays(6), Category.WORK),
        Task("15", "Buy vitamins", "Vitamin D and Omega-3 supplements", Priority.LOW, false, LocalDate.now().plusDays(3), Category.SHOPPING),
        Task("16", "Meditate", "15 minutes guided meditation", Priority.LOW, true, LocalDate.now(), Category.HEALTH),
        Task("17", "Deploy staging build", "Push v2.1.0 to staging environment", Priority.HIGH, false, LocalDate.now(), Category.WORK),
        Task("18", "Water plants", "Indoor plants need watering every 3 days", Priority.LOW, false, LocalDate.now().plusDays(1), Category.PERSONAL),
        Task("19", "Schedule team sync", "Weekly standup for the mobile team", Priority.MEDIUM, false, LocalDate.now().plusDays(2), Category.WORK),
        Task("20", "Yoga class", "Evening yoga at 6 PM", Priority.MEDIUM, false, LocalDate.now().plusDays(1), Category.HEALTH),
        Task("21", "Return package", "Return the wrong-sized shoes", Priority.HIGH, false, LocalDate.now(), Category.SHOPPING),
        Task("22", "Backup photos", "Export photos from phone to cloud storage", Priority.LOW, false, LocalDate.now().plusDays(10), Category.PERSONAL),
        Task("23", "Code review meeting", "Review architecture decisions with the team", Priority.HIGH, false, LocalDate.now().plusDays(1), Category.WORK),
        Task("24", "Buy birthday gift", "Gift for Sarah's birthday next week", Priority.MEDIUM, false, LocalDate.now().plusDays(5), Category.SHOPPING)
    )
}
