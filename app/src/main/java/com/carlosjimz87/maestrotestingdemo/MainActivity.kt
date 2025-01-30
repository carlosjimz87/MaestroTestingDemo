package com.carlosjimz87.maestrotestingdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.carlosjimz87.maestrotestingdemo.navigation.MyNavController
import com.carlosjimz87.maestrotestingdemo.ui.screens.LoginScreen
import com.carlosjimz87.maestrotestingdemo.ui.theme.MaestroTestingDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaestroTestingDemoTheme {
                MaestroDemoApp()
            }
        }
    }
}

@Composable
fun MaestroDemoApp() {
    val navController = rememberNavController()
    MyNavController(navController)
}






@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(rememberNavController())
}
