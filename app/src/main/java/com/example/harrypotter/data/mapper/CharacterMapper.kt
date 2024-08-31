package com.example.harrypotter.data.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.harrypotter.data.source.local.model.CharacterEntity
import com.example.harrypotter.data.source.remote.model.CharacterApiModel
import com.example.harrypotter.ui.theme.GryffindorColor
import com.example.harrypotter.ui.theme.HufflepuffColor
import com.example.harrypotter.ui.theme.RavenclawColor
import com.example.harrypotter.ui.theme.SlytherinColor
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class CharacterMapper @Inject constructor() {

    fun mapToUiModel(characterApiModel: CharacterApiModel): List<CharacterEntity> {
        return characterApiModel.map {
            CharacterEntity(
                name = it.name,
                actor = it.actor,
                species = it.species,
                house = it.house,
                houseColor = getHouseColor(it.house).toArgb(),
                dateOfBirth = formatDate(it.dateOfBirth),
                imageUrl = it.image,
                status = if (it.alive) "Alive" else "Deceased"
            )
        }

    }

    private fun getHouseColor(house: String?): Color {
        return when (house) {
            "Gryffindor" -> GryffindorColor
            "Slytherin" -> SlytherinColor
            "Ravenclaw" -> RavenclawColor
            "Hufflepuff" -> HufflepuffColor
            else -> Color.Gray
        }
    }

    private fun formatDate(date: String?): String? {
        return date?.let {
            try {
                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val parsedDate = sdf.parse(it)
                val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                outputFormat.format(parsedDate!!)
            } catch (e: Exception) {
                null
            }
        }
    }
}