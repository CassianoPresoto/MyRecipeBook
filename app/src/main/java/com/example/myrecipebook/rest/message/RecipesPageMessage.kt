package com.example.myrecipebook.rest.message

/**
 * Wrapper returned by GET /recipes
 */
 data class RecipesPageMessage(
    val recipes: List<RecipeMessage>,
    val total: Int,
    val skip: Int,
    val limit: Int
 )
