package com.example.myrecipebook.common.domain.model

/**
 * Domain entity representing a Recipe from the DummyJSON API.
 * This model is UI-agnostic and should be used across layers.
 */
data class Recipe(
    val id: RecipeId,
    val name: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val servings: Int,
    val difficulty: Difficulty,
    val cuisine: String,
    val caloriesPerServing: Int,
    val tags: List<String>,
    val userId: Int,
    val image: String,
    val rating: Double,
    val reviewCount: Int,
    val mealType: List<String>
)

/**
 * Domain-specific type alias for improving readability and intent.
 */
typealias RecipeId = Int

/**
 * Normalized difficulty representation. Mapping from API string values should
 * be handled by the REST/mapper layer.
 */
enum class Difficulty {
    Easy,
    Medium,
    Hard,
    Unknown
}
