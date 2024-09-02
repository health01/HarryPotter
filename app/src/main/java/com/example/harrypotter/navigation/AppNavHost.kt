package com.example.harrypotter.navigation

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.harrypotter.data.source.local.model.CharacterEntity
import com.example.harrypotter.ui.theme.screen.details.CharacterDetailContent
import com.example.harrypotter.ui.theme.screen.home.HomeScreen
import com.google.gson.Gson

@SuppressLint("NewApi")
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {

        composable(route = Home.route,
            enterTransition = { fadeIn(animationSpec = tween(500)) },
            exitTransition = { fadeOut(animationSpec = tween(500)) }
        ) {
            HomeScreen(
                onNavigationRequested = { character ->
                    val json = Uri.encode(Gson().toJson(character))
                    navController.navigate("${CharDetail.route}/$json")
                }
            )
        }

        composable(
            route = CharDetail.routeWithArgs,
            arguments = CharDetail.arguments,
            enterTransition = { fadeIn(animationSpec = tween(500)) },
            exitTransition = { fadeOut(animationSpec = tween(500)) }

        ) { backStackEntry ->
            val character =
                backStackEntry.arguments?.getSerializable(
                    CharDetail.charArg,
                    CharacterEntity::class.java
                )

            CharacterDetailContent(character)
        }
    }
}