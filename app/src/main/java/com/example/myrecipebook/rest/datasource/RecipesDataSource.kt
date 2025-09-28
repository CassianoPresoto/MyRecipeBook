package com.example.myrecipebook.rest.datasource

import com.example.myrecipebook.common.domain.model.Recipe
import com.example.myrecipebook.common.domain.model.RecipesPage
import io.reactivex.rxjava3.core.Single

interface RecipesDataSource {
    fun getRecipes(limit: Int? = null, skip: Int? = null): Single<RecipesPage>
    fun getRecipeById(id: Int): Single<Recipe>
}
