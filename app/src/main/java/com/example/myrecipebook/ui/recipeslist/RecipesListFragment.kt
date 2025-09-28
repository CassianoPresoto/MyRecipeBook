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
import androidx.fragment.app.viewModels

class RecipesListFragment : Fragment() {

    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RecipesAdapter
    private val viewModel: RecipesListViewModel by viewModels()

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
