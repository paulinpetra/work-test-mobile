package com.paulin.work_test_mobile.ui.components

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

//component to get rid off status bar in detail screen

@Composable
fun TransparentStatusBarEffect(hideStatusBarIcons: Boolean = true) {
    val view = LocalView.current
    val context = LocalContext.current

    DisposableEffect(Unit) {//clean up side effect when composable leaves the composition
        val window = (context as Activity).window
        val controller = WindowInsetsControllerCompat(
            window,
            view
        )//utility class that provides a consistent API for controlling window insets ( status bar)

        // remember the original state
        val originalDecorFitsSystemWindows = WindowCompat.getInsetsController(window, view)
            .systemBarsBehavior
        val originalLightStatusBars = controller.isAppearanceLightStatusBars

        // make the status bar transparent and draw under it
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // make status bar transparent using the controller
        controller.apply {
            if (hideStatusBarIcons) {
                // hide the status bar completely
                hide(WindowInsetsCompat.Type.statusBars())
            } else {
                // show the status bar but make it transparent
                show(WindowInsetsCompat.Type.statusBars())
            }
        }

        onDispose {
            // reset to original state when leaving the screen
            WindowCompat.setDecorFitsSystemWindows(window, true)
            controller.apply {
                systemBarsBehavior = originalDecorFitsSystemWindows
                isAppearanceLightStatusBars = originalLightStatusBars
                // Show the status bar again when leaving
                show(WindowInsetsCompat.Type.statusBars())
            }
        }
    }
}