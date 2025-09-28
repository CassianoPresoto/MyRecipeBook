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

    fun loadRecipes(limit: Int? = null, skip: Int? = null) {
        _loading.value = true
        _error.value = null
        val dataSource = NetworkProvider.recipesDataSource
        val disposable = dataSource.getRecipes(limit, skip)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.recipes }
            .subscribe(
                { list ->
                    _recipes.value = list
                    _loading.value = false
                },
                { t ->
                    _error.value = t.message
                    _loading.value = false
                }
            )
        disposables.add(disposable)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}
