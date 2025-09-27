package com.example.myrecipebook.rest.api

import com.example.myrecipebook.rest.message.RecipeMessage
import com.example.myrecipebook.rest.message.RecipesPageMessage
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API for DummyJSON Recipes endpoints.
 * Base URL: https://dummyjson.com/
 */
interface RecipesRequest {
    /**
     * GET /recipes
     * Supports pagination using [limit] and [skip].
     */
    @GET("recipes")
    fun getRecipes(
        @Query("limit") limit: Int? = null,
        @Query("skip") skip: Int? = null
    ): Single<RecipesPageMessage>

    /**
     * GET /recipes/{id}
     */
    @GET("recipes/{id}")
    fun getRecipeById(
        @Path("id") id: Int
    ): Single<RecipeMessage>
}
