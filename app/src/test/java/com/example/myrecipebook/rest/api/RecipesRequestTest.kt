package com.example.myrecipebook.rest.api

import com.example.myrecipebook.common.domain.model.Page
import com.example.myrecipebook.common.domain.model.Recipe
import kotlinx.serialization.json.Json
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.HttpException
import kotlinx.serialization.SerializationException

class RecipesRequestTest {

    private lateinit var server: MockWebServer
    private lateinit var api: RecipesRequest

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        val json = Json { ignoreUnknownKeys = true }
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
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
        val response: Page<Recipe> = api.getRecipes(limit = 30, skip = 0).blockingGet()

        // Assert
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/recipes?limit=30&skip=0", request.path)

        assertNotNull(response)
        assertEquals(50, response.total)
        assertEquals(0, response.skip)
        assertEquals(30, response.limit)
        assertEquals(1, response.items.size)
        assertEquals(1, response.items.first().id)
        assertEquals("Classic Margherita Pizza", response.items.first().name)
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
        val response: Recipe = api.getRecipeById(10).blockingGet()

        // Assert
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/recipes/10", request.path)

        assertNotNull(response)
        assertEquals(10, response.id)
        assertEquals("Shrimp Scampi Pasta", response.name)
        assertEquals("Medium", response.difficulty.name)
    }

    @Test
    fun `getRecipes without params omits query`() {
        val body = """
            { "recipes": [], "total": 0, "skip": 0, "limit": 30 }
        """.trimIndent()
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        val response: Page<Recipe> = api.getRecipes().blockingGet()

        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/recipes", request.path)
        assertTrue(response.items.isEmpty())
        assertFalse(response.hasNextPage)
    }

    @Test
    fun `getRecipes empty list and hasNextPage false`() {
        val body = """
            { "recipes": [], "total": 0, "skip": 0, "limit": 30 }
        """.trimIndent()
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        val response: Page<Recipe> = api.getRecipes(limit = 30, skip = 0).blockingGet()
        assertTrue(response.items.isEmpty())
        assertFalse(response.hasNextPage)
    }

    @Test
    fun `getRecipes hasNextPage true when skip plus limit is less than total`() {
        val body = """
            { "recipes": [], "total": 50, "skip": 0, "limit": 30 }
        """.trimIndent()
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        val response: Page<Recipe> = api.getRecipes(limit = 30, skip = 0).blockingGet()
        assertTrue(response.hasNextPage)
    }

    @Test
    fun `getRecipes ignores unknown fields`() {
        val body = """
            {
              "recipes": [
                { "id": 1, "name": "Test", "ingredients": [], "instructions": [],
                  "prepTimeMinutes": 0, "cookTimeMinutes": 0, "servings": 1,
                  "difficulty": "Easy", "cuisine": "N/A", "caloriesPerServing": 1,
                  "tags": [], "userId": 1, "image": "", "rating": 1.0, "reviewCount": 0,
                  "mealType": [], "unknownInside": 123 }
              ],
              "total": 1, "skip": 0, "limit": 30, "extraRoot": "ignored"
            }
        """.trimIndent()
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        val response: Page<Recipe> = api.getRecipes().blockingGet()
        assertEquals(1, response.items.size)
        assertEquals("Test", response.items.first().name)
    }

    @Test
    fun `unicode and precision are preserved`() {
        val body = """
            {
              "recipes": [
                { "id": 2, "name": "Créme Brûlée 😋", "ingredients": [], "instructions": [],
                  "prepTimeMinutes": 0, "cookTimeMinutes": 0, "servings": 1,
                  "difficulty": "Hard", "cuisine": "FR", "caloriesPerServing": 1,
                  "tags": [], "userId": 1, "image": "", "rating": 4.6666667, "reviewCount": 0,
                  "mealType": [] }
              ],
              "total": 1, "skip": 0, "limit": 30
            }
        """.trimIndent()
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        val response: Page<Recipe> = api.getRecipes().blockingGet()
        val item = response.items.first()
        assertEquals("Créme Brûlée 😋", item.name)
        assertEquals(4.6666667, item.rating, 0.0000001)
    }

    @Test
    fun `enum Difficulty parses valid values`() {
        fun recipeWith(diff: String) = """
            { "id": 1, "name": "R", "ingredients": [], "instructions": [],
              "prepTimeMinutes": 0, "cookTimeMinutes": 0, "servings": 1,
              "difficulty": "$diff", "cuisine": "", "caloriesPerServing": 0,
              "tags": [], "userId": 0, "image": "", "rating": 0.0, "reviewCount": 0,
              "mealType": [] }
        """.trimIndent()

        server.enqueue(MockResponse().setResponseCode(200).setBody(recipeWith("Easy")))
        assertEquals("Easy", api.getRecipeById(1).blockingGet().difficulty.name)

        server.enqueue(MockResponse().setResponseCode(200).setBody(recipeWith("Medium")))
        assertEquals("Medium", api.getRecipeById(2).blockingGet().difficulty.name)

        server.enqueue(MockResponse().setResponseCode(200).setBody(recipeWith("Hard")))
        assertEquals("Hard", api.getRecipeById(3).blockingGet().difficulty.name)
    }

    @Test
    fun `enum Difficulty invalid value throws SerializationException`() {
        val body = """
            { "id": 9, "name": "X", "ingredients": [], "instructions": [],
              "prepTimeMinutes": 0, "cookTimeMinutes": 0, "servings": 1,
              "difficulty": "Unknownish", "cuisine": "", "caloriesPerServing": 0,
              "tags": [], "userId": 0, "image": "", "rating": 0.0, "reviewCount": 0,
              "mealType": [] }
        """.trimIndent()
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        assertThrows(SerializationException::class.java) {
            api.getRecipeById(9).blockingGet()
        }
    }

    @Test
    fun `getRecipeById 404 returns HttpException`() {
        server.enqueue(MockResponse().setResponseCode(404))
        assertThrows(HttpException::class.java) {
            api.getRecipeById(999).blockingGet()
        }
        val request = server.takeRequest()
        assertEquals("/recipes/999", request.path)
    }

    @Test
    fun `getRecipes 500 returns HttpException`() {
        server.enqueue(MockResponse().setResponseCode(500))
        assertThrows(HttpException::class.java) {
            api.getRecipes(limit = 10, skip = 5).blockingGet()
        }
        val request = server.takeRequest()
        assertEquals("/recipes?limit=10&skip=5", request.path)
    }

    @Test
    fun `query params limit and skip are sent correctly`() {
        val body = """
            { "recipes": [], "total": 0, "skip": 200, "limit": 100 }
        """.trimIndent()
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        api.getRecipes(limit = 100, skip = 200).blockingGet()
        val request = server.takeRequest()
        assertEquals("/recipes?limit=100&skip=200", request.path)
    }

    @Test
    fun `getRecipes returns multiple items preserving order`() {
        val body = """
            {
              "recipes": [
                { "id": 1, "name": "A", "ingredients": [], "instructions": [],
                  "prepTimeMinutes": 0, "cookTimeMinutes": 0, "servings": 1,
                  "difficulty": "Easy", "cuisine": "", "caloriesPerServing": 0,
                  "tags": [], "userId": 0, "image": "", "rating": 0.0, "reviewCount": 0,
                  "mealType": [] },
                { "id": 2, "name": "B", "ingredients": [], "instructions": [],
                  "prepTimeMinutes": 0, "cookTimeMinutes": 0, "servings": 1,
                  "difficulty": "Medium", "cuisine": "", "caloriesPerServing": 0,
                  "tags": [], "userId": 0, "image": "", "rating": 0.0, "reviewCount": 0,
                  "mealType": [] }
              ],
              "total": 2, "skip": 0, "limit": 30
            }
        """.trimIndent()
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        val response: Page<Recipe> = api.getRecipes().blockingGet()
        assertEquals(listOf(1, 2), response.items.map { it.id })
    }
}
