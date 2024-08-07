package com.example.TeamApp.auth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.TeamApp.auth.ui.theme.TeamAppTheme

class ForgotPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Make the content extend into the status bar and navigation bar areas
        WindowCompat.setDecorFitsSystemWindows(window, false)

        super.onCreate(savedInstanceState)

        // Ensure edge-to-edge content
        setEdgeToEdge()

        setContent {
            TeamAppTheme {
                ForgotPasswordUI()
            }
        }
    }

    private fun setEdgeToEdge() {
        WindowInsetsControllerCompat(window, window.decorView).apply {
            // Make status bar icons dark to contrast with light background
            isAppearanceLightStatusBars = true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
