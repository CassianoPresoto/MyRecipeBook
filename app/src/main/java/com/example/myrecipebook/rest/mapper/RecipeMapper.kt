package com.example.myrecipebook.rest.mapper

import com.example.myrecipebook.common.domain.model.Difficulty
import com.example.myrecipebook.common.domain.model.Recipe
import com.example.myrecipebook.common.domain.model.RecipeMessage
import com.example.myrecipebook.common.domain.model.RecipesPage
import com.example.myrecipebook.common.domain.model.RecipesPageMessage

object RecipeMapper {

    fun toDomain(message: RecipeMessage): Recipe = Recipe(
        id = message.id,
        name = message.name,
        ingredients = message.ingredients,
        instructions = message.instructions,
        prepTimeMinutes = message.prepTimeMinutes,
        cookTimeMinutes = message.cookTimeMinutes,
        servings = message.servings,
        difficulty = message.difficulty.toDifficulty(),
        cuisine = message.cuisine,
        caloriesPerServing = message.caloriesPerServing,
        tags = message.tags,
        userId = message.userId,
        image = message.image,
        rating = message.rating,
        reviewCount = message.reviewCount,
        mealType = message.mealType
    )

    fun toDomain(page: RecipesPageMessage): RecipesPage = RecipesPage(
        recipes = page.recipes.map { toDomain(it) },
        total = page.total,
        skip = page.skip,
        limit = page.limit
    )
}

private fun String.toDifficulty(): Difficulty = when (this.lowercase()) {
    "easy" -> Difficulty.Easy
    "medium" -> Difficulty.Medium
    "hard" -> Difficulty.Hard
    else -> Difficulty.Unknown
}
