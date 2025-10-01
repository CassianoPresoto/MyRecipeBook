package com.example.myrecipebook.rest.datasource

import com.example.myrecipebook.common.domain.model.Recipe
import com.example.myrecipebook.common.domain.model.Page
import com.example.myrecipebook.rest.api.RecipesRequest
import io.reactivex.rxjava3.core.Single

class RecipesDataSourceImp(
    private val api: RecipesRequest
) : RecipesDataSource {

    override fun getRecipes(limit: Int?, skip: Int?): Single<Page<Recipe>> =
        api.getRecipes(limit = limit, skip = skip)

    override fun getRecipeById(id: Int): Single<Recipe> =
        api.getRecipeById(id)
}
