package com.example.myrecipebook.ui.recipeslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myrecipebook.common.domain.model.Recipe
import com.example.myrecipebook.rest.network.NetworkProvider
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class RecipesListViewModel : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var currentSkip = 0
    private val pageLimit = 10

    fun loadRecipes(append: Boolean = false) {
        _loading.value = true
        _error.value = null
        
        val skip = if (append) currentSkip else 0
        val dataSource = NetworkProvider.recipesDataSource
        val disposable = dataSource.getRecipes(limit = pageLimit, skip = skip)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.items }
            .subscribe(
                { list ->
                    val current = _recipes.value.orEmpty()
                    _recipes.value = if (append) current + list else list
                    currentSkip = if (append) currentSkip + list.size else list.size
                    _loading.value = false
                },
                { t ->
                    _error.value = t.message
                    _loading.value = false
                }
            )
        disposables.add(disposable)
    }

    fun loadNextPage() {
        loadRecipes(append = true)
    }

    fun refresh() {
        currentSkip = 0
        loadRecipes(append = false)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}
