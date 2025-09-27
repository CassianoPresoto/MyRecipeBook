package com.example.myrecipebook.rest.datasource

import com.example.myrecipebook.common.domain.model.Recipe
import com.example.myrecipebook.common.domain.model.RecipesPage
import com.example.myrecipebook.rest.api.RecipesRequest
import com.example.myrecipebook.rest.mapper.RecipeMapper
import io.reactivex.rxjava3.core.Single

/**
 * Implementação concreta da fonte de dados remota usando DummyJSON.
 * Mapeia DTOs para modelos de domínio e expõe apenas domínio.
 */
class RecipesDataSourceImp(
    private val api: RecipesRequest
) : RecipesDataSource {

    override fun getRecipes(limit: Int?, skip: Int?): Single<RecipesPage> =
        api.getRecipes(limit = limit, skip = skip)
            .map { message -> RecipeMapper.toDomain(message) }

    override fun getRecipeById(id: Int): Single<Recipe> =
        api.getRecipeById(id)
            .map { message -> RecipeMapper.toDomain(message) }
}
