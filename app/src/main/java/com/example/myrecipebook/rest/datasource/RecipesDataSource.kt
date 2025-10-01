package com.example.myrecipebook.rest.datasource

import com.example.myrecipebook.common.domain.model.Recipe
import com.example.myrecipebook.common.domain.model.Page
import io.reactivex.rxjava3.core.Single

interface RecipesDataSource {
    fun getRecipes(limit: Int? = null, skip: Int? = null): Single<Page<Recipe>>
    fun getRecipeById(id: Int): Single<Recipe>
}
