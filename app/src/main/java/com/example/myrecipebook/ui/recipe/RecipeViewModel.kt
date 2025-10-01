package com.example.myrecipebook.ui.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myrecipebook.common.domain.model.Recipe
import com.example.myrecipebook.rest.network.NetworkProvider
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class RecipeViewModel : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _recipe = MutableLiveData<Recipe?>()
    val recipe: LiveData<Recipe?> = _recipe

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadRecipe(id: Int) {
        _loading.value = true
        _error.value = null
        val dataSource = NetworkProvider.recipesDataSource
        val disposable = dataSource.getRecipeById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { item ->
                    _recipe.value = item
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
