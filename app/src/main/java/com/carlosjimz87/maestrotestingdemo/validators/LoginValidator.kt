package com.carlosjimz87.maestrotestingdemo.validators

sealed class LoginResult {
    data object Success : LoginResult()
    data class Error(val message: String) : LoginResult()
}

object LoginValidator {
    fun validate(user: String, pass: String): Boolean {
        return user == "carlos" && pass == "1234"
    }

    fun validateCredentials(user: String, pass: String): LoginResult {
        if (user.isEmpty() || pass.isEmpty()) {
            return LoginResult.Error("User or password are empty")
        }

        if (user != "carlos" || pass != "1234") {
            return LoginResult.Error("User or password are incorrect")
        }

        return LoginResult.Success
    }
}
