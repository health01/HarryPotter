package com.example.harrypotter.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.harrypotter.ui.theme.HarryPotterTheme
import org.junit.Rule

open class BaseComposeTest {
    @get:Rule
    val composeRule = createComposeRule()

    protected fun launchComposable(content: @Composable () -> Unit) {
        composeRule.setContent {
            HarryPotterTheme {
                content()
            }
        }
    }
} 