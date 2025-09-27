package com.example.myrecipebook.rest.datasource

import com.example.myrecipebook.common.domain.model.Recipe
import com.example.myrecipebook.common.domain.model.RecipesPage
import io.reactivex.rxjava3.core.Single

/**
 * Abstração da fonte de dados remota para Recipes.
 * Expõe apenas modelos de domínio para camadas superiores.
 */
interface RecipesDataSource {
    fun getRecipes(limit: Int? = null, skip: Int? = null): Single<RecipesPage>
    fun getRecipeById(id: Int): Single<Recipe>
}
