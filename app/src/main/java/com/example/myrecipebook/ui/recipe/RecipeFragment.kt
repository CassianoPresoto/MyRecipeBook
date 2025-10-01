package com.example.myrecipebook.ui.recipe

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.example.myrecipebook.R
import com.example.myrecipebook.common.domain.model.Recipe
import com.example.myrecipebook.common.utils.NetworkUtils
import com.example.myrecipebook.databinding.FragmentRecipeBinding
import com.google.android.material.chip.Chip

class RecipeFragment : Fragment() {

    companion object {
        private const val ARG_RECIPE_ID = "arg_recipe_id"
        fun newInstance(id: Int): RecipeFragment = RecipeFragment().apply {
            arguments = Bundle().apply { putInt(ARG_RECIPE_ID, id) }
        }
    }

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = requireArguments().getInt(ARG_RECIPE_ID)
        ViewCompat.setOnApplyWindowInsetsListener(binding.recipeContent) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val navigationBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.updatePadding(top = systemBars.top, bottom = navigationBars.bottom)
            insets
        }
        observeViewModel()
        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            viewModel.loadRecipe(id)
        } else {
            showNetworkErrorDialog(id)
        }
    }

    private fun observeViewModel() {
        viewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            if (recipe != null) {
                loadImage(recipe.image)
                loadInfoRecipe(recipe)
                renderTags(recipe.tags)
                ingredients(recipe.ingredients)
                instructions(recipe.instructions)
            }
        }
        
        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                if (NetworkUtils.isNetworkError(Exception(error))) {
                    showNetworkErrorDialog(requireArguments().getInt(ARG_RECIPE_ID))
                }
            }
        }
    }

    private fun showNetworkErrorDialog(recipeId: Int) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle(R.string.network_error_recipe_dialog_title)
            .setMessage(R.string.network_error_recipe_dialog_message)
            .setPositiveButton(R.string.network_error_retry) { _, _ ->
                if (NetworkUtils.isNetworkAvailable(requireContext())) {
                    viewModel.loadRecipe(recipeId)
                } else {
                    showNetworkErrorDialog(recipeId)
                }
            }
            .setNegativeButton(R.string.network_error_settings) { _, _ ->
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
            .setCancelable(false)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadImage(imageUrl: String) {
        binding.image.load(imageUrl) {
            crossfade(true)
            placeholder(android.R.color.darker_gray)
        }
    }

    private fun loadInfoRecipe(recipe: Recipe) {
        binding.title.text = recipe.name
        binding.rating.text = getString(R.string.rating_format, recipe.rating)
        val totalMinutes = recipe.prepTimeMinutes + recipe.cookTimeMinutes
        binding.time.text = getString(R.string.time_minutes_format, totalMinutes)
        binding.servings.text = getString(R.string.servings_format, recipe.servings)
        binding.difficulty.text =
            getString(R.string.difficulty_format, recipe.difficulty)
    }

    private fun renderTags(tags: List<String>) {
        binding.chipGroupTags.removeAllViews()
        tags.forEach { tag ->
            binding.chipGroupTags.addView(
                Chip(requireContext()).apply {
                    text = getString(R.string.tag_chip_format, tag)
                    isClickable = false
                    isCheckable = false
                }
            )
        }
    }

    private fun ingredients(ingredients: List<String>) {
        binding.ingredients.text = ingredients
            .joinToString(separator = "\n") {
                getString(
                    R.string.ingredient_item_format,
                    it
                )
            }
    }

    private fun instructions(instructions: List<String>) {
        binding.instructions.text = instructions.mapIndexed { i, step ->
            "${i + 1}. $step"
        }.joinToString(separator = "\n\n")
    }
}
