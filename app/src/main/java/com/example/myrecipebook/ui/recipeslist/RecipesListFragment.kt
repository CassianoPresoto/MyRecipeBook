package com.example.myrecipebook.ui.recipeslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrecipebook.R
import com.example.myrecipebook.databinding.FragmentRecipesListBinding
import com.example.myrecipebook.ui.main.MainActivity
import com.example.myrecipebook.ui.recipe.RecipeFragment

class RecipesListFragment : Fragment() {

    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RecipesAdapter
    private val viewModel: RecipesListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = RecipesAdapter(onClick = { recipe ->
            val fragment = RecipeFragment.newInstance(recipe.id)
            parentFragmentManager.beginTransaction().replace(
                (requireActivity() as MainActivity).findViewById<View>(R.id.container).id,
                fragment
            ).addToBackStack(null).commit()
        })
        binding.recyclerRecipesList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerRecipesList.adapter = adapter

        viewModel.recipes.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            // TODO: show/hide progress bar in layout
        }
        viewModel.error.observe(viewLifecycleOwner) {
            // TODO: show error state/snackbar
        }

        // Trigger initial load
        viewModel.loadRecipes(limit = 100000, skip = 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
