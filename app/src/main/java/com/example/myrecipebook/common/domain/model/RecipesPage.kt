package com.example.myrecipebook.common.domain.model

/**
 * Domain model for the paginated list of recipes returned by /recipes.
 */
data class RecipesPage(
    val recipes: List<Recipe>,
    val total: Int,
    val skip: Int,
    val limit: Int
) {
    val hasNextPage: Boolean get() = skip + limit < total
}
