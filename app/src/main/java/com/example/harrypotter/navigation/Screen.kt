package com.example.harrypotter.navigation

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.harrypotter.data.source.local.model.CharacterEntity
import com.google.gson.Gson

interface Screen {
    val route: String
}

object Home : Screen {
    override val route = "home"
}

object CharDetail : Screen {
    override val route = "char_detail"
    const val charArg = "char"
    val routeWithArgs = "$route/{$charArg}"
    val arguments = listOf(navArgument(charArg) { type = AssetParamType() }
    )
}

class AssetParamType : NavType<CharacterEntity>(isNullableAllowed = false) {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun get(bundle: Bundle, key: String): CharacterEntity? {
        return bundle.getSerializable(key, CharacterEntity::class.java)
    }

    override fun parseValue(value: String): CharacterEntity {
        return Gson().fromJson(value, CharacterEntity::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: CharacterEntity) {
        bundle.putSerializable(key, value)
    }

}