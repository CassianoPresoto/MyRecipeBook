package com.example.myrecipebook.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.example.myrecipebook.R
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

        viewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            if (recipe != null) {
                binding.image.load(recipe.image) {
                    crossfade(true)
                    placeholder(android.R.color.darker_gray)
                }
                binding.title.text = recipe.name
                binding.rating.text = getString(R.string.rating_format, recipe.rating)
                val totalMinutes = recipe.prepTimeMinutes + recipe.cookTimeMinutes
                binding.time.text = getString(R.string.time_minutes_format, totalMinutes)
                binding.servings.text = getString(R.string.servings_format, recipe.servings)
                binding.difficulty.text =
                    getString(R.string.difficulty_format, recipe.difficulty.toString())
                binding.chipGroupTags.removeAllViews()
                recipe.tags.forEach { tag ->
                    val chip = Chip(requireContext()).apply {
                        text = getString(R.string.tag_chip_format, tag)
                        isClickable = false
                        isCheckable = false
                    }
                    binding.chipGroupTags.addView(chip)
                }
                binding.ingredients.text = recipe.ingredients.joinToString(separator = "\n")
                binding.instructions.text = recipe.instructions.mapIndexed { i, step ->
                    "${i + 1}. $step"
                }.joinToString(separator = "\n\n")
            }
        }

        viewModel.loadRecipe(id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
