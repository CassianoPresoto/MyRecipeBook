package com.example.myrecipebook.rest.api

import com.example.myrecipebook.common.domain.model.RecipeMessage
import com.example.myrecipebook.common.domain.model.RecipesPageMessage
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class RecipesRequestTest {

    private lateinit var server: MockWebServer
    private lateinit var api: RecipesRequest

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        api = retrofit.create(RecipesRequest::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `getRecipes returns list and pagination`() {
        // Arrange
        val body = """
            {
              "recipes": [
                {
                  "id": 1,
                  "name": "Classic Margherita Pizza",
                  "ingredients": ["Pizza dough", "Tomato sauce"],
                  "instructions": ["Preheat oven"],
                  "prepTimeMinutes": 20,
                  "cookTimeMinutes": 15,
                  "servings": 4,
                  "difficulty": "Easy",
                  "cuisine": "Italian",
                  "caloriesPerServing": 300,
                  "tags": ["Pizza","Italian"],
                  "userId": 166,
                  "image": "https://cdn.dummyjson.com/recipe-images/1.webp",
                  "rating": 4.6,
                  "reviewCount": 98,
                  "mealType": ["Dinner"]
                }
              ],
              "total": 50,
              "skip": 0,
              "limit": 30
            }
        """.trimIndent()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(body)
        )

        // Act
        val response: RecipesPageMessage = api.getRecipes(limit = 30, skip = 0).blockingGet()

        // Assert
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/recipes?limit=30&skip=0", request.path)

        assertNotNull(response)
        assertEquals(50, response.total)
        assertEquals(0, response.skip)
        assertEquals(30, response.limit)
        assertEquals(1, response.recipes.size)
        assertEquals(1, response.recipes.first().id)
        assertEquals("Classic Margherita Pizza", response.recipes.first().name)
    }

    @Test
    fun `getRecipeById returns one recipe`() {
        // Arrange
        val body = """
            {
              "id": 10,
              "name": "Shrimp Scampi Pasta",
              "ingredients": ["Linguine pasta","Shrimp"],
              "instructions": ["Cook pasta"],
              "prepTimeMinutes": 15,
              "cookTimeMinutes": 20,
              "servings": 3,
              "difficulty": "Medium",
              "cuisine": "Italian",
              "caloriesPerServing": 400,
              "tags": ["Pasta","Shrimp"],
              "userId": 114,
              "image": "https://cdn.dummyjson.com/recipe-images/10.webp",
              "rating": 4.3,
              "reviewCount": 5,
              "mealType": ["Dinner"]
            }
        """.trimIndent()
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(body)
        )

        // Act
        val response: RecipeMessage = api.getRecipeById(10).blockingGet()

        // Assert
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/recipes/10", request.path)

        assertNotNull(response)
        assertEquals(10, response.id)
        assertEquals("Shrimp Scampi Pasta", response.name)
        assertEquals("Medium", response.difficulty)
    }
}
