package com.example.myrecipebook.rest.api

import com.example.myrecipebook.common.domain.model.Page
import com.example.myrecipebook.common.domain.model.Recipe
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipesRequest {
    @GET("recipes")
    fun getRecipes(
        @Query("limit") limit: Int? = null,
        @Query("skip") skip: Int? = null
    ): Single<Page<Recipe>>

    @GET("recipes/{id}")
    fun getRecipeById(
        @Path("id") id: Int
    ): Single<Recipe>
}
