package com.example.myrecipebook.ui.recipeslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrecipebook.common.domain.model.Difficulty
import com.example.myrecipebook.common.domain.model.Recipe
import com.example.myrecipebook.databinding.FragmentRecipesListBinding

class RecipesListFragment : Fragment() {

    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RecipesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = RecipesAdapter(onClick = { /* TODO: open recipe details */ })
        binding.recyclerRecipesList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerRecipesList.adapter = adapter
        adapter.submitList(sampleRecipes())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun sampleRecipes(): List<Recipe> = listOf(
        Recipe(
            id = 1,
            name = "Classic Margherita Pizza",
            ingredients = emptyList(),
            instructions = emptyList(),
            prepTimeMinutes = 20,
            cookTimeMinutes = 15,
            servings = 4,
            difficulty = Difficulty.Easy,
            cuisine = "Italian",
            caloriesPerServing = 300,
            tags = listOf("Pizza", "Italian"),
            userId = 0,
            image = "",
            rating = 4.6,
            reviewCount = 0,
            mealType = listOf("Dinner")
        ),
        Recipe(
            id = 1,
            name = "Classic Margherita Pizza",
            ingredients = emptyList(),
            instructions = emptyList(),
            prepTimeMinutes = 20,
            cookTimeMinutes = 15,
            servings = 4,
            difficulty = Difficulty.Easy,
            cuisine = "Italian",
            caloriesPerServing = 300,
            tags = listOf("Pizza", "Italian"),
            userId = 0,
            image = "",
            rating = 4.6,
            reviewCount = 0,
            mealType = listOf("Dinner")
        )
    )
}
