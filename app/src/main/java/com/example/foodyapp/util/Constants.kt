package com.example.foodyapp.util

class Constants {

    companion object {
        const val BASE_URL = "https://api.spoonacular.com"
        const val API_KEY = "c0da77e30f904c768995b89c44d5556e"

        // API Query Keys
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        // Room Database
        const val DATABASE_NAME = "recipes_database"
        const val RECIPES_TABLE = "recipes_table"
    }

}