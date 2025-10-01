package com.example.myrecipebook.ui.recipeslist

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecipebook.R
import com.example.myrecipebook.common.utils.NetworkUtils
import com.example.myrecipebook.databinding.FragmentRecipesListBinding
import com.example.myrecipebook.ui.main.MainActivity
import com.example.myrecipebook.ui.recipe.RecipeFragment
import coil.imageLoader

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
            if (NetworkUtils.isNetworkAvailable(requireContext())) {
                val fragment = RecipeFragment.newInstance(recipe.id)
                parentFragmentManager.beginTransaction().replace(
                    (requireActivity() as MainActivity).findViewById<View>(R.id.container).id,
                    fragment
                ).addToBackStack(null).commit()
            } else {
                showNetworkErrorDialog()
            }
        })
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.initialPrefetchItemCount = 5
        binding.recyclerRecipesList.layoutManager = layoutManager
        binding.recyclerRecipesList.adapter = adapter
        binding.recyclerRecipesList.setItemViewCacheSize(10)
        setupInfiniteScroll()
        setupErrorHandling()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            viewModel.loadRecipes()
        } else {
            showNetworkError()
        }
    }

    private fun observeViewModel() {
        viewModel.recipes.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            showContent()
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            val hasItems = viewModel.recipes.value?.isNotEmpty() == true
            if (isLoading && !hasItems) {
                showLoading()
            } else if (!isLoading) {
                showContent()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                if (NetworkUtils.isNetworkError(Exception(error))) {
                    showNetworkError()
                } else {
                    showGenericError(error)
                }
            }
        }
    }

    private fun setupInfiniteScroll() {
        binding.recyclerRecipesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                val isLoading = viewModel.loading.value == true
                if (!isLoading && lastVisibleItem >= totalItemCount - 3 && totalItemCount > 0) {
                    if (NetworkUtils.isNetworkAvailable(requireContext())) {
                        viewModel.loadNextPage()
                    }
                }
            }
        })
    }

    private fun setupErrorHandling() {
        binding.retryButton.setOnClickListener {
            if (NetworkUtils.isNetworkAvailable(requireContext())) {
                viewModel.refresh()
            } else {
                showNetworkError()
            }
        }

        binding.settingsButton.setOnClickListener {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerRecipesList.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
    }

    private fun showContent() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerRecipesList.visibility = View.VISIBLE
        binding.errorLayout.visibility = View.GONE
    }

    private fun showNetworkError() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerRecipesList.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE
        binding.errorTitle.text = getString(R.string.network_error_title)
        binding.errorMessage.text = getString(R.string.network_error_message)
    }

    private fun showGenericError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.recyclerRecipesList.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE
        binding.errorTitle.text = getString(R.string.error)
        binding.errorMessage.text = message
    }

    private fun showNetworkErrorDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.network_error_recipe_dialog_title)
            .setMessage(R.string.network_error_recipe_dialog_message)
            .setPositiveButton(R.string.network_error_settings) { _, _ ->
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireContext().imageLoader.memoryCache?.clear()
        _binding = null
    }
}
