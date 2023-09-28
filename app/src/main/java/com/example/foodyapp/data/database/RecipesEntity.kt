package com.example.foodyapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodyapp.models.FoodRecipe
import com.example.foodyapp.util.Constants

@Entity(tableName = Constants.RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0

}