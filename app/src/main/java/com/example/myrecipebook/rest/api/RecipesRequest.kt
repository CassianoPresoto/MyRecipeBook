package com.example.myrecipebook.rest.api

import com.example.myrecipebook.common.domain.model.RecipeMessage
import com.example.myrecipebook.common.domain.model.RecipesPageMessage
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipesRequest {
    @GET("recipes")
    fun getRecipes(
        @Query("limit") limit: Int? = null,
        @Query("skip") skip: Int? = null
    ): Single<RecipesPageMessage>

    @GET("recipes/{id}")
    fun getRecipeById(
        @Path("id") id: Int
    ): Single<RecipeMessage>
}
